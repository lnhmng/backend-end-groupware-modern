package com.groupware.service.impl;

import com.groupware.dto.request.AuthenticationRequest;
import com.groupware.dto.response.AuthenticationResponse;
import com.groupware.dto.request.RegisterRequest;
import com.groupware.entity.department.Department;
import com.groupware.entity.menu.MenuPermission;
import com.groupware.entity.position.Position;
import com.groupware.entity.user.permission.PermissionRoute;
import com.groupware.entity.user.role.Roles;
import com.groupware.repository.*;
import com.groupware.exception.CommonException;
import com.groupware.common.CommonResponse;
import com.groupware.common.ResponseCode;
import com.groupware.config.security.JwtService;
import com.groupware.entity.token.Token;
import com.groupware.entity.token.TokenType;
import com.groupware.entity.user.User;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class AuthenticationService {
    private final UserRepository repository;
    private final TokenRepository tokenRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;
    private final RolesRepository rolesRepository;
    private final UserRolePermissionRepository userRolePermissionRepository;
    private final PermissionRouteRepository permissionRouteRepository;
    private final MenuPermissionRepository menuPermissionRepository;
    private final PositionRepository positionRepository;
    private final DepartmentRepository departmentRepository;
    final String password = "123123";

    @Transactional
    public CommonResponse<?> register(RegisterRequest request) {

        Boolean exitMailCheck = repository.existsByEmail(request.getEmail());
        if (exitMailCheck || repository.existsByUsername(request.getUsername())) {
            throw new CommonException("Duplicate email or username");
        }

        PermissionRoute permissionRoute = permissionRouteRepository.findById(request.getPermissionRoute())
                .orElseThrow(() -> new RuntimeException("permission route rights did not exist"));
        Set<PermissionRoute> permissionRoutes = new HashSet<>();
        permissionRoutes.add(permissionRoute);

        Set<MenuPermission> menuPermissions = new HashSet<>();
        for (Integer i : permissionRouteRepository.lstMenuPermission(request.getPermissionRoute())) {
            MenuPermission menuPermission = menuPermissionRepository.findById(i)
                    .orElseThrow(() -> new RuntimeException("not found menu permission detail"));
            menuPermissions.add(menuPermission);
        }

        Position position = positionRepository.findById(request.getPositionId())
                .orElseThrow(() -> new RuntimeException("not found Position"));
        Department department = departmentRepository.findById(request.getDepartmentId())
                .orElseThrow(() -> new RuntimeException("not found Department"));

        Set<String> strRoles = request.getRole();
        Set<Roles> roles = new HashSet<>();
        if (strRoles == null) {
            Roles userRole = rolesRepository.findByName("USER")
                    .orElseThrow(() -> new RuntimeException("Error: Role is not found"));

            roles.add(userRole);
        } else {
            strRoles.forEach(role -> {
                Roles addRole = rolesRepository.findByName(role)
                        .orElseThrow(() -> new RuntimeException("Error: Role is not found"));
                roles.add(addRole);
            });
        }

        var user = User.builder()
                .firstname(request.getFirstname())
                .username(request.getUsername())
                .lastname(request.getLastname())
                .email(request.getEmail())
                .password(passwordEncoder.encode(password))
                .position(position)
                .department(department)
                .userPermissionRoute(permissionRoutes)
                .menuRolePermission(menuPermissions)
                .roles(roles)
                .active(true)
                .useStatus(true)
                .build();
        var savedUser = repository.save(user);
        return CommonResponse.success(user);
    }

    public CommonResponse<?> authenticate(AuthenticationRequest request) {
        try {
            Collection<GrantedAuthority> authorities = new ArrayList<>();
            List<String> roles = new ArrayList<>();
            String srtRole = "";
            for (String role : userRolePermissionRepository.userRole(request.getUsername())) {
                srtRole = "ROLE_" + role.toUpperCase();
                roles.add(srtRole);
                authorities.add(new SimpleGrantedAuthority(srtRole));
            }
            for (String role : userRolePermissionRepository.rolePermission(request.getUsername())) {
                srtRole = role.toLowerCase();
                roles.add(srtRole);
                authorities.add(new SimpleGrantedAuthority(srtRole));
            }

            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            request.getUsername(),
                            request.getPassword(),
                            authorities
                    )
            );

            var user = repository.findByUsername(request.getUsername())
                    .orElseThrow();
            var jwtToken = jwtService.generateToken(user);
            var refreshToken = jwtService.generateRefreshToken(user);
            revokeAllUserTokens(user);
            saveUserToken(user, jwtToken);

            List<Map<String, String>> permissionByMenu = rolesRepository.menuPermission(user.getUsername());

            Map<String, List<String>> result = permissionByMenu.stream()
                    .collect(Collectors.groupingBy(map -> map.get("name"),
                            Collectors.mapping(map -> map.get("permissionName"), Collectors.toList())));

            return CommonResponse.of(HttpStatus.OK, ResponseCode.SUCCESS, "SUCCESS"
                    , AuthenticationResponse.builder()
                            .accessToken(jwtToken)
                            .username(user.getUsername())
                            .refreshToken(refreshToken)
                            .id(user.getId())
                            .roles(roles)
                            .menuPermission(result)
                            .email(user.getEmail())
                            .department(user.getDepartment())
                            .build());
        } catch (Exception e) {
            log.error("### login error " + e.getMessage());
            throw CommonException.of("Password or user not correct");
        }
    }

    private void saveUserToken(User user, String jwtToken) {
        var token = Token.builder()
                .user(user)
                .token(jwtToken)
                .tokenType(TokenType.BEARER)
                .expired(false)
                .revoked(false)
                .build();
        tokenRepository.save(token);
    }

    private void revokeAllUserTokens(User user) {
        var validUserTokens = tokenRepository.findAllValidTokenByUser(user.getId());
        if (validUserTokens.isEmpty())
            return;
        validUserTokens.forEach(token -> {
            token.setExpired(true);
            token.setRevoked(true);
        });
        tokenRepository.saveAll(validUserTokens);
    }

    public void refreshToken(
            HttpServletRequest request,
            HttpServletResponse response
    ) throws Exception {
        final String authHeader = request.getHeader(HttpHeaders.AUTHORIZATION);
        final String refreshToken;
        final String userEmail;
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            return;
        }
        refreshToken = authHeader.substring(7);
        userEmail = jwtService.extractUsername(refreshToken);
        if (userEmail != null) {
            var user = this.repository.findByUsername(userEmail)
                    .orElseThrow();
            if (jwtService.isTokenValid(refreshToken, user)) {
                var accessToken = jwtService.generateToken(user);
                revokeAllUserTokens(user);
                saveUserToken(user, accessToken);
                var authResponse = AuthenticationResponse.builder()
                        .accessToken(accessToken)
                        .refreshToken(refreshToken)
                        .build();
                new ObjectMapper().writeValue(response.getOutputStream(), authResponse);
            }
        }
    }
}
