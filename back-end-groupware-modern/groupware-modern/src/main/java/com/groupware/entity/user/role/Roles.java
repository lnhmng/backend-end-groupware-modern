package com.groupware.entity.user.role;

import jakarta.persistence.*;
import lombok.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "_roles")
public class Roles {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(length = 30)
    private String name;

    public String getName() {
        return "ROLE_" + name.toUpperCase();
    }

//    @ManyToMany(fetch = FetchType.LAZY)
//    @JoinTable(name = "role_permission",
//            joinColumns = @JoinColumn(name = "role_id"),
//            inverseJoinColumns = @JoinColumn(name = "permission_id"))
//    private Set<Permissions> permission = new HashSet<>();
}
