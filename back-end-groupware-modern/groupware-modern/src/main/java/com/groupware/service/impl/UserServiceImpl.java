package com.groupware.service.impl;

import com.groupware.common.CommonResponse;
import com.groupware.dto.common.EmployeeDTO;
import com.groupware.dto.request.EmployeeEditRequest;
import com.groupware.dto.response.UserDetailResponse;
import com.groupware.entity.department.Department;
import com.groupware.entity.menu.MenuPermission;
import com.groupware.entity.position.Position;
import com.groupware.entity.user.User;
import com.groupware.entity.user.permission.PermissionRoute;
import com.groupware.exception.CommonException;
import com.groupware.repository.*;
import com.groupware.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.security.Principal;
import java.util.*;

@Slf4j
@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private PermissionRouteRepository permissionRouteRepository;
    @Autowired
    private MenuPermissionRepository menuPermissionRepository;
    @Autowired
    private RolesRepository rolesRepository;
    @Autowired
    private PositionRepository positionRepository;
    @Autowired
    private DepartmentRepository departmentRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public UserDetailResponse detailUser(Integer id) {
        try {
            var detailUser = userRepository.detailEmployee(id);
            var user = userRepository.findById(id)
                    .orElseThrow(()-> new  RuntimeException("not found user id"));

            List<Map<String, String>> permissionByMenu = rolesRepository.menuPermission(user.getUsername());
            Map<String, Object> permissionRoute = new HashMap<>();
                    Set<PermissionRoute> permissionRoutes = user.getUserPermissionRoute();
            permissionRoutes.forEach(route -> {
                permissionRoute.put("id",route.getId());
                permissionRoute.put("label",route.getPermissionRouteName());
            });

            return UserDetailResponse.builder()
                    .id(user.getId())
                    .username(user.getUsername())
                    .firstName(user.getFirstname())
                    .lastName(user.getLastname())
                    .email(user.getEmail())
                    .position(user.getPosition())
                    .department(user.getDepartment())
                    .active(user.isActive())
                    .useStatus(user.isUseStatus())
                    .createdAt(user.getCreatedAt())
                    .permissionRoutes(permissionRoute)
                    .menuPermission(permissionByMenu)
                    .build();
        } catch (Exception e){
            log.error("### Error detailUser: " + e.getMessage());
            throw CommonException.of("Error: {}", e.getMessage());
        }
    }

    @Override
    public List<Map<String, Object>> lstUser() {
        try {
            return userRepository.lstEmployee();
        }catch (Exception e){
            log.error("### Error lstUser: "+ e.getMessage());
            throw CommonException.of("Error: {}", e.getMessage());
        }
    }

    @Override
    public User editEmployee(EmployeeEditRequest request) {
        try {
            var user = userRepository.findById(request.getId())
                    .orElseThrow(() -> new RuntimeException("not found employee id"));
            user.setFirstname(request.getFirstname());
            user.setLastname(request.getLastname());
            user.setActive(request.isActive());
            user.setUseStatus(request.isUseStatus());

            Position position = positionRepository.findById(request.getPositionId())
                    .orElseThrow(() -> new RuntimeException("not found position id"));
            user.setPosition(position);
            Department department = departmentRepository.findById(request.getDepartmentId())
                    .orElseThrow(() -> new RuntimeException("not found department id"));
            user.setDepartment(department);

            PermissionRoute permissionRoute = permissionRouteRepository.findById(request.getPermissionRoute())
                    .orElseThrow(()-> new RuntimeException("permission route rights did not exist"));
            Set<PermissionRoute> permissionRoutes = new HashSet<>();
            permissionRoutes.add(permissionRoute);

            Set<MenuPermission> menuPermissions = new HashSet<>();
            for (Integer i : permissionRouteRepository.lstMenuPermission(request.getPermissionRoute())) {
                MenuPermission menuPermission = menuPermissionRepository.findById(i)
                        .orElseThrow(() -> new RuntimeException("not found menu permission detail"));
                menuPermissions.add(menuPermission);
            }

            user.setMenuRolePermission(menuPermissions);
            user.setUserPermissionRoute(permissionRoutes);
            var savedUser = userRepository.save(user);

            return user;
        }
        catch (Exception e){
            log.error("### Error editEmployee "+ e.getMessage());
            throw CommonException.of("Error: {}", e.getMessage());
        }
    }

    @Override
    public void deleteEmployee(int id) {
        try{
            var user = userRepository.findById(id)
                    .orElseThrow(()-> new RuntimeException("not found user id"));
            user.setUseStatus(false);
            userRepository.save(user);
        }
        catch (Exception e){
            log.error("### Error: "+ e.getMessage());
            throw CommonException.of("Error: {}", e.getMessage());
        }
    }

    @Override
    public List<Map<String, Object>> empStructure() {
        try {
            List<Map<String, Object>> empLstCore = userRepository.empStructureCore();
            List<Map<String, Object>> lst = userRepository.empStructure();
            List<EmployeeDTO> employeeDTOS = new ArrayList<>();
            for (Map<String, Object> map : lst){
                var employee = EmployeeDTO.builder()
                        .id((Integer) map.get("id"))
                        .departmentId((Integer) map.get("departmentId"))
                        .positionId((Integer) map.get("positionId"))
                        .name((String) map.get("name"))
                        .title((String) map.get("title"))
                        .departmentParentId((Integer) map.get("departmentParentId"))
                        .build();
                employeeDTOS.add(employee);
            }

            Map<String, Integer> lastIdMap = new HashMap<>();
            Map<Integer, List<Map<Integer, Integer>>> partentLstMap = new HashMap<>();

            for (EmployeeDTO employee : employeeDTOS) {
                String key = employee.getDepartmentId() + "-" + employee.getPositionId();

                Map<Integer, Integer> chilMap = new HashMap<>();

                if (lastIdMap.containsKey(key)) {
                    int lastId = lastIdMap.get(key);
                    employee.setDepartmentParentId(lastId);
                }

                lastIdMap.put(key, employee.getId());
            }

            lastIdMap = new HashMap<>();
            for (EmployeeDTO employeeDTO: employeeDTOS){
                String key = String.valueOf(employeeDTO.getDepartmentId());

                lastIdMap.put(key, employeeDTO.getId());
            }

//            return employeeDTOS;
            return empLstCore;
        } catch (Exception e){
            log.error("### Error empStructure: " + e.getMessage());
            throw CommonException.of("Error: {}", e.getMessage());
        }
    }

    @Override
    public CommonResponse<?> changePassword(String currentPassword, String newPassword, Principal principal) {
        try {
            User user = userRepository.findByUsername(principal.getName())
                    .orElseThrow(()-> new RuntimeException("Not found username"));
            if (passwordEncoder.matches(currentPassword, user.getPassword())){
                String encodedNewPassword = passwordEncoder.encode(newPassword);
                user.setPassword(encodedNewPassword);
                userRepository.save(user);
                return CommonResponse.success("Okay");
            }
            throw CommonException.of("Current password not match");
        }catch (Exception e){
            log.error("### Error changePassword: " + e.getMessage());
            throw CommonException.of("Error: {}", e.getMessage());
        }
    }
}
