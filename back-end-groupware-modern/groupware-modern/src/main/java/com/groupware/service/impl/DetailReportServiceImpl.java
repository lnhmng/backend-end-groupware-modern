package com.groupware.service.impl;

import com.groupware.entity.user.User;
import com.groupware.entity.work.CommentPost;
import com.groupware.entity.work.DailyReport;
import com.groupware.entity.work.Description;
import com.groupware.entity.work.DetailReport;
import com.groupware.exception.CommonException;
import com.groupware.repository.*;
import com.groupware.service.DailyReportService;
import com.groupware.service.DetailReportService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.security.Principal;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Slf4j
@Service
@AllArgsConstructor
public class DetailReportServiceImpl implements DetailReportService {
    final DetailReportRepository detailReportRepository;
    final DescriptionRepository descriptionRepository;
    final UserRepository userRepository;
    final CommentPostRepository commentPostRepository;
    final DailyReportService dailyReportService;
    final DailyReportRepository dailyReportRepository;

    @Transactional
    @Override
    public DetailReport updateDescriptionReport(Integer detailId, String descriptionContent, Principal principal) {
        try {
            DetailReport detailReport = detailReportRepository.findById(detailId)
                    .orElseThrow(() -> new RuntimeException("Not found id"));
            Description description = new Description();
            if (!Objects.isNull(detailReport.getDescription())) {
                description = descriptionRepository.findById(detailReport.getDescription().getId())
                        .orElseThrow(() -> new RuntimeException("Not found description id"));
            }
            description.setDescriptionContent(descriptionContent);
            DailyReport dailyReport = dailyReportRepository.dailyReport(detailId)
                            .orElseThrow(()-> new RuntimeException("Not found detail id"));
            dailyReportService.permissionCheck(dailyReport.getUser().getUsername(), principal);

            descriptionRepository.save(description);
            detailReport.setDescription(description);
            return detailReportRepository.save(detailReport);
        } catch (Exception e) {
            log.error("### Error createDetailReport: " + e.getMessage());
            throw CommonException.of("Error: {}", e.getMessage());
        }
    }

    @Transactional
    @Override
    public DetailReport commentPost(int detailId, String commentContent, Principal principal) {
        try {
            DetailReport detailReport = detailReportRepository.findById(detailId)
                    .orElseThrow(() -> new RuntimeException("Not found detail report"));
            CommentPost commentPost = new CommentPost();
            User user = userRepository.findByUsername(principal.getName())
                    .orElseThrow(() -> new RuntimeException("Not found username"));
            commentPost.setContentComment(commentContent);
            commentPost.setUser(user);
            CommentPost commentPostMap = commentPostRepository.save(commentPost);

            List<CommentPost> commentPosts = new ArrayList<>(detailReport.getCommentDetailReports());
            commentPosts.add(commentPostMap);
            detailReport.setCommentDetailReports(commentPosts);
            return detailReportRepository.save(detailReport);
        } catch (Exception e) {
            log.error("### Error commentPost: " + e.getMessage());
            throw CommonException.of("Error: {}", e.getMessage());
        }
    }

    @Transactional
    @Override
    public DetailReport editComment(int detailId, int commentId, String commentContent, Principal principal) {
        try {
            CommentPost commentPost = commentPostRepository.findById(commentId)
                    .orElseThrow(() -> new RuntimeException("Not found comment id"));
            commentPost.setContentComment(commentContent);
            commentPostRepository.save(commentPost);

            dailyReportService.permissionCheck(commentPost.getUser().getUsername(), principal);

            return detailReportRepository.findById(detailId)
                    .orElseThrow(() -> new RuntimeException("Not found detail id"));
        } catch (Exception e) {
            log.error("### Error editComment: " + e.getMessage());
            throw CommonException.of("Error: {}", e.getMessage());
        }
    }

    @Transactional
    @Override
    public DetailReport deleteComment(int detailId, int commentId, Principal principal) {
        try {
            DetailReport detailReport = detailReportRepository.findById(detailId)
                    .orElseThrow(() -> new RuntimeException("Not found detail report"));
            CommentPost commentPost = commentPostRepository.findById(commentId)
                    .orElseThrow(() -> new RuntimeException("Not found comment id"));

            dailyReportService.permissionCheck(commentPost.getUser().getUsername(), principal);

            List<CommentPost> commentPosts = detailReport.getCommentDetailReports();
            commentPosts.remove(commentPost);
            detailReport.setCommentDetailReports(commentPosts);
            detailReportRepository.save(detailReport);
            commentPostRepository.delete(commentPost);
            return detailReport;
        } catch (Exception e) {
            log.error("### Error deleteComment: " + e.getMessage());
            throw CommonException.of("Error: {}", e.getMessage());
        }
    }
}
