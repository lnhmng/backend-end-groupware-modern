package com.groupware.entity.user;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.groupware.entity.department.Department;
import com.groupware.entity.menu.MenuPermission;
import com.groupware.entity.position.Position;
import com.groupware.entity.user.permission.PermissionRoute;
import com.groupware.entity.user.role.Roles;
import jakarta.persistence.*;

import java.util.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "_user")
public class User implements UserDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private String username;
    @ManyToOne
    @JoinColumn(name = "departmentId")
    private Department department;

    @ManyToOne
    @JoinColumn(name = "positionId")
    private Position position;

    private String firstname;
    private String lastname;
    private String email;
    @JsonIgnore
    private String password;

    private boolean active;
    private boolean useStatus;

    private String createdBy;
    @CreationTimestamp
    private Date createdAt;

    private String updatedBy;
    @UpdateTimestamp
    private Date updatedAt;

//    @Enumerated(EnumType.STRING)
//    private Role role;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(name = "user_roles",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "role_id"))
    private Set<Roles> roles;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(name = "menu_role_permission",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "menu_permission_id"))
    private Set<MenuPermission> menuRolePermission;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(name = "user_permission_route",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "permission_route_id"))
    private Set<PermissionRoute> userPermissionRoute;

//    @OneToMany(mappedBy = "user")
//    private List<Token> tokens;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return null;
    }
    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return username;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}
