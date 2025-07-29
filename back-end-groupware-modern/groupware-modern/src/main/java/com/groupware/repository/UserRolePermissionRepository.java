package com.groupware.repository;

import com.groupware.entity.user.permission.UserRolePermission;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserRolePermissionRepository extends JpaRepository<UserRolePermission, Integer> {
    @Query(value = "SELECT CONCAT(r.name, ':', p.permission_name) as permission_Name\n" +
            "FROM user_role_permission urp\n" +
            "    LEFT JOIN _user u on urp.user_id = u.id\n" +
            "    LEFT JOIN role_permission rp ON rp.id = urp.role_permission_id\n" +
            "    LEFT JOIN _roles r ON r.id = rp.role_id\n" +
            "    LEFT JOIN _permission p ON p.id = rp.permission_id\n" +
            "WHERE u.username = :username", nativeQuery = true)
    List<String> rolePermission(@Param("username") String username);

    @Query(value = "SELECT r.name\n" +
            "\tFROM user_roles ur\n" +
            "\tLEFT JOIN _user u ON ur.user_id = u.id\n" +
            "\tLEFT JOIN _roles r ON ur.role_id = r.id\n" +
            "\tWHERE u.username = :username", nativeQuery = true)
    List<String> userRole(@Param("username") String username);
}
