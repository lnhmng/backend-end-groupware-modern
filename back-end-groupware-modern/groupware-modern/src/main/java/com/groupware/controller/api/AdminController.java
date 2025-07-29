package com.groupware.controller.api;

import com.groupware.common.CommonResponse;
import com.groupware.common.ResponseCode;
import com.groupware.dto.request.RegisterRequest;
import com.groupware.service.impl.AuthenticationService;
import io.swagger.v3.oas.annotations.Hidden;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/v1/admin")
public class AdminController {

    @Autowired
    private AuthenticationService service;

    @PostMapping("/register")
    public CommonResponse<?> register(
            @Valid @RequestBody RegisterRequest request, BindingResult result
    ) {
        if (result.hasErrors()){
            return CommonResponse.of(HttpStatus.BAD_REQUEST, ResponseCode.FAILED, "Invalid email", "Invalid email");
        }
        return CommonResponse.success(service.register(request));
    }
    @GetMapping
    @PreAuthorize("hasAuthority('admin:read')")
    public String get() {
        return "GET:: admin controller";
    }
    @PostMapping
    @PreAuthorize("hasAuthority('admin:create')")
    @Hidden
    public String post() {
        return "POST:: admin controller";
    }
    @PutMapping
    @PreAuthorize("hasAuthority('admin:update')")
    @Hidden
    public String put() {
        return "PUT:: admin controller";
    }
    @DeleteMapping
    @PreAuthorize("hasAuthority('admin:delete')")
    @Hidden
    public String delete() {
        return "DELETE:: admin controller";
    }
}
