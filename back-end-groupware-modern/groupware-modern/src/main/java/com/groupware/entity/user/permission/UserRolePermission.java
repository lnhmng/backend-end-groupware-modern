package com.groupware.entity.user.permission;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name = "user_role_permission")
public class UserRolePermission {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private Integer userId;
    private Integer rolePermissionId;
}
