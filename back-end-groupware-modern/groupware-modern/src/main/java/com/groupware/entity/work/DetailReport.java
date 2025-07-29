package com.groupware.entity.work;

import com.groupware.entity.document.DocumentPath;
import com.groupware.entity.user.User;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.util.Date;
import java.util.List;
import java.util.Set;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "detail_report")
public class DetailReport {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private String workDetail;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "workStatusId")
    private WorkStatus workStatus;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(name = "detail_report_document",
    joinColumns = @JoinColumn(name = "detail_report_id"),
    inverseJoinColumns = @JoinColumn(name = "document_path_id"))
    private Set<DocumentPath> documentPaths;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(name = "detail_report_comment",
    joinColumns = @JoinColumn(name = "detail_report_id"),
    inverseJoinColumns = @JoinColumn(name = "comment_id"))
    private List<CommentPost> commentDetailReports;

    @ManyToOne
    @JoinColumn(name = "descriptionId")
    private Description description;

    @CreationTimestamp
    private Date createdAt;

    @UpdateTimestamp
    private Date updatedAt;

    @OneToOne
    @JoinColumn(name = "updatedBy")
    private User userUpdated;
}