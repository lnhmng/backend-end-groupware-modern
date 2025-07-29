package com.groupware.entity.menu;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name = "menu_permission")
public class MenuPermission {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private Integer permissionId;

    private Integer menuId;
}
