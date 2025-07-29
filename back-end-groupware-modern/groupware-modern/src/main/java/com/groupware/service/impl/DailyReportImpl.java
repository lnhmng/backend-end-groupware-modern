package com.groupware.service.impl;

import com.groupware.dto.request.DailyReportRequest;
import com.groupware.dto.request.DetailReportRequest;
import com.groupware.dto.request.EditDailyReportRequest;
import com.groupware.dto.request.EditDetailReportRequest;
import com.groupware.entity.department.Department;
import com.groupware.entity.document.DocumentPath;
import com.groupware.entity.user.User;
import com.groupware.entity.work.DailyReport;
import com.groupware.entity.work.DetailReport;
import com.groupware.entity.work.WorkStatus;
import com.groupware.exception.CommonException;
import com.groupware.repository.*;
import com.groupware.service.DailyReportService;
import com.groupware.service.MediaService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.security.Principal;
import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.*;

@Slf4j
@Service
@AllArgsConstructor
public class DailyReportImpl implements DailyReportService {
    final WorkStatusRepository workStatusRepository;
    final DocumentPartRepository documentPartRepository;
    final UserRepository userRepository;
    final DetailReportRepository detailReportRepository;
    final DailyReportRepository dailyReportRepository;
    final MediaService mediaService;
    final DepartmentRepository departmentRepository;

    @Override
    public DailyReport createDailyReport(DailyReportRequest dailyReportRequest, Principal principal) {
        try {
            User user = userRepository.findByUsername(principal.getName())
                    .orElseThrow(() -> new RuntimeException("not found username"));
            LocalDateTime now = LocalDateTime.now();
            LocalDateTime oneMinuteAgo = now.minusSeconds(5);
            Date date = Timestamp.valueOf(oneMinuteAgo);
            //save detailReport
            Set<DetailReport> detailReports = new HashSet<>();
            for (DetailReportRequest detailReportRequest : dailyReportRequest.getDetailReportRequests()) {
                DetailReport detailReport = new DetailReport();
                detailReport.setWorkDetail(detailReportRequest.getWorkDetail());
                detailReport.setWorkStatus(workStatusRepository.findById(detailReportRequest.getWorkStatusId())
                        .orElseThrow(() -> new RuntimeException("Not found work status id")));

                Set<DocumentPath> documentPaths = new HashSet<>();
                if (detailReportRequest.getFileNames() != null) {
                    for (String documentName : detailReportRequest.getFileNames()) {
                        DocumentPath documentPath = documentPartRepository.documentPath(documentName, date, user.getId());
                        documentPaths.add(documentPath);
                    }
                }
                detailReport.setDocumentPaths(documentPaths);
                detailReports.add(detailReport);
            }
            List<DetailReport> rsDetailReport = detailReportRepository.saveAll(detailReports);
            //end

            //start save dailyReport
            DailyReport dailyReport = getDailyReport(dailyReportRequest, user, rsDetailReport);
            //end save dailyReport

            return dailyReportRepository.save(dailyReport);
        } catch (Exception e) {
            log.error("### Error createDailyReport: " + e.getMessage());
            throw CommonException.of("Error: {}", e.getMessage());
        }
    }

    private static DailyReport getDailyReport(DailyReportRequest dailyReportRequest, User user, List<DetailReport> rsDetailReport) throws ParseException {
        DailyReport dailyReport = new DailyReport();
        dailyReport.setUser(user);
        dailyReport.setPosition(user.getPosition());
        dailyReport.setDepartment(user.getDepartment());
        dailyReport.setTitle(dailyReportRequest.getTitle());
        Set<DetailReport> paramDetailReport = new HashSet<>(rsDetailReport);
        dailyReport.setDetailReports(paramDetailReport);
        dailyReport.setUseStatus(true);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Date date = sdf.parse(dailyReportRequest.getDateReport());
        dailyReport.setDateReport(date);
        return dailyReport;
    }

    @Override
    public DailyReport detailDailyReport(int id) {
        try {
            return dailyReportRepository.findById(id)
                    .orElseThrow(() -> new RuntimeException("not found id"));
        } catch (Exception e) {
            log.error("### Error: " + e.getMessage());
            throw CommonException.of("Error: {}", e.getMessage());
        }
    }

