package com.groupware.entity.user.permission;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name = "role_permission")
public class RolePermission {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private Integer role_id;
    private Integer permission_id;
}
