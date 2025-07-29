package com.groupware.entity.work;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.groupware.entity.department.Department;
import com.groupware.entity.position.Position;
import com.groupware.entity.user.User;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.util.Date;
import java.util.Set;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table(name = "daily_report")
public class DailyReport {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @JsonIgnore
    @ManyToOne
    @JoinColumn(name = "userId")
    private User user;

    @ManyToOne
    @JoinColumn(name = "positionId")
    private Position position;

    @ManyToOne
    @JoinColumn(name = "departmentId")
    private Department department;

    private String title;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(name = "daily_detail_report",
            joinColumns = @JoinColumn(name = "daily_report_id"),
            inverseJoinColumns = @JoinColumn(name = "detail_report_id"))
    private Set<DetailReport> detailReports;

    private Date dateReport;
    private Boolean useStatus;
    @CreationTimestamp
    private Date createdAt;
    private Integer updatedBy;
    @UpdateTimestamp
    private Date updatedAt;
}
