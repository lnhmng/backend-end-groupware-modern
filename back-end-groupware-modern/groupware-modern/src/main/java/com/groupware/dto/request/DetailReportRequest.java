package com.groupware.dto.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class DetailReportRequest {
    private String workDetail;
    private Integer workStatusId;
    private List<String> fileNames;
}
