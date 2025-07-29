package com.groupware.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class EmployeeEditRequest {
    @NotNull(message = "id is required")
    private int id;
    @NotBlank(message = "First name is required")
    private String firstname;
    @NotBlank(message = "Last name is required")
    private String lastname;
    @NotNull(message = "Position is required")
    private int positionId;
    @NotNull(message = "Department is required")
    private int departmentId;
    @NotNull(message = "Active is required")
    private boolean active;
    @NotNull(message = "Use status is required")
    private boolean useStatus;
    @NotNull(message = "permissionRoute is required")
    private int permissionRoute;
}
