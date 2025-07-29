package com.groupware.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Set;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PermissionRouteEditRequest {
    private Integer id;
    @NotBlank(message = "Permission name is required")
    @Size(min = 2, max = 100)
    private String permissionRouteName;
    private Set<Integer> routeDetail;
}
