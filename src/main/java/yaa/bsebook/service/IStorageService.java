package yaa.bsebook.service;

import org.springframework.web.multipart.MultipartFile;

public interface IStorageService {
    String store(MultipartFile file);
}