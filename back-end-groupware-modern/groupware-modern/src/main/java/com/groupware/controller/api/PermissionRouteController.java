package com.groupware.controller.api;

import com.groupware.common.CommonResponse;
import com.groupware.common.ResponseCode;
import com.groupware.dto.request.PermissionRouteEditRequest;
import com.groupware.dto.request.PermissionRouteRequest;
import com.groupware.service.DepartmentService;
import com.groupware.service.PermissionRouteService;
import com.groupware.service.PositionService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@CrossOrigin(origins = "*", maxAge = 3600)
@RequestMapping("/api/v1/admin/per/route")
public class PermissionRouteController {

    @Autowired
    private PermissionRouteService permissionRouteService;
    @Autowired
    private PositionService positionService;
    @Autowired
    private DepartmentService departmentService;

    @PostMapping
    public CommonResponse<?> createPermissionRoute(@Valid @RequestBody PermissionRouteRequest permissionRouteRequest, BindingResult result) {
        if (result.hasErrors()){
            Map<String, String> errors = new HashMap<>();

            for (FieldError error : result.getFieldErrors()) {
                errors.put(error.getField(), error.getDefaultMessage());
            }
            return CommonResponse.of(HttpStatus.BAD_REQUEST, ResponseCode.FAILED, "FAILED", errors);
        }
        return CommonResponse.success(permissionRouteService.createPermissionRoute(permissionRouteRequest));
    }

    @PutMapping
    public CommonResponse<?> updatePermissionRoute(@Valid @RequestBody PermissionRouteEditRequest permissionRouteRequest, BindingResult result) {
        if (result.hasErrors()){
            Map<String, String> errors = new HashMap<>();
            for (FieldError error : result.getFieldErrors()) {
                errors.put(error.getField(), error.getDefaultMessage());
            }
            return CommonResponse.of(HttpStatus.BAD_REQUEST, ResponseCode.FAILED, "FAILED", errors);
        }
        return CommonResponse.success(permissionRouteService.updatePermissionRoute(permissionRouteRequest));
    }

    @DeleteMapping
    public CommonResponse<?> deletePermissionRoute(@RequestParam(defaultValue = "0", required = false) int id) {
        return CommonResponse.success(permissionRouteService.deletePermissionRoute(id));
    }

    @GetMapping
    public CommonResponse<?> getPermissionRoute() {
        Map<String, Object> result = new HashMap<>();
        result.put("permissions", permissionRouteService.getPermissionRoute());
        result.put("roles", permissionRouteService.getLstRole());
        result.put("menuPermission", permissionRouteService.lstMenuPermission());
        result.put("position", positionService.lstPosition());
        result.put("department", departmentService.lstDepartment());
        return CommonResponse.success(result);
    }

    @PatchMapping("/{id}")
    public CommonResponse<?> getDetailPermissionRoute(@PathVariable int id) {
        return CommonResponse.success(permissionRouteService.permissionRoute(id));
    }
}
