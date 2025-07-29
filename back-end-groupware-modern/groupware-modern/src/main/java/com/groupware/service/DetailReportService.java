package com.groupware.service;

import com.groupware.entity.work.DetailReport;

import java.security.Principal;

public interface DetailReportService {
    DetailReport updateDescriptionReport(Integer detailId, String descriptionContent, Principal principal);

    DetailReport commentPost (int detailId, String commentContent, Principal principal);

    DetailReport editComment(int detailId, int commentId, String commentContent, Principal principal);

    DetailReport deleteComment(int detailId, int commentId, Principal principal);
}
