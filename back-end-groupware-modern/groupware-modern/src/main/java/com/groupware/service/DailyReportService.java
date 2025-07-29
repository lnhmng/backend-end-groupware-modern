package com.groupware.service;

import com.groupware.dto.request.DailyReportRequest;
import com.groupware.dto.request.EditDailyReportRequest;
import com.groupware.entity.work.DailyReport;
import com.groupware.entity.work.DetailReport;
import org.springframework.web.multipart.MultipartFile;

import java.security.Principal;
import java.util.List;
import java.util.Map;

public interface DailyReportService {
    DailyReport createDailyReport(DailyReportRequest dailyReportRequest, Principal principal);

    DailyReport detailDailyReport(int id);

    DailyReport updateDailyReport(EditDailyReportRequest editDailyReportRequest, Principal principal);

    List<Map<String, Object>> lstDailyReport(String dataDateSearch, int departmentSelectModel);

    DailyReport updateTitleDailyReport(int id, String title, String date, Principal principal);

    DetailReport updateDetailReport(int id, String detail, int statusId, List<MultipartFile> files, Principal principal);

    DailyReport deleteDailyReport(int id, Principal principal);

    int deleteDocument(int documentId, Principal principal);

    DetailReport getDetailReport(int detailReportId, Principal principal);

    DailyReport updateDetailWork(DailyReportRequest dailyReportRequest, Principal principal);

    DailyReport deleteDetailWork(int dailyReportId, int detailReportId, Principal principal);

    void permissionCheck(String username, Principal principal);
}
