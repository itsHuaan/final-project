package org.example.final_project.service;

import org.example.final_project.dto.OtpDto;
import org.example.final_project.model.OtpModel;

import java.time.LocalDateTime;

public interface IOtpService extends IBaseService<OtpDto, OtpModel, Long>{
    String generateOtp();
    boolean isValid(String email, String otp, LocalDateTime currentTime);
    void setInvalid(String otp, String email);
    int sendOtp(String email);
}
