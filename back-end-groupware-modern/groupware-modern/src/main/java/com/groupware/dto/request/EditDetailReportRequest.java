package com.groupware.dto.request;

import com.groupware.entity.document.DocumentPath;
import lombok.Data;

import java.util.List;

@Data
public class EditDetailReportRequest {
    private Integer id;
    private String workDetail;
    private Integer workStatusId;
    private List<DocumentPath> documentPaths;
    private List<String> fileNames;
}
