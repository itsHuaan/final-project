package org.example.final_project.service;

import com.google.zxing.ChecksumException;
import com.google.zxing.FormatException;
import com.google.zxing.NotFoundException;
import org.example.final_project.dto.ApiResponse;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

public interface IQrService {
    ApiResponse<?> getUserInfo(MultipartFile file) throws IOException, ChecksumException, NotFoundException, FormatException;
}
