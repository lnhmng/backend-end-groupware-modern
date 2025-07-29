package com.groupware.repository;

import com.groupware.entity.work.DetailReport;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface DetailReportRepository extends JpaRepository<DetailReport, Integer> {
    @Query(value = "SELECT *\n" +
            "\tFROM detail_report dr\n" +
            "\tWHERE dr.id = (\n" +
            "\t\tSELECT drd.detail_report_id\n" +
            "\t\t\tFROM detail_report_document drd\n" +
            "\t\t\tWHERE drd.document_path_id = :documentPathId)", nativeQuery = true)
    DetailReport getDetailReport(@Param("documentPathId") int id);
}