    @Override
    public DailyReport updateDailyReport(EditDailyReportRequest editDailyReportRequest, Principal principal) {
        try {
            User user = userRepository.findByUsername(principal.getName())
                    .orElseThrow(() -> new RuntimeException("not found username"));
            LocalDateTime now = LocalDateTime.now();
            LocalDateTime oneMinuteAgo = now.minusSeconds(10);
            Date date = Timestamp.valueOf(oneMinuteAgo);
            // update detailReport
            List<DetailReport> detailReports = new ArrayList<>();
            for (EditDetailReportRequest editDetailReportRequest : editDailyReportRequest.getEditDetailReportRequests()) {
                DetailReport detailReport = new DetailReport();
                if (editDetailReportRequest.getId() != null) {
                    detailReport = detailReportRepository.findById(editDetailReportRequest.getId())
                            .orElseThrow(() -> new RuntimeException("Not found id"));
                }
                detailReport.setWorkDetail(editDetailReportRequest.getWorkDetail());
                detailReport.setWorkStatus(workStatusRepository.findById(editDetailReportRequest.getWorkStatusId())
                        .orElseThrow(() -> new RuntimeException("not found status id")));
                Set<DocumentPath> documentPaths = new HashSet<>(editDetailReportRequest.getDocumentPaths());

                if (editDetailReportRequest.getFileNames() != null) {
                    for (String documentName : editDetailReportRequest.getFileNames()) {
                        DocumentPath documentPath = documentPartRepository.documentPath(documentName, date, user.getId());
                        documentPaths.add(documentPath);
                    }
                }
                detailReport.setUserUpdated(user);
                detailReport.setDocumentPaths(documentPaths);
            }
            List<DetailReport> rsDetailReport = detailReportRepository.saveAll(detailReports);
            //end
            //update dailyReport
            DailyReport dailyReport = dailyReportRepository.findById(editDailyReportRequest.getId())
                    .orElseThrow(() -> new RuntimeException("not found id"));
            dailyReport.setTitle(editDailyReportRequest.getTitle());
            dailyReport.setDateReport(editDailyReportRequest.getDateReport());
            dailyReport.setUpdatedBy(user.getId());
            Set<DetailReport> detailReportSet = new HashSet<>(rsDetailReport);
            dailyReport.setDetailReports(detailReportSet);
            return null;
        } catch (Exception e) {
            log.error("### Error: " + e.getMessage());
            throw CommonException.of("Error: {}", e.getMessage());
        }
    }

    @Transactional
    @Override
    public List<Map<String, Object>> lstDailyReport(String dataDateSearch, int departmentSelectModel) {
        try {
            Set<Department> departments = departmentRepository.getDepartmentAndChildren(departmentSelectModel);
            List<Integer> lst = new ArrayList<>();
            for (Department department: departments){
                lst.add(department.getId());
            }
            return dailyReportRepository.lstDailyReport(dataDateSearch, lst);
        } catch (Exception e) {
            log.error("### Error: " + e.getMessage());
            throw CommonException.of("Error: {}", e.getMessage());
        }
    }

    @Override
    public DailyReport updateTitleDailyReport(int id, String title, String strDate, Principal principal) {
        try {
            DailyReport dailyReport = dailyReportRepository.findById(id)
                    .orElseThrow(() -> new RuntimeException("not found id"));
            if (title.length() < 6) {
                throw new RuntimeException("Title minimum 6 chars required");
            }
            if (!Objects.equals(dailyReport.getUser().getUsername(), principal.getName())) {
                throw new AccessDeniedException("You do not have permission to access this resource");
            }
            dailyReport.setTitle(title);
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            Date date = sdf.parse(strDate);
            dailyReport.setDateReport(date);
            return dailyReportRepository.save(dailyReport);
        } catch (Exception e) {
            log.error("### Error: updateTitleDailyReport " + e.getMessage());
            throw CommonException.of("Error: {}", e.getMessage());
        }
    }

    @Transactional
    @Override
    public DetailReport updateDetailReport(int id, String detail, int statusId, List<MultipartFile> files, Principal principal) {
        try {
            DetailReport detailReport = detailReportRepository.findById(id)
                    .orElseThrow(() -> new RuntimeException("not found id"));
            if (detail.length() < 5) {
                throw new RuntimeException("detail report minimum 6 chars required");
            }
            DailyReport dailyReport = dailyReportRepository.dailyReport(id)
                    .orElseThrow(() -> new RuntimeException("Not found id daily report"));
            if (!Objects.equals(dailyReport.getUser().getUsername(), principal.getName())) {
                throw new AccessDeniedException("You do not have permission to access this resource");
            }
            WorkStatus workStatus = workStatusRepository.findById(statusId)
                    .orElseThrow(() -> new RuntimeException("not found work status id"));
            detailReport.setWorkDetail(detail);
            detailReport.setWorkStatus(workStatus);
            DetailReport dataDetailReport = detailReportRepository.save(detailReport);
            if (files != null) {
                mediaService.updateDocumentPath(id, files, principal);
            }

            return dataDetailReport;
        } catch (Exception e) {
            log.error("### Error: updateDetailReport error: " + e.getMessage());
            throw CommonException.of("Error: {} ", e.getMessage());
        }
    }

    @Override
    public DailyReport deleteDailyReport(int id, Principal principal) {
        try {
            DailyReport dailyReport = dailyReportRepository.findById(id)
                    .orElseThrow(() -> new RuntimeException("not found daily report id"));
            if (!Objects.equals(dailyReport.getUser().getUsername(), principal.getName())) {
                throw new AccessDeniedException("You do not have permission to access this resource");
            }
            dailyReport.setUseStatus(false);
            return dailyReportRepository.save(dailyReport);
        } catch (Exception e) {
            log.error("### Error: deleteDailyReport: " + e.getMessage());
            throw CommonException.of("Error: {}", e.getMessage());
        }
    }

