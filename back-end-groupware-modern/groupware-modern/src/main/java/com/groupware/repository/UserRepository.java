package com.groupware.repository;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import com.groupware.entity.user.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface UserRepository extends JpaRepository<User, Integer> {

    Optional<User> findByEmail(String email);

    Optional<User> findById(Integer id);

    Optional<User> findByUsername(String username);

    Boolean existsByEmail(String email);

    Boolean existsByUsername(String username);

    @Query(value = "SELECT _u.id\n" +
            "          , _u.username\n" +
            "          , _u.email\n" +
            "          , _u.active\n" +
            "          , _u.created_at as createAt\n" +
            "          , _u.use_status as useStatus\n" +
            "          , pr.permission_route_name\n" +
            "      FROM _user _u\n" +
            "      LEFT JOIN user_permission_route upr ON _u.id = upr.user_id\n" +
            "      LEFT JOIN permission_route pr ON upr.permission_route_id = pr.id\n" +
            "      WHERE _u.id = :id", nativeQuery = true)
    Map<String, Object> detailEmployee(@Param("id") Integer id);

    @Query(value = "SELECT _u.id\n" +
            "\t\t, concat(_u.firstname,' ', _u.lastname) as fullName\n" +
            "\t\t, _u.username\n" +
            "\t\t, _u.email\n" +
            "\t\t, _u.active\n" +
            "\t\t, d.department_name as departmentName\n" +
            "\t\t, p.position_name as positionName\n" +
            "\t\t, _u.created_at as createdAt\n" +
            "\t\t, _u.use_status as useStatus\n" +
            "\tFROM _user _u\n" +
            "\tLEFT JOIN _department d ON _u.department_id = d.id\n" +
            "\tLEFT JOIN _position p ON _u.position_id = p.id\n" +
            "\tWHERE _u.use_status = 1\n" +
            "\tORDER BY _u.id DESC\n", nativeQuery = true)
    List<Map<String, Object>> lstEmployee();

    @Query(value = "SELECT u.id AS id\n" +
            "\t\t, u.username AS name\n" +
            "\t\t, \n" +
            "\t\t\tCASE \n" +
            "\t\t\t\tWHEN u.id != (\n" +
            "\t\t\t\t\t\tSELECT MIN(m.id)\n" +
            "\t\t\t\t\t\t\tFROM _user m\n" +
            "\t\t\t\t\t\t\tWHERE m.department_id = u.department_id\n" +
            "\t\t\t\t\t\t\tAND m.position_id = (SELECT MIN(c.position_id)\t\n" +
            "\t\t\t\t\t\t\t\t\t\t\t\t\t\tFROM _user c\n" +
            "\t\t\t\t\t\t\t\t\t\t\t\t\t\t\tWHERE c.department_id = u.department_id\n" +
            "\t\t\t\t\t\t\t\t\t\t\t\t\t\t\tAND c.use_status = 1)\n" +
            "\t\t\t\t\t\t\tAND m.use_status = 1\n" +
            "\t\t\t\t\t)\n" +
            "\t\t\t\t\tTHEN (\n" +
            "\t\t\t\t\t\tCASE\n" +
            "\t\t\t\t\t\t\tWHEN (\n" +
            "\t\t\t\t\t\t\t\tSELECT MAX(m.id)\n" +
            "\t\t\t\t\t\t\t\t\tFROM _user m\n" +
            "\t\t\t\t\t\t\t\t\tWHERE m.department_id = u.department_id\n" +
            "\t\t\t\t\t\t\t\t\tAND m.position_id = (SELECT MIN(c.position_id)\n" +
            "\t\t\t\t\t\t\t\t\t\t\t\t\t\t\tFROM _user c\n" +
            "\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\tWHERE c.department_id = u.department_id\n" +
            "\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\tAND c.use_status = 1)\n" +
            "\t\t\t\t\t\t\t\t\tAND m.id < u.id\n" +
            "\t\t\t\t\t\t\t\t\tAND m.use_status = 1\n" +
            "\t\t\t\t\t\t\t\t) IS NULL\n" +
            "\t\t\t\t\t\t\t\tTHEN (\n" +
            "\t\t\t\t\t\t\t\t\tSELECT MAX(m.id)\n" +
            "\t\t\t\t\t\t\t\t\t\tFROM _user m\n" +
            "\t\t\t\t\t\t\t\t\t\tWHERE m.department_id = u.department_id\n" +
            "\t\t\t\t\t\t\t\t\t\tAND m.position_id = (SELECT MIN(c.position_id)\n" +
            "\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\tFROM _user c\n" +
            "\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\tWHERE c.department_id = u.department_id\n" +
            "\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\tAND c.use_status = 1)\n" +
            "\t\t\t\t\t\t\t\t\t\tAND m.id >u.id\n" +
            "\t\t\t\t\t\t\t\t\t\tAND m.use_status = 1\n" +
            "\t\t\t\t\t\t\t\t)\n" +
            "\t\t\t\t\t\t\tELSE (\n" +
            "\t\t\t\t\t\t\t\tCASE\n" +
            "\t\t\t\t\t\t\t\t\tWHEN (\n" +
            "\t\t\t\t\t\t\t\t\t\tSELECT MAX(m.id)\n" +
            "\t\t\t\t\t\t\t\t\t\t\tFROM _user m\n" +
            "\t\t\t\t\t\t\t\t\t\t\tWHERE m.department_id = u.department_id\n" +
            "\t\t\t\t\t\t\t\t\t\t\tAND m.position_id = p.parent_position_id\n" +
            "\t\t\t\t\t\t\t\t\t\t\tAND m.id < u.id\n" +
            "\t\t\t\t\t\t\t\t\t\t\tAND m.use_status = 1\n" +
            "\t\t\t\t\t\t\t\t\t\t\t) IS NULL\n" +
            "\t\t\t\t\t\t\t\t\t\t\tTHEN\n" +
            "\t\t\t\t\t\t\t\t\t\t\t\t(\n" +
            "\t\t\t\t\t\t\t\t\t\t\t\t\tCASE\n" +
            "\t\t\t\t\t\t\t\t\t\t\t\t\t\tWHEN\n" +
            "\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t( SELECT COUNT(*)\n" +
            "\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\tFROM (\n" +
            "\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\tSELECT rr.position_id\n" +
            "\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\tFROM _user rr\n" +
            "\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\tWHERE rr.department_id = u.department_id\n" +
            "\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\tAND rr.use_status = 1\n" +
            "\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\tGROUP BY rr.position_id\n" +
            "\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t) rs\n" +
            "\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t) = 1\n" +
            "\t\t\t\t\t\t\t\t\t\t\t\t\t\tTHEN (\t\n" +
            "\t\t\t\t\t\t\t\t\t\t\t\t\t\t\tSELECT MAX(m.id)\n" +
            "\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\tFROM _user m\n" +
            "\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\tWHERE m.department_id = u.department_id\n" +
            "\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\tAND m.position_id = (SELECT MIN(c.position_id)\n" +
            "\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\tFROM _user c\n" +
            "\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\tWHERE c.department_id = u.department_id\n" +
            "\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\tAND c.use_status = 1)\n" +
            "\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\tAND m.id < u.id\n" +
            "\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\tAND m.use_status = 1\n" +
            "\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t)\n" +
            "\t\t\t\t\t\t\t\t\t\t\t\t\t\tELSE (\n" +
            "\t\t\t\t\t\t\t\t\t\t\t\t\t\t\tSELECT MAX(m.id)\n" +
            "\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\tFROM _user m\n" +
            "\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\tWHERE m.department_id = u.department_id\n" +
            "\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\tAND m.position_id = (SELECT MAX(c.position_id)\n" +
            "\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\tFROM _user c\n" +
            "\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\tWHERE c.department_id = u.department_id\n" +
            "\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\tAND c.position_id NOT LIKE u.position_id\n" +
            "\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\tAND c.use_status = 1)\n" +
            "\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\tAND m.id != u.id\n" +
            "\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\tAND m.use_status = 1\n" +
            "\t\t\t\t\t\t\t\t\t\t\t\t\t\t) END\n" +
            "\t\t\t\t\t\t\t\t\t\t\t\t)\n" +
            "\t\t\t\t\t\t\t\t\t\t\tELSE (\n" +
            "\t\t\t\t\t\t\t\t\t\t\t \tSELECT MAX(m.id)\n" +
            "\t\t\t\t\t\t\t\t\t\t\t\t\tFROM _user m\n" +
            "\t\t\t\t\t\t\t\t\t\t\t\t\tWHERE m.department_id = u.department_id\n" +
            "\t\t\t\t\t\t\t\t\t\t\t\t\tAND m.position_id = p.parent_position_id\n" +
            "\t\t\t\t\t\t\t\t\t\t\t\t\tAND m.id != u.id\n" +
            "\t\t\t\t\t\t\t\t\t\t\t\t\tAND m.use_status = 1\n" +
            "\t\t\t\t\t\t\t\t\t\t\t) END\n" +
            "\t\t\t\t\t\t\t) END\n" +
            "\t\t\t\t\t\t)\n" +
            "\t\t\t\t\tWHEN (\n" +
            "\t\t\t\t\t\t\tSELECT MAX(z.id)\n" +
            "\t\t\t\t\t\t\t\tFROM _user z\n" +
            "\t\t\t\t\t\t\t\tWHERE z.department_id = (\n" +
            "\t\t\t\t\t\t\t\t\t\t\tSELECT p.department_parent_id\n" +
            "\t\t\t\t\t\t\t\t\t\t\t\tFROM _department p\n" +
            "\t\t\t\t\t\t\t\t\t\t\t\tWHERE p.id = u.department_id\n" +
            "\t\t\t\t\t\t\t\t\t\t\t)\n" +
            "\t\t\t\t\t\t\t\t\tAND z.position_id = (\n" +
            "\t\t\t\t\t\t\t\t\t\tSELECT max(r.position_id)\n" +
            "\t\t\t\t\t\t\t\t\t\t\tFROM _user r\n" +
            "\t\t\t\t\t\t\t\t\t\t\tWHERE r.department_id = (\n" +
            "\t\t\t\t\t\t\t\t\t\t\t\t\t\tSELECT y.department_parent_id\n" +
            "\t\t\t\t\t\t\t\t\t\t\t\t\t\t\tFROM _department y\n" +
            "\t\t\t\t\t\t\t\t\t\t\t\t\t\t\tWHERE y.id = u.department_id\n" +
            "\t\t\t\t\t\t\t\t\t\t\t\t\t\t)\n" +
            "\t\t\t\t\t\t\t\t\t\t\tAND r.use_status = 1\n" +
            "\t\t\t\t\t\t\t\t\t)\n" +
            "\t\t\t\t\t\t\t\t\tAND z.use_status = 1\n" +
            "\t\t\t\t\t\t) IS NULL\n" +
            "\t\t\t\t\t\tTHEN (0)\n" +
            "\t\t\t\t\tELSE\n" +
            "\t\t\t\t\t\t(\n" +
            "\t\t\t\t\t\t\tSELECT MAX(z.id)\n" +
            "\t\t\t\t\t\t\t\tFROM _user z\n" +
            "\t\t\t\t\t\t\t\tWHERE z.department_id = (\n" +
            "\t\t\t\t\t\t\t\t\t\t\tSELECT p.department_parent_id\n" +
            "\t\t\t\t\t\t\t\t\t\t\t\tFROM _department p\n" +
            "\t\t\t\t\t\t\t\t\t\t\t\tWHERE p.id = u.department_id\n" +
            "\t\t\t\t\t\t\t\t\t\t\t)\n" +
            "\t\t\t\t\t\t\t\t\tAND z.position_id = (\n" +
            "\t\t\t\t\t\t\t\t\t\tSELECT max(r.position_id)\n" +
            "\t\t\t\t\t\t\t\t\t\t\tFROM _user r\n" +
            "\t\t\t\t\t\t\t\t\t\t\tWHERE r.department_id = (\n" +
            "\t\t\t\t\t\t\t\t\t\t\t\t\t\tSELECT y.department_parent_id\n" +
            "\t\t\t\t\t\t\t\t\t\t\t\t\t\t\tFROM _department y\n" +
            "\t\t\t\t\t\t\t\t\t\t\t\t\t\t\tWHERE y.id = u.department_id\n" +
            "\t\t\t\t\t\t\t\t\t\t\t\t\t\t)\n" +
            "\t\t\t\t\t\t\t\t\t\t\tAND r.use_status = 1\n" +
            "\t\t\t\t\t\t\t\t\t) AND z.use_status = 1\n" +
            "\t\t\t\t\t\t)\n" +
            "\t\t\t\t\tEND as departmentParentId\n" +
            "\t\t, CONCAT(d.department_name, '-', p.position_name) as title\n" +
            "\t\t, u.department_id as departmentId\n" +
            "\t\t, u.position_id as positionId\n" +
            "\tFROM _user u\n" +
            "\tLEFT JOIN _position p ON u.position_id = p.id\n" +
            "\tLEFT JOIN _department d ON  u.department_id = d.id\n" +
            "\tWHERE u.use_status = 1", nativeQuery = true)
    List<Map<String, Object>> empStructure();

    @Query(value = "SELECT u.id, u.username as name, u.parent_user_id as departmentParentId, CONCAT(d.department_name , '-', p.position_name) as title\n" +
            "\tFROM _user u\n" +
            "\tLEFT JOIN _department d ON u.department_id = d.id\n" +
            "\tLEFT JOIN _position p ON u.position_id = p.id\n" +
            "\tWHERE u.use_status = 1", nativeQuery = true)
    List<Map<String, Object>> empStructureCore();

    @Query(value = "WITH RecursiveCTE AS (\n" +
            "        SELECT \n" +
            "            id,\n" +
            "            department_name,\n" +
            "            use_status,\n" +
            "            department_parent_id\n" +
            "        FROM \n" +
            "            [dbo].[_department]\n" +
            "        WHERE \n" +
            "            id IN (SELECT id FROM _department WHERE department_parent_id is null AND use_status = 1)\n" +
            "            AND use_status = 1\n" +
            "        UNION ALL\n" +
            "        SELECT \n" +
            "            d.id,\n" +
            "            d.department_name,\n" +
            "            d.use_status,\n" +
            "            d.department_parent_id\n" +
            "        FROM \n" +
            "            [dbo].[_department] d\n" +
            "        INNER JOIN \n" +
            "            RecursiveCTE r ON d.department_parent_id = r.id\n" +
            "        WHERE\n" +
            "        \td.use_status = 1\n" +
            "    )\n" +
            "   \n" +
            "    SELECT \n" +
            "        a.id,\n" +
            "        a.department_name AS departmentName,\n" +
            "        a.department_parent_id as departmentParentId,\n" +
            "        a.department_name as userName\n" +
            "    FROM \n" +
            "        RecursiveCTE a", nativeQuery = true)
    List<Map<String, Object>> deptMap();

}
