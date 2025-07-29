package com.groupware.repository;

import com.groupware.entity.menu.MenuPermission;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@Repository
public interface MenuPermissionRepository extends JpaRepository<MenuPermission, Integer> {
    Optional<MenuPermission> findById(Integer id);

    @Query(value = "SELECT m.name_show as nameShow\n" +
            "\t\t, mp.id as id\n" +
            "\t\t, p.permission_name as permissionName\n" +
            "\tFROM menu_permission mp\n" +
            "\tLEFT JOIN _menu m ON mp.menu_id = m.id\n" +
            "\tLEFT JOIN _permission p ON mp.permission_id = p.id", nativeQuery = true)
    List<Map<String, Object>> lstMenuPermission();
}
