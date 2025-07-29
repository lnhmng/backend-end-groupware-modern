package com.groupware.controller.api;

import com.groupware.common.CommonResponse;
import com.groupware.service.DepartmentService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

@AllArgsConstructor
@RestController
@RequestMapping("/api/v1/admin/department")
public class DepartmentController {
    final DepartmentService departmentService;

    @GetMapping
    public CommonResponse<?> lstDepartment() {
        return CommonResponse.success(departmentService.lstDepartment());
    }

    @PostMapping
    public CommonResponse<?> createDepartment(@RequestParam(defaultValue = "", required = false) String departmentName) {
        return CommonResponse.success(departmentService.createDepartment(departmentName));
    }

    @PatchMapping("/{id}")
    public CommonResponse<?> detailDepartment(@PathVariable int id) {
        return CommonResponse.success(departmentService.detailDepartment(id));
    }

    @PutMapping()
    public CommonResponse<?> editDepartment(@RequestParam(defaultValue = "", required = false) String departmentName
            , @RequestParam(defaultValue = "", required = false) int id) {
        return CommonResponse.success(departmentService.editDepartment(departmentName, id));
    }

    @DeleteMapping
    public CommonResponse<?> deleteDepartment(@RequestParam(defaultValue = "", required = false) int id) {
        return CommonResponse.success(departmentService.deleteDepartment(id));
    }
}
