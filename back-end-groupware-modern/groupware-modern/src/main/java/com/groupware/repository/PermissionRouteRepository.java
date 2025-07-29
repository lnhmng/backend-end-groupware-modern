package com.groupware.repository;

import com.groupware.entity.user.permission.PermissionRoute;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

@Repository
public interface PermissionRouteRepository extends JpaRepository<PermissionRoute, Integer> {
    List<PermissionRoute> findAll();
    Optional<PermissionRoute> findById(Integer id);
    Optional<PermissionRoute> findByPermissionRouteName(String name);
    Boolean existsByPermissionRouteName(String name);

    @Query(value = "SELECT _m.name_show as nameShow\n" +
            "\t\t, _p.permission_name as permissionName\n" +
            "\tFROM(\n" +
            "\t\tSELECT m.*\n" +
            "\t\t\tFROM permission_route m\n" +
            "\t\t\tWHERE m.id = :id\n" +
            "\t\t) pr\n" +
            "\tLEFT JOIN route_detail rd ON pr.id = rd.permission_route_id\n" +
            "\tLEFT JOIN menu_permission mp ON rd.menu_permission_id = mp.id\n" +
            "\tLEFT JOIN _permission _p ON mp.permission_id = _p.id\n" +
            "\tLEFT JOIN _menu _m ON mp.menu_id = _m.id", nativeQuery = true)
    List<Map<String, String>> getDetailPermissionRoute(@Param("id") int id);

    @Query(value = "SELECT rd.menu_permission_id\n" +
            "\tFROM route_detail rd\n" +
            "\tWHERE rd.permission_route_id = :permissionRouteId", nativeQuery = true)
    Set<Integer> lstMenuPermission(@Param("permissionRouteId") int permissionRouteId);

    @Query(value = "SELECT pr.id as id\n" +
            "\t\t, pr.permission_route_name as label\n" +
            "\tFROM permission_route pr", nativeQuery = true)
    List<Map<String, String>> lstPermissionRoute();

    @Query(value = "SELECT count(*)\n" +
            "\tFROM permission_route pr\n" +
            "\tWHERE pr.permission_route_name = :name\n" +
            "\tAND pr.id !=  :id", nativeQuery = true)
    int checkExitUpdatePermissionRoute(@Param("name") String name, @Param("id") int id);
}
