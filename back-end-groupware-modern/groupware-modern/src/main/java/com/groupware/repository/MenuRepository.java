package com.groupware.repository;

import com.groupware.entity.menu.Menu;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

@Repository
public interface MenuRepository extends JpaRepository<Menu, Integer> {
    @Query(value = "SELECT _m.api_link as apiLink\n" +
            "\t\t, UPPER(_p.method) as method\n" +
            "\t\t, LOWER(CONCAT(_m.name, ':', _p.permission_name)) as rolePermission\n" +
            "\tFROM menu_permission mp\n" +
            "\tLEFT JOIN _menu _m ON mp.menu_id = _m.id\n" +
            "\tLEFT JOIN _permission _p ON mp.permission_id = _p.id", nativeQuery = true)
    List<Map<String, String>> rolePermission();
}
