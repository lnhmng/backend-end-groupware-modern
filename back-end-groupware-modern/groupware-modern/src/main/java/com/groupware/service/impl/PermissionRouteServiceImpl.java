package com.groupware.service.impl;

import com.groupware.dto.request.PermissionRouteEditRequest;
import com.groupware.dto.request.PermissionRouteRequest;
import com.groupware.entity.menu.MenuPermission;
import com.groupware.entity.user.permission.PermissionRoute;
import com.groupware.exception.CommonException;
import com.groupware.repository.MenuPermissionRepository;
import com.groupware.repository.PermissionRouteRepository;
import com.groupware.repository.RolesRepository;
import com.groupware.service.PermissionRouteService;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@Service
public class PermissionRouteServiceImpl implements PermissionRouteService {

    @Autowired
    private PermissionRouteRepository permissionRouteRepository;
    @Autowired
    private MenuPermissionRepository menuPermissionRepository;

    @Autowired
    private RolesRepository rolesRepository;

    @Override
    @Transactional
    public PermissionRoute createPermissionRoute(PermissionRouteRequest request) {
        try {
            String permissionRouteName = request.getPermissionRouteName();
            if (permissionRouteRepository.existsByPermissionRouteName(permissionRouteName)) {
                throw new IllegalArgumentException("Route name has been duplicated");
            }
            PermissionRoute permissionRoute = new PermissionRoute();
            permissionRoute.setPermissionRouteName(permissionRouteName);

            Set<MenuPermission> menuPermissions = new HashSet<>();
            Iterator<Integer> iterator = request.getRouteDetail().iterator();
            while (iterator.hasNext()) {
                MenuPermission menuPermission = menuPermissionRepository.findById(iterator.next())
                        .orElseThrow(() -> new RuntimeException("Error: permission not found"));
                menuPermissions.add(menuPermission);
            }
            permissionRoute.setMenuPermissions(menuPermissions);
            permissionRouteRepository.save(permissionRoute);
            return permissionRoute;
        } catch (Exception e) {
            log.error("### Error createPermissionRoute: " + e.getMessage());
            throw CommonException.of("Error: {}", e.getMessage());
        }
    }

    @Override
    @Transactional
    public PermissionRoute updatePermissionRoute(PermissionRouteEditRequest request) {
        try {
            var permissionRouteMain = permissionRouteRepository.findById(request.getId())
                    .orElseThrow(() -> new RuntimeException("Error: not found Route Id"));
            if (permissionRouteRepository.checkExitUpdatePermissionRoute(request.getPermissionRouteName(), request.getId()) != 0){
                throw CommonException.of("Error: duplicate value permission name");
            }

            Set<MenuPermission> menuPermissions = new HashSet<>();
            Iterator<Integer> iterator = request.getRouteDetail().iterator();
            while (iterator.hasNext()) {
                MenuPermission menuPermission = menuPermissionRepository.findById(iterator.next())
                        .orElseThrow(() -> new RuntimeException("Error: permission not found"));
                menuPermissions.add(menuPermission);
            }

            permissionRouteMain.setMenuPermissions(menuPermissions);
            permissionRouteMain.setPermissionRouteName(request.getPermissionRouteName());

            permissionRouteRepository.save(permissionRouteMain);

            return permissionRouteRepository.findById(permissionRouteMain.getId())
                    .orElseThrow(() -> new RuntimeException("not found data"));
        } catch (Exception e) {
            log.error("### Error updatePermissionRoute:" + e.getMessage());
            throw CommonException.of("Error: {}", e.getMessage());
        }
    }

    @Override
    public boolean deletePermissionRoute(int id) {
        try {
            var permissionRoute = permissionRouteRepository.findById(id)
                    .orElseThrow(() -> new RuntimeException("not found id"));
            permissionRouteRepository.delete(permissionRoute);
            return true;
        } catch (Exception e) {
            log.error("### Error deletePermissionRoute: " + e.getMessage());
            throw CommonException.of("Error: {}", e.getMessage());
        }
    }

    @Override
    public List<Map<String, String>> getPermissionRoute() {
        try {
            List<Map<String, String>> permissionRoutes = permissionRouteRepository.lstPermissionRoute();
            return permissionRoutes;
        } catch (Exception e) {
            log.error("### Error getPermissionRoute: " + e.getMessage());
            throw CommonException.of("Error: {}", e.getMessage());
        }
    }

    @Override
    public Map<String, List<String>> getDetailPermissionRoute(int id) {
        try {
            List<Map<String, String>> detailPermissionRoute = permissionRouteRepository.getDetailPermissionRoute(id);
            Map<String, List<String>> mapResult = detailPermissionRoute.stream()
                    .collect(Collectors.groupingBy(map -> map.get("nameShow"),
                            Collectors.mapping(map -> map.get("permissionName"), Collectors.toList())));
            return mapResult;
        } catch (Exception e) {
            log.error("### Error getDetailPermissionRoute: " + e.getMessage());
            throw CommonException.of("Error: {}", e.getMessage());
        }
    }

    @Override
    public Map<String, Object> permissionRoute(int id) {
        try {
            PermissionRoute route = permissionRouteRepository.findById(id)
                    .orElseThrow(() -> new RuntimeException("not found permission Id"));
            Set<MenuPermission> maps = route.getMenuPermissions();
            List<Integer> idArray = new ArrayList<>();
            maps.stream().forEach(item -> {
                idArray.add(item.getId());
            });
            Map<String, Object> mapResult = new HashMap<>();
            mapResult.put("permissionName", route.getPermissionRouteName());
            mapResult.put("permissionId", idArray.stream().sorted());

            return mapResult;
        } catch (Exception e) {
            log.error("### Error permissionRoute " + e.getMessage());
            throw CommonException.of("Error: {}", e.getMessage());
        }
    }

    @Override
    public Map<String, String> getLstRole() {
        try {
            Map<String, String> result = new HashMap<>();
            Set<String> roles = rolesRepository.getLstRole();
            Iterator<String> iterator = roles.iterator();
            while (iterator.hasNext()) {
                String role = iterator.next();
                result.put(role, role);
            }
            return result;
        } catch (Exception e) {
            log.error("### Error getLstRole: " + e.getMessage());
            throw CommonException.of("Error: {}", e.getMessage());
        }
    }

    @Override
    public Map<String, List<Map<String, String>>> lstMenuPermission() {
        try {
            List<Map<String, Object>> maps = menuPermissionRepository.lstMenuPermission();
            Map<String, List<Map<String, String>>> result = new HashMap<>();

            maps.stream().forEach(item -> {
                var name = item.get("nameShow").toString();
                var permissionName = item.get("permissionName").toString();
                var id = item.get("id").toString();

                var permissionMap = new HashMap<String, String>();
                permissionMap.put("id", id);
                permissionMap.put("permissionName", permissionName);

                if (result.containsKey(name)) {
                    result.get(name).add(permissionMap);
                } else {
                    var permissionList = new ArrayList<Map<String, String>>();
                    permissionList.add(permissionMap);
                    result.put(name, permissionList);
                }
            });

            return result;
        } catch (Exception e) {
            log.error("### Error lstMenuPermission " + e.getMessage());
            throw CommonException.of("Error: {}", e.getMessage());
        }
    }
}
