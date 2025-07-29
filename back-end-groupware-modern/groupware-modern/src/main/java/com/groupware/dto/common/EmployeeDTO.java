package com.groupware.dto.common;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class EmployeeDTO {
    private int id;
    private int departmentId;
    private int positionId;
    private String name;
    private String title;
    private int departmentParentId;
}
