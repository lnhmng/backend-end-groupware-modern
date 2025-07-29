package com.groupware.service;

import com.groupware.entity.document.DocumentPath;
import com.groupware.entity.work.DetailReport;
import org.springframework.core.io.Resource;
import org.springframework.web.multipart.MultipartFile;

import java.security.Principal;
import java.util.List;
import java.util.Set;

public interface MediaService {
    List<DocumentPath> createDocumentPath(List<MultipartFile> files, Principal principal, Set<String> fileNames);

    Resource downloadFile(int id);

    DetailReport updateDocumentPath(int detailReportId, List<MultipartFile> files, Principal principal);

    void deleteFileDocument(String fileName);
}
