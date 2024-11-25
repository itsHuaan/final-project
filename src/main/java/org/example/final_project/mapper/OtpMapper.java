package org.example.final_project.mapper;

import org.example.final_project.dto.OtpDto;
import org.example.final_project.entity.OtpEntity;
import org.example.final_project.model.OtpModel;
import org.springframework.stereotype.Component;

@Component
public class OtpMapper {
    public OtpDto toDto(OtpEntity otpEntity) {
        return OtpDto.builder()
                .otpCode(otpEntity.getOtpCode())
                .build();
    }
    public OtpEntity toEntity(OtpModel otpModel) {
        return OtpEntity.builder()
                .otpCode(otpModel.getOtpCode())
                .email(otpModel.getEmail())
                .createdAt(otpModel.getCreatedAt())
                .status(otpModel.getStatus())
                .build();
    }
}
