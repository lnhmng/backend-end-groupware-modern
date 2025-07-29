package com.groupware.controller.api;

import com.groupware.common.CommonResponse;
import com.groupware.dto.request.AirLeakRequest;
import com.groupware.dto.request.DailyReportRequest;
import com.groupware.service.*;
import com.groupware.service.mes.AirLeakService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.security.Principal;
import java.util.*;

@RestController
@RequestMapping("/api/v1/common")
public class CommonController {
    @Autowired
    MediaService mediaService;
    @Autowired
    DailyReportService dailyReportService;
    @Autowired
    AirLeakService airLeakService;
    @Autowired
    DepartmentService departmentService;
    @Autowired
    DetailReportService detailReportService;
    @Autowired
    UserService userService;

    @PostMapping("/file-upload")
    public CommonResponse<?> uploadFile(@RequestParam List<MultipartFile> files, Principal principal) {
        Set<String> strings = new HashSet<>();
        return CommonResponse.success(mediaService.createDocumentPath(files, principal, strings));
    }

    @GetMapping("/download/{id}")
    public ResponseEntity<Resource> downloadFile(@PathVariable int id) throws IOException {
        Resource resource = mediaService.downloadFile(id);
        return ResponseEntity.ok().header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + resource.getFilename() + "\"").contentLength(resource.contentLength()).contentType(MediaType.APPLICATION_OCTET_STREAM).body(resource);
    }

    @PreAuthorize("isAuthenticated()")
    @PutMapping("/title")
    public CommonResponse<?> updateTitleReport(@RequestParam(defaultValue = "", required = false) String title, @RequestParam(defaultValue = "", required = false) int id, @RequestParam(defaultValue = "", required = false) String date, Principal principal) {
        return CommonResponse.success(dailyReportService.updateTitleDailyReport(id, title, date, principal));
    }

    @PreAuthorize("isAuthenticated()")
    @PutMapping("/detail-edit")
    public CommonResponse<?> updateDetailReport(@RequestParam(defaultValue = "", required = false) String detail, @RequestParam(defaultValue = "", required = false) int id, @RequestParam(defaultValue = "", required = false) int statusId, @RequestParam(required = false) List<MultipartFile> files, Principal principal) {
        return CommonResponse.success(dailyReportService.updateDetailReport(id, detail, statusId, files, principal));
    }

    @PostMapping("/file-update")
    public CommonResponse<?> updateFile(@RequestParam(defaultValue = "", required = false) int detailReportId, @RequestParam List<MultipartFile> files, Principal principal) {
        return CommonResponse.success(mediaService.updateDocumentPath(detailReportId, files, principal));
    }

    @PutMapping("/delete-document")
    public CommonResponse<?> deleteDocument(@RequestParam int documentId, Principal principal) {
        return CommonResponse.success(dailyReportService.deleteDocument(documentId, principal));
    }

    @GetMapping("/detail-report")
    public CommonResponse<?> detailReport(@RequestParam int detailReportId, Principal principal) {
        return CommonResponse.success(dailyReportService.getDetailReport(detailReportId, principal));
    }

    @PostMapping("/update-report")
    public CommonResponse<?> addMoreDetailReport(@Valid @RequestBody DailyReportRequest dailyReportRequest, Principal principal) {
        return CommonResponse.success(dailyReportService.updateDetailWork(dailyReportRequest, principal));
    }

    @DeleteMapping("/delete-detail")
    public CommonResponse<?> deleteDetailReport(@RequestParam int dailyReportId, @RequestParam int detailReportId, Principal principal) {
        return CommonResponse.success(dailyReportService.deleteDetailWork(dailyReportId, detailReportId, principal));
    }

    @PostMapping("/air-leak")
    public CommonResponse<?> uploadAirLeak(@RequestBody AirLeakRequest airLeakRequest) {
        return CommonResponse.success(airLeakService.createAirLeak(airLeakRequest));
    }

    @PreAuthorize("isAuthenticated()")
    @GetMapping("/department")
    public CommonResponse<?> getDepartment(Principal principal) {
        return CommonResponse.success(departmentService.getDepartmentWithPermission(principal));
    }

    @PreAuthorize("isAuthenticated() and #descriptionContent.length() >= 13")
    @PutMapping("/description")
    public CommonResponse<?> putDescription(@RequestParam(required = true) int detailId, @RequestParam(required = true) String descriptionContent, Principal principal) {
        return CommonResponse.success(detailReportService.updateDescriptionReport(detailId, descriptionContent, principal));
    }

    @PreAuthorize("isAuthenticated() and #commentContent.length() >= 9")
    @PutMapping("/add-comment")
    public CommonResponse<?> addComment(@RequestParam(required = true) int detailId, @RequestParam(required = true) String commentContent, Principal principal) {
        return CommonResponse.success(detailReportService.commentPost(detailId, commentContent, principal));
    }

    @PreAuthorize("isAuthenticated() and #commentContent.length() >= 9")
    @PutMapping("/edit-comment")
    public CommonResponse<?> editComment(@RequestParam(required = true) int detailId, @RequestParam(required = true) int commentId, @RequestParam(required = true) String commentContent, Principal principal) {
        return CommonResponse.success(detailReportService.editComment(detailId, commentId, commentContent, principal));
    }

    @PreAuthorize("isAuthenticated()")
    @DeleteMapping("/delete-comment")
    public CommonResponse<?> deleteComment(@RequestParam(required = true) int detailId, @RequestParam(required = true) int commentId, Principal principal){
        return CommonResponse.success(detailReportService.deleteComment(detailId, commentId, principal));
    }

    @PreAuthorize("isAuthenticated()")
    @GetMapping("/emp-structure")
    public CommonResponse<?> empStructure(){
        return CommonResponse.success(userService.empStructure());
    }

    @PreAuthorize("isAuthenticated() and #currentPassword.length() >= 6 and #newPassword.length() >= 6")
    @PutMapping("/change-password")
    public CommonResponse<?> changePassword(@RequestParam String currentPassword, @RequestParam String newPassword, Principal principal){
        return userService.changePassword(currentPassword, newPassword, principal);
    }
}
