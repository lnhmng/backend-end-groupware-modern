package com.groupware.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Set;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class RegisterRequest {
  private String username;
  private String firstname;
  private String lastname;

  private int positionId;
  private int departmentId;

  @NotBlank
  @Email
  private String email;

  private Set<String> role;
  private Integer permissionRoute;
}
