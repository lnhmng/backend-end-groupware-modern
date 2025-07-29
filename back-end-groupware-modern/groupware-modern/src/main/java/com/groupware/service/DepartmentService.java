package com.groupware.service;

import com.groupware.entity.department.Department;

import java.security.Principal;
import java.util.List;
import java.util.Set;

public interface DepartmentService {
    List<Department> lstDepartment();

    Department createDepartment(String departmentName);

    Department detailDepartment(int id);

    Department editDepartment(String departmentName, int id);

    Department deleteDepartment(int id);

    Set<Department> getDepartmentWithPermission(Principal principal);
}
