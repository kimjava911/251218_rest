package kr.java.restapi.service;

import jakarta.annotation.PostConstruct;
import kr.java.restapi.model.dto.FileResponse;
import kr.java.restapi.model.entity.FileEntity;
import kr.java.restapi.exception.BadRequestException;
import kr.java.restapi.exception.NotFoundException;
import kr.java.restapi.model.repository.FileRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.List;
import java.util.Set;
import java.util.UUID;

// #(2)-4
@Slf4j
@Service
@RequiredArgsConstructor
// import org.springframework.transaction.annotation.Transactional;
@Transactional(readOnly = true)
public class FileService {

    private final FileRepository fileRepository;

    // import org.springframework.beans.factory.annotation.Value;
    @Value("${file.upload-dir}")
    private String uploadDir;

    // 허용 파일 타입 (화이트리스트)
    private static final Set<String> ALLOWED_TYPES = Set.of(
            "image/jpeg", "image/png", "image/gif",
            "application/pdf", "text/plain"
    );

    // 초기화: 업로드 디렉토리 생성
    @PostConstruct
    public void init() {
        try {
            Path uploadPath = Paths.get(uploadDir);
            if (!Files.exists(uploadPath)) {
                Files.createDirectories(uploadPath);
            }
        } catch (IOException e) {
            throw new RuntimeException("업로드 디렉토리 생성 실패", e);
        }
    }

    // UPLOAD: 파일 업로드
    @Transactional
    public FileResponse upload(MultipartFile file) {
        // 1. 검증
        validateFile(file);

        // 2. UUID 파일명 생성 (원본 파일명 신뢰 금지!)
        String originalName = file.getOriginalFilename();
        String extension = extractExtension(originalName);
        String storedName = UUID.randomUUID() + extension;

        // 3. 저장 경로
        Path filePath = Paths.get(uploadDir).resolve(storedName);

        try {
            // 4. 파일 저장
            Files.copy(file.getInputStream(), filePath,
                    StandardCopyOption.REPLACE_EXISTING);

            // 5. 메타데이터 DB 저장
            FileEntity fileEntity = FileEntity.builder()
                    .originalName(originalName)
                    .storedName(storedName)
                    .contentType(file.getContentType())
                    .fileSize(file.getSize())
                    .filePath(filePath.toString())
                    .build();

            return FileResponse.from(fileRepository.save(fileEntity));

        } catch (IOException e) {
            throw new RuntimeException("파일 저장 실패", e);
        }
    }

    // DOWNLOAD: 파일 리소스 로드
    public Resource loadAsResource(Long fileId) {
        FileEntity fileEntity = fileRepository.findById(fileId)
                // #(3)-6-1
//                .orElseThrow(() -> new NoSuchElementException("파일이 존재하지 않습니다: " + fileId));
                .orElseThrow(() -> new NotFoundException("파일이 존재하지 않습니다: " + fileId));

        try {
            Path filePath = Paths.get(fileEntity.getFilePath());
            Resource resource = new UrlResource(filePath.toUri());

            if (resource.exists() && resource.isReadable()) {
                return resource;
            }
            // #(3)-6-2
//            throw new NoSuchElementException("파일이 존재하지 않습니다: " + fileId);
            throw new NotFoundException("파일이 존재하지 않습니다: " + fileId);

        } catch (MalformedURLException e) {
            // #(3)-6-3
//            throw new IllegalArgumentException("파일 경로 오류: " + fileId);
            throw new NotFoundException("파일 경로 오류: " + fileId);
        }
    }

    public FileEntity findById(Long id) {
        return fileRepository.findById(id)
                // #(5)-9
//                .orElseThrow(() -> new NoSuchElementException("파일이 존재하지 않습니다: " + id));
                .orElseThrow(() -> new NotFoundException("파일이 존재하지 않습니다: " + id));
    }

    public List<FileResponse> findAll() {
        return fileRepository.findAll().stream()
                .map(FileResponse::from)
                .toList();
    }

    // 파일 검증
    private void validateFile(MultipartFile file) {
        if (file.isEmpty()) {
            // #(3)-6-4
//            throw new IllegalArgumentException("파일이 비어있습니다.");
            throw new BadRequestException("파일이 비어있습니다.");
        }
        String contentType = file.getContentType();
        if (contentType == null || !ALLOWED_TYPES.contains(contentType)) {
            // #(3)-6-5
//            throw new IllegalArgumentException("허용되지 않는 파일 형식입니다.");
            throw new BadRequestException("허용되지 않는 파일 형식입니다.");
        }
    }

    // 확장자 추출
    private String extractExtension(String filename) {
        if (filename == null || !filename.contains(".")) return "";
        return filename.substring(filename.lastIndexOf("."));
    }
}