package com.groupware.dto.request;

import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import jakarta.validation.constraints.NotBlank;

import java.util.Set;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PermissionRouteRequest {
    @NotBlank(message = "Permission name is required")
    @Size(min = 2, max = 100)
    private String permissionRouteName;

    private Set<Integer> routeDetail;
}
