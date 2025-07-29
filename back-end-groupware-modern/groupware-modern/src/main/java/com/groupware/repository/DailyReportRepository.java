package com.groupware.repository;

import com.groupware.entity.work.DailyReport;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Map;
import java.util.Optional;

public interface DailyReportRepository extends JpaRepository<DailyReport, Integer> {
    @Query(value = "SELECT dr.id, dr.date_report as dateReport, dr.title, " +
            "CONCAT(u.firstname, ' ', u.lastname) as reportBy, " +
            "d.department_name as departmentName, p.position_name positionName, " +
            "dr.created_at as createdAt, ROW_NUMBER() OVER (ORDER BY dr.id DESC) as rowNum " +
            "FROM daily_report dr " +
            "JOIN _department d ON dr.department_id = d.id " +
            "LEFT JOIN _position p ON dr.position_id = p.id " +
            "LEFT JOIN _user u ON dr.user_id = u.id " +
            "WHERE dr.use_status = 1 " +
            "AND (COALESCE(:dateReport, '') = '' OR dr.date_report = :dateReport) " +
            "AND dr.department_id IN (:departmentId)", nativeQuery = true)
    List<Map<String, Object>> lstDailyReport(@Param("dateReport") String dateReport, @Param("departmentId") List<Integer> departmentId);

    @Query(value = "SELECT *\n" +
            "\tFROM daily_report\n" +
            "\tWHERE id = (\n" +
            "\t\t\t\tSELECT daily_report_id \n" +
            "\t\t\t\t\tFROM daily_detail_report\n" +
            "\t\t\t\t\tWHERE detail_report_id = :detailReportId)", nativeQuery = true)
    Optional<DailyReport> dailyReport(@Param("detailReportId") int detailReportId);
}
