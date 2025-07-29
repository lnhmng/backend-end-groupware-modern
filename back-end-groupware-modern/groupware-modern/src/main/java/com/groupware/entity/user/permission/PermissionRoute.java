package com.groupware.entity.user.permission;

import com.groupware.entity.menu.MenuPermission;
import jakarta.persistence.*;
import lombok.Data;

import java.util.Set;

@Data
@Entity
@Table(name = "permission_route")
public class PermissionRoute {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private String permissionRouteName;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(name = "route_detail",
            joinColumns = @JoinColumn(name = "permission_route_id"),
            inverseJoinColumns = @JoinColumn(name = "menu_permission_id"))
    private Set<MenuPermission> menuPermissions;
}