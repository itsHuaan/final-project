package org.example.final_project.service.impl;

import com.google.zxing.*;
import com.google.zxing.common.HybridBinarizer;
import com.google.zxing.qrcode.QRCodeReader;
import org.example.final_project.configuration.BufferedImageLuminanceSource;
import org.example.final_project.dto.ApiResponse;
import org.example.final_project.dto.UserInfoDto;
import org.example.final_project.service.IQrService;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.IOException;

@Service
public class QrService implements IQrService {


    @Override
    public ApiResponse<?> getUserInfo(MultipartFile file) throws IOException, ChecksumException, NotFoundException, FormatException {
        if (file.isEmpty()) {
            return ApiResponse.createResponse(HttpStatus.BAD_REQUEST, "không có ảnh", null);
        }
        BufferedImage image = ImageIO.read(file.getInputStream());

        if (image == null) {
            return ApiResponse.createResponse(HttpStatus.BAD_REQUEST, "ảnh không tồn tại", null);
        }
        LuminanceSource luminanceSource = new BufferedImageLuminanceSource(image);
        BinaryBitmap binaryBitmap = new BinaryBitmap(new HybridBinarizer(luminanceSource));
        QRCodeReader qrCodeReader = new QRCodeReader();
        com.google.zxing.Result result = qrCodeReader.decode(binaryBitmap);

        String userData = result.getText();
        UserInfoDto userInfoDto = new UserInfoDto();
        String[] parts = userData.split("\\|\\|");
        userInfoDto.setNumber(parts[0]);
        String[] part = parts[1].split("\\|");
        if (part.length >= 2) {
            userInfoDto.setName(part[0]);
            userInfoDto.setDateOfBirth(part[1]);
            userInfoDto.setGender(part[2]);
            userInfoDto.setAddress(part[3]);
            userInfoDto.setIssueDate(part[4]);
        }
        return ApiResponse.createResponse(HttpStatus.OK, "thông tin căn cước của bạn :  ", userInfoDto);
    }
}
