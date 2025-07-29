package com.groupware.entity.menu;

import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.UpdateTimestamp;

import java.util.Date;

@Data
@Entity
@Table(name = "_menu")
public class Menu {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private String apiLink;

    private String name;

    private String nameShow;

    private Integer menuParentId;

    private int menuType;

    private Integer modifiedBy;

    @UpdateTimestamp
    private Date modifiedDate;
}
