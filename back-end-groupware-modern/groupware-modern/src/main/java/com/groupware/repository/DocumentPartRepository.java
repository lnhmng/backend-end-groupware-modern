package com.groupware.repository;

import com.groupware.entity.document.DocumentPath;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Date;
import java.util.Set;

public interface DocumentPartRepository extends JpaRepository<DocumentPath, Integer> {
    @Query(value = "SELECT *\n" +
            "\tFROM document_path dp\n" +
            "\tWHERE dp.document_name = :documentName\n" +
            "\tAND dp.created_at >= :createdAt\n" +
            "\tAND dp.user_id = :userId", nativeQuery = true)
    DocumentPath documentPath(@Param("documentName") String documentName,
                              @Param("createdAt")Date createdAt,
                              @Param("userId") int userId);

    @Query(value = "SELECT dp.document_name\n" +
            "\tFROM document_path dp\n" +
            "\tLEFT JOIN detail_report_document drd ON drd.document_path_id = dp.id \n" +
            "\tWHERE drd.detail_report_id = :detailReportId", nativeQuery = true)
    Set<String> getDocumentNames(@Param("detailReportId") int detailReportId);
}
