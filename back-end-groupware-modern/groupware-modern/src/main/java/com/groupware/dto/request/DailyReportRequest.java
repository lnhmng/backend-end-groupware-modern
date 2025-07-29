package com.groupware.dto.request;

import jakarta.validation.constraints.NotEmpty;
import lombok.*;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class DailyReportRequest {
    @NotEmpty
    private String title;
    @NotEmpty
    private String dateReport;
    private List<DetailReportRequest> detailReportRequests;
}
