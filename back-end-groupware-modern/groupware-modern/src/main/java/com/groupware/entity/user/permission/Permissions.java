package com.groupware.entity.user.permission;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name = "_permission")
public class Permissions {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private String permissionName;

    private String method;
}
