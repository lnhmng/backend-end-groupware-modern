package com.groupware.service.impl;

import com.groupware.entity.document.DocumentPath;
import com.groupware.entity.work.DetailReport;
import com.groupware.exception.CommonException;
import com.groupware.repository.DetailReportRepository;
import com.groupware.repository.DocumentPartRepository;
import com.groupware.repository.UserRepository;
import com.groupware.service.MediaService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FilenameUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.security.Principal;
import java.util.*;

@Slf4j
@Service
public class MediaServiceImpl implements MediaService {
    @Value("${path.data}")
    private String dataPath;

    @Autowired
    private DocumentPartRepository documentPartRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private DetailReportRepository detailReportRepository;

    @Override
    public List<DocumentPath> createDocumentPath(List<MultipartFile> files, Principal principal, Set<String> fileNames) {
        try {

            Path storageFolder = Paths.get(dataPath);
            List<DocumentPath> documentPaths = new ArrayList<>();
            for (MultipartFile file : files) {
                if (file.isEmpty()) {
                    throw new RuntimeException("Failed to store empty file.");
                }
                String fileName = file.getOriginalFilename();
                if (fileNames.contains(fileName)) {
                    // Tên file đã tồn tại, xử lý theo yêu cầu của bạn tại đây
                    throw new RuntimeException("The file name has been duplicated: " + fileName);
                } else {
                    fileNames.add(fileName);
                }
                //file must be <= 5Mb
                float fileSizeInMegabytes = file.getSize() / 1_000_000.0f;
                if (fileSizeInMegabytes > 5.0f) {
                    throw new RuntimeException("File must be <= 5Mb");
                }
                String fileExtension = FilenameUtils.getExtension(file.getOriginalFilename());
                String generatedFileName = UUID.randomUUID().toString().replace("-", "");
                generatedFileName = generatedFileName + "." + fileExtension;
                Path destinationFilePath = storageFolder.resolve(
                                Paths.get(generatedFileName))
                        .normalize().toAbsolutePath();
                if (!destinationFilePath.getParent().equals(storageFolder.toAbsolutePath())) {
                    throw new RuntimeException(
                            "Cannot store file outside current directory.");
                }
                try (InputStream inputStream = file.getInputStream()) {
                    Files.copy(inputStream, destinationFilePath, StandardCopyOption.REPLACE_EXISTING);
                }
                DocumentPath documentPath = new DocumentPath();
                documentPath.setDocumentName(file.getOriginalFilename());
                documentPath.setDocumentSize(fileSizeInMegabytes * 1000);
                documentPath.setDocumentHashName(generatedFileName);
                documentPath.setUseStatus(true);
                if (principal != null){
                    documentPath.setUser(userRepository.findByUsername(principal.getName())
                            .orElseThrow(() -> new RuntimeException("Not found username")));
                }
                documentPaths.add(documentPath);
            }
            return documentPartRepository.saveAll(documentPaths);
        }catch (Exception e){
            log.error("### Error: " + e.getMessage());
            throw CommonException.of("Error: {}", e.getMessage());
        }
    }

    @Override
    public Resource downloadFile(int id) {
        try {
            DocumentPath documentPath = documentPartRepository.findById(id)
                    .orElseThrow(() -> new RuntimeException("Not found Document"));
            return new FileSystemResource(dataPath + "/"  + documentPath.getDocumentHashName());
        } catch (Exception e){
            log.error("### Error: " + e.getMessage());
            throw CommonException.of("Error: {}", e.getMessage());
        }
    }

    @Override
    public DetailReport updateDocumentPath(int detailReportId, List<MultipartFile> files, Principal principal) {
        try {
            Set<String> fileNames = documentPartRepository.getDocumentNames(detailReportId);
            List<DocumentPath> createDocumentPaths = createDocumentPath(files, principal, fileNames);
            DetailReport detailReport = detailReportRepository.findById(detailReportId)
                    .orElseThrow(()-> new RuntimeException("not found detail report"));

            Set<DocumentPath> documentPaths = new HashSet<>(createDocumentPaths);
            documentPaths.addAll(detailReport.getDocumentPaths());
            detailReport.setDocumentPaths(documentPaths);
            return detailReportRepository.save(detailReport);
        } catch (Exception e){
            log.error("### Error updateDocumentPath: " + e.getMessage());
            throw CommonException.of("Error: {}" + e.getMessage());
        }
    }

    @Override
    public void deleteFileDocument(String fileName) {
        try {
            Path storageFolder = Paths.get(dataPath + "/" + fileName);
            Files.delete(storageFolder);
        }catch (Exception e){
            log.error("### Error deleteFileDocument: " + e.getMessage());
            throw CommonException.of("Error: {}" + e.getMessage());
        }
    }
}