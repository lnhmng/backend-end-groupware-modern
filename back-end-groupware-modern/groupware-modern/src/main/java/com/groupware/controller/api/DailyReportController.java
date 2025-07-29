package com.groupware.controller.api;

import com.groupware.common.CommonResponse;
import com.groupware.dto.request.DailyReportRequest;
import com.groupware.dto.request.EditDailyReportRequest;
import com.groupware.service.DailyReportService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.security.access.prepost.PostAuthorize;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;

@RestController
@RequestMapping("/api/v1/admin/daily-report")
@AllArgsConstructor
public class DailyReportController {
    final DailyReportService dailyReportService;

    @PostMapping
    public CommonResponse<?> registerDailyReport(@Valid @RequestBody DailyReportRequest dailyReportRequest, Principal principal) {
        return CommonResponse.success(dailyReportService.createDailyReport(dailyReportRequest, principal));
    }

    @PreAuthorize("isAuthenticated()")
    @PostAuthorize("returnObject.user.id == authentication.principal.id")
    @PutMapping
    public CommonResponse<?> updateDailyReport(@Valid @RequestBody EditDailyReportRequest editDailyReportRequest, Principal principal) {
        return CommonResponse.success(dailyReportService.updateDailyReport(editDailyReportRequest, principal));
    }

    @GetMapping
    public CommonResponse<?> getDailyReport(
            @RequestParam(defaultValue = "", required = false) String dataDateSearch,
            @RequestParam(defaultValue = "0", required = false) int departmentSelectModel
    ) {
        return CommonResponse.success(dailyReportService.lstDailyReport(dataDateSearch, departmentSelectModel));
    }

    @PatchMapping("/{id}")
    public CommonResponse<?> detailDailyReport(@PathVariable int id) {
        return CommonResponse.success(dailyReportService.detailDailyReport(id));
    }

    @DeleteMapping
    public CommonResponse<?> deleteDailyReport(@RequestParam(defaultValue = "", required = false) int id, Principal principal) {
        return CommonResponse.success(dailyReportService.deleteDailyReport(id, principal));
    }
}
