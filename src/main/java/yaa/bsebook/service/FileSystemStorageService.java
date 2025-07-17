// File: src/main/java/yaa/bsebook/service/FileSystemStorageService.java
package yaa.bsebook.service;

import org.apache.commons.io.FilenameUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.UUID;

@Service
public class FileSystemStorageService implements IStorageService {

    // Tiêm giá trị từ file properties vào biến uploadPath
    @Value("${file-upload}")
    private String uploadPath;

    private Path storageFolder;

    // Phương thức này sẽ được gọi sau khi service được khởi tạo
    @PostConstruct
    public void init() {
        try {
            // Khởi tạo storageFolder bằng đường dẫn đã được tiêm vào
            storageFolder = Paths.get(uploadPath);
            if (!Files.exists(storageFolder)) {
                Files.createDirectories(storageFolder);
            }
        } catch (IOException exception) {
            throw new RuntimeException("Cannot initialize storage", exception);
        }
    }

    @Override
    public String store(MultipartFile file) {
        if (file == null || file.isEmpty()) {
            return null;
        }

        // Tạo một tên file duy nhất để tránh trùng lặp
        String fileExtension = FilenameUtils.getExtension(file.getOriginalFilename());
        String generatedFileName = UUID.randomUUID().toString().replace("-", "");
        generatedFileName = generatedFileName + "." + fileExtension;

        // Sử dụng storageFolder đã được cấu hình để tạo đường dẫn đích
        Path destinationFilePath = this.storageFolder.resolve(Paths.get(generatedFileName))
                .normalize().toAbsolutePath();

        // Lưu file
        try (InputStream inputStream = file.getInputStream()) {
            Files.copy(inputStream, destinationFilePath, StandardCopyOption.REPLACE_EXISTING);
        } catch (IOException e) {
            throw new RuntimeException("Failed to store file.", e);
        }

        // Trả về tên file đã tạo để lưu vào DB
        return generatedFileName;
    }
}
