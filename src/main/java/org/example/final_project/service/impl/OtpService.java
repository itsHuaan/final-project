package org.example.final_project.service.impl;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.example.final_project.dto.OtpDto;
import org.example.final_project.entity.OtpEntity;
import org.example.final_project.mapper.OtpMapper;
import org.example.final_project.model.OtpModel;
import org.example.final_project.repository.IOtpRepository;
import org.example.final_project.service.IOtpService;
import org.example.final_project.specification.OtpSpecification;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.security.SecureRandom;
import java.time.LocalDateTime;
import java.util.List;

import static org.example.final_project.specification.OtpSpecification.*;
import static org.example.final_project.util.Const.OTP_LENGTH;
import static org.example.final_project.util.Const.SALTCHARS;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class OtpService implements IOtpService {

    IOtpRepository otpRepository;
    OtpMapper otpMapper;
    

    @Override
    public List<OtpDto> getAll() {
        return List.of();
    }

    @Override
    public OtpDto getById(Long id) {
        return null;
    }

    @Override
    public int save(OtpModel otpModel) {
        Specification<OtpEntity> specification = Specification.where(OtpSpecification.hasEmail(otpModel.getEmail()));
        if (otpRepository.findOne(specification).isPresent()) {
            OtpEntity otp = otpRepository.findOne(specification).get();
            otp.setStatus(1);
            otp.setOtpCode(otpModel.getOtpCode());
            otp.setCreatedAt(LocalDateTime.now());
            otpRepository.save(otp);
        } else {
            otpRepository.save(otpMapper.toEntity(otpModel));
        }
        return 1;
    }

    @Override
    public int update(Long aLong, OtpModel otpModel) {
        return 0;
    }

    @Override
    public int delete(Long id) {
        return 0;
    }

    @Override
    public String generateOtp() {
        SecureRandom secureRandom = new SecureRandom();
        StringBuilder otp = new StringBuilder(OTP_LENGTH);
        for (int i = 0; i < OTP_LENGTH; i++) {
            int index = secureRandom.nextInt(SALTCHARS.length());
            otp.append(SALTCHARS.charAt(index));
        }
        return otp.toString();
    }

    @Override
    public boolean isValid(String email, String otp, LocalDateTime currentTime) {
        return otpRepository.findOne(Specification.where(hasEmail(email)
                .and(isOtp(otp))
                .and(createdWithinLastMinutes(3))
                .and(isActive()))).isPresent();
    }

    @Override
    public void setInvalid(String otp, String email) {
        OtpEntity otpEntity = otpRepository.findOne(Specification.where(isOtp(otp).and(hasEmail(email)))).orElse(null);
        if (otpEntity != null) {
            otpEntity.setStatus(0);
            otpRepository.save(otpEntity);
        }
    }

    @Override
    public int sendOtp(String email) {
        return 0;
    }
}