    @Transactional
    @Override
    public int deleteDocument(int documentId, Principal principal) {
        try {
            DetailReport detailReport = detailReportRepository.getDetailReport(documentId);
            Set<DocumentPath> documentPaths = detailReport.getDocumentPaths();
            DocumentPath targetDocumentPath = documentPartRepository.findById(documentId)
                    .orElseThrow(() -> new RuntimeException("Not found Document id"));
            DailyReport dailyReport = dailyReportRepository.dailyReport(detailReport.getId())
                            .orElseThrow(()-> new RuntimeException("Not found daily report"));
            permissionCheck(dailyReport.getUser().getUsername(), principal);
            documentPaths.remove(targetDocumentPath);
            detailReport.setDocumentPaths(documentPaths);
            detailReportRepository.save(detailReport);
            mediaService.deleteFileDocument(targetDocumentPath.getDocumentHashName());
            documentPartRepository.delete(targetDocumentPath);
            return detailReport.getId();
        } catch (Exception e) {
            log.error("### Error: getDetailReport " + e.getMessage());
            throw CommonException.of("Error: {}", e.getMessage());
        }
    }

    @Override
    public DetailReport getDetailReport(int detailReportId, Principal principal) {
        return detailReportRepository.findById(detailReportId)
                .orElseThrow(() -> new RuntimeException("not found detail report"));
    }

    @Override
    public void permissionCheck(String username, Principal principal) {
        if (!Objects.equals(username, principal.getName())) {
            throw new AccessDeniedException("You do not have permission to access this resource");
        }
    }

    @Transactional
    @Override
    public DailyReport updateDetailWork(DailyReportRequest dailyReportRequest, Principal principal) {
        try {
            User user = userRepository.findByUsername(principal.getName())
                    .orElseThrow(() -> new RuntimeException("not found username"));
            LocalDateTime now = LocalDateTime.now();
            LocalDateTime oneMinuteAgo = now.minusSeconds(5);
            Date date = Timestamp.valueOf(oneMinuteAgo);
            int dailyReportId = Integer.parseInt(dailyReportRequest.getTitle());
            DailyReport dailyReport = dailyReportRepository.findById(dailyReportId)
                    .orElseThrow(() -> new RuntimeException("not found daily report"));

            permissionCheck(dailyReport.getUser().getUsername(), principal);

            Set<DetailReport> detailReports = new HashSet<>();
            for (DetailReportRequest detailReportRequest : dailyReportRequest.getDetailReportRequests()) {
                DetailReport detailReport = new DetailReport();
                detailReport.setWorkDetail(detailReportRequest.getWorkDetail());
                detailReport.setWorkStatus(workStatusRepository.findById(detailReportRequest.getWorkStatusId())
                        .orElseThrow(() -> new RuntimeException("Not found work status id")));

                Set<DocumentPath> documentPaths = new HashSet<>();
                if (detailReportRequest.getFileNames() != null) {
                    for (String documentName : detailReportRequest.getFileNames()) {
                        DocumentPath documentPath = documentPartRepository.documentPath(documentName, date, user.getId());
                        documentPaths.add(documentPath);
                    }
                }
                detailReport.setDocumentPaths(documentPaths);
                detailReports.add(detailReport);
            }
            List<DetailReport> rsDetailReport = detailReportRepository.saveAll(detailReports);
            Set<DetailReport> setDetailReports = new HashSet<>(rsDetailReport);
            setDetailReports.addAll(dailyReport.getDetailReports());
            dailyReport.setDetailReports(setDetailReports);
            return dailyReportRepository.save(dailyReport);
        } catch (Exception e) {
            log.error("### Error: updateDetailWork " + e.getMessage());
            throw CommonException.of("Error: {}", e.getMessage());
        }
    }

    @Transactional
    @Override
    public DailyReport deleteDetailWork(int dailyReportId, int detailReportId, Principal principal) {
        try {
            DailyReport dailyReport = dailyReportRepository.findById(dailyReportId)
                    .orElseThrow(() -> new RuntimeException("not found daily report"));
            DetailReport detailReport = detailReportRepository.findById(detailReportId)
                    .orElseThrow(() -> new RuntimeException("not found detail report"));
            permissionCheck(dailyReport.getUser().getUsername(), principal);

            Set<DetailReport> detailReports = dailyReport.getDetailReports();

            detailReports.remove(detailReport);
            for (DocumentPath documentPath: detailReport.getDocumentPaths() ){
                mediaService.deleteFileDocument(documentPath.getDocumentHashName());
                documentPartRepository.delete(documentPath);
            }
            dailyReport.setDetailReports(detailReports);
            DailyReport saveDailyReport = dailyReportRepository.save(dailyReport);
            detailReportRepository.delete(detailReport);
            return saveDailyReport;
        } catch (Exception e) {
            log.error("### Error: deleteDetailWork " + e.getMessage());
            throw CommonException.of("Error: {}", e.getMessage());
        }
    }
}
