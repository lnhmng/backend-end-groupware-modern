package com.groupware.service.impl;

import com.groupware.entity.department.Department;
import com.groupware.entity.user.User;
import com.groupware.exception.CommonException;
import com.groupware.repository.DepartmentRepository;
import com.groupware.repository.UserRepository;
import com.groupware.service.DepartmentService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.security.Principal;
import java.util.List;
import java.util.Set;

@Slf4j
@AllArgsConstructor
@Service
public class DepartmentServiceImpl implements DepartmentService {
    final DepartmentRepository departmentRepository;
    final UserRepository userRepository;

    @Override
    public List<Department> lstDepartment() {
        try {
            return departmentRepository.lstDepartment();
        } catch (Exception e) {
            log.error("### Error lstDepartment: " + e.getMessage());
            throw CommonException.of("Error: {}", e.getMessage());
        }
    }

    @Override
    public Department createDepartment(String departmentName) {
        try {
            if (departmentName.length() < 2) {
                throw new RuntimeException("Department name not null and min is 3 char");
            }
            if (departmentRepository.existsByDepartmentName(departmentName)) {
                throw new RuntimeException("Department name has been duplicated");
            }
            Department department = new Department();
            department.setDepartmentName(departmentName);
            department.setUseStatus(true);
            return departmentRepository.save(department);
        } catch (Exception e) {
            log.error("### Error createDepartment: " + e.getMessage());
            throw CommonException.of("Error: {}", e.getMessage());
        }
    }

    @Override
    public Department detailDepartment(int id) {
        try {
            return departmentRepository.findById(id)
                    .orElseThrow(() -> new RuntimeException("Not found id Department"));
        } catch (Exception e) {
            log.error("### Error detailDepartment: " + e.getMessage());
            throw CommonException.of("Error: {}", e.getMessage());
        }
    }

    @Override
    public Department editDepartment(String departmentName, int id) {
        try {
            Department department = departmentRepository.findById(id)
                    .orElseThrow(() -> new RuntimeException("Not found id Department"));
            if (departmentRepository.exitsDepartmentNameEdit(departmentName, id) != 0) {
                throw CommonException.of("Department name has been duplicated");
            }
            department.setDepartmentName(departmentName);
            return departmentRepository.save(department);
        } catch (Exception e) {
            log.error("### Error editDepartment: " + e.getMessage());
            throw CommonException.of("Error: {}", e.getMessage());
        }
    }

    @Override
    public Department deleteDepartment(int id) {
        try {
            Department department = departmentRepository.findById(id)
                    .orElseThrow(() -> new RuntimeException("not found Department id"));
            department.setUseStatus(false);
            return departmentRepository.save(department);
        } catch (Exception e){
            log.error("### Error deleteDepartment: " + e.getMessage());
            throw CommonException.of("Error: {}", e.getMessage());
        }
    }

    @Transactional
    @Override
    public Set<Department> getDepartmentWithPermission(Principal principal) {
        try {
            User user = userRepository.findByUsername(principal.getName())
                    .orElseThrow(()-> new RuntimeException("you don have permission"));
            return departmentRepository.getDepartmentAndChildren(user.getDepartment().getId());
        } catch (Exception e){
            log.error("### Error getDepartmentWithPermission " + e.getMessage());
            throw CommonException.of("Error: {}", e.getMessage());
        }
    }


}
