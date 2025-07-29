package com.groupware.service;

import com.groupware.dto.request.PermissionRouteEditRequest;
import com.groupware.dto.request.PermissionRouteRequest;
import com.groupware.entity.user.permission.PermissionRoute;

import java.util.List;
import java.util.Map;

public interface PermissionRouteService {
    PermissionRoute createPermissionRoute(PermissionRouteRequest request);

    PermissionRoute updatePermissionRoute(PermissionRouteEditRequest request);

    boolean deletePermissionRoute(int id);

    List<Map<String, String>> getPermissionRoute();

    Map<String, List<String>> getDetailPermissionRoute(int id);

    Map<String, Object> permissionRoute(int id);

    Map<String, String> getLstRole();

    Map<String, List<Map<String, String>>> lstMenuPermission();
}

