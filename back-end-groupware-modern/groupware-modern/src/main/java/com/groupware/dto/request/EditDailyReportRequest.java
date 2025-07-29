package com.groupware.dto.request;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.NonNull;

import java.util.Date;
import java.util.List;

@Data
public class EditDailyReportRequest {
    private Integer id;

    @NotEmpty
    @Size(min = 6, message = "Minimum 6 chars required")
    private String title;

    @NonNull
    private Date dateReport;

    private List<EditDetailReportRequest> editDetailReportRequests;
}
