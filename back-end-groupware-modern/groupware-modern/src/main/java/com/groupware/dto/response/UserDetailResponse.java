package com.groupware.dto.response;

import com.groupware.entity.department.Department;
import com.groupware.entity.position.Position;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.List;
import java.util.Map;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UserDetailResponse {
    private Integer id;
    private String username;
    private String email;
    private String firstName;
    private String lastName;
    private Position position;
    private Department department;
    private boolean active;
    private boolean useStatus;
    private Date createdAt;
    private Map<String, Object> permissionRoutes;
    private List<Map<String, String>> menuPermission;
}
