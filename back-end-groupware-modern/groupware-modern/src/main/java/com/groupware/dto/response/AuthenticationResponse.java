package com.groupware.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.groupware.entity.department.Department;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.Map;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AuthenticationResponse {

  @JsonProperty("accessToken")
  private String accessToken;
  @JsonProperty("refreshToken")
  private String refreshToken;
  private String username;
  private Integer id;
  private String email;
  private List<String> roles;
  private Map<String, List<String>> menuPermission;
  private String tokenType;
  private Department department;
}
