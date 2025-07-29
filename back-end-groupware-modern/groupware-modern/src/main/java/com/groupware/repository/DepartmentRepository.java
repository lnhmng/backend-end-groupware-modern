package com.groupware.repository;

import com.groupware.entity.department.Department;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.query.Procedure;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Set;

public interface DepartmentRepository extends JpaRepository<Department, Integer> {

    Boolean existsByDepartmentName(String departmentName);
    @Query(value = "SELECT *\n" +
            "\tFROM _department d\n" +
            "\tWHERE d.use_status = 1", nativeQuery = true)
    List<Department> lstDepartment();

    @Query(value = "SELECT count(*)\n" +
            "\tFROM _department d\n" +
            "\tWHERE d.department_name = :departmentName\n" +
            "\tAND d.id != :id", nativeQuery = true)
    int exitsDepartmentNameEdit(@Param("departmentName") String departmentName, @Param("id") int id);

    @Procedure(name = "GetDepartmentAndChildren")
    Set<Department> getDepartmentAndChildren(@Param("inputDepartmentParam") int inputDepartmentParam);

    List<Department> findByDepartmentIsNull();
}
