package com.groupware.controller.api;

import com.groupware.common.CommonResponse;
import com.groupware.common.ResponseCode;
import com.groupware.dto.request.EmployeeEditRequest;
import com.groupware.service.UserService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@CrossOrigin(origins = "*", maxAge = 3600)
@RequestMapping("/api/v1/admin/employee")
public class EmployeeController {
    @Autowired
    private UserService userService;
    @PatchMapping("/{id}")
    public CommonResponse<?> detailEmployee(@PathVariable int id)
    {
        return CommonResponse.success(userService.detailUser(id));
    }

    @GetMapping
    public CommonResponse<?> lstEmployee(){
        return CommonResponse.success(userService.lstUser());
    }

    @PutMapping
    public CommonResponse<?> editEmployee(@Valid @RequestBody EmployeeEditRequest request, BindingResult result){
        if (result.hasErrors()) {
            Map<String, String> errors = new HashMap<>();

            for (FieldError error : result.getFieldErrors()) {
                errors.put(error.getField(), error.getDefaultMessage());
            }
            return CommonResponse.of(HttpStatus.BAD_REQUEST, ResponseCode.FAILED, "FAILED", errors);
        }
        return CommonResponse.success(userService.editEmployee(request));
    }
    @DeleteMapping
    public CommonResponse<?> deleteEmployee(@RequestParam(required = false, defaultValue = "0") int id){
        userService.deleteEmployee(id);
        return CommonResponse.success("SUCCESS");
    }
}
