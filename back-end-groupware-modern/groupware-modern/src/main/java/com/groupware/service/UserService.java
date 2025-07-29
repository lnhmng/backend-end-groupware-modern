package com.groupware.service;

import com.groupware.common.CommonResponse;
import com.groupware.dto.request.EmployeeEditRequest;
import com.groupware.dto.response.UserDetailResponse;
import com.groupware.entity.user.User;

import java.security.Principal;
import java.util.List;
import java.util.Map;

public interface UserService {
    UserDetailResponse detailUser(Integer id);
    List<Map<String, Object>> lstUser();

    User editEmployee(EmployeeEditRequest request);

    void deleteEmployee(int id);

    List<Map<String, Object>> empStructure();

    CommonResponse<?> changePassword(String currentPassword, String newPassword, Principal principal);

}
