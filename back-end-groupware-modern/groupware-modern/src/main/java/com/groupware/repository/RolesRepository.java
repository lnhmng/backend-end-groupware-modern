package com.groupware.repository;

import com.groupware.entity.user.role.Roles;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

public interface RolesRepository extends JpaRepository<Roles, Integer> {
    Optional<Roles> findByName(String name);

    @Query(value = "SELECT r.name as nameKey\n" +
            "\tFROM _roles r", nativeQuery = true)
    Set<String> getLstRole();

    @Query(value = "SELECT _m.name as name\n" +
            "\t, LOWER(CONCAT(_m.name,':', _p.permission_name)) as permissionName\n" +
            "\t, _p.permission_name as detailName" +
            "\tFROM menu_role_permission mrp\n" +
            "\tLEFT JOIN _user _u ON mrp.user_id = _u.id\n" +
            "\tLEFT JOIN menu_permission mp ON mrp.menu_permission_id = mp.id\n" +
            "\tLEFT JOIN _menu _m ON mp.menu_id = _m.id\n" +
            "\tLEFT JOIN _permission _p ON mp.permission_id = _p.id\n" +
            "\tWHERE _u.username = :username", nativeQuery = true)
    List<Map<String, String>> menuPermission(@Param("username") String username);
}
