package org.example.final_project.service.impl;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.example.final_project.entity.TokenBlacklist;
import org.example.final_project.repository.ITokenBlacklistRepository;
import org.example.final_project.service.ITokenBlacklistService;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class TokenBlacklistBlacklistService implements ITokenBlacklistService {
    ITokenBlacklistRepository tokenBlacklistRepository;

    @Override
    public boolean isTokenPresent(String token) {
        return tokenBlacklistRepository.findByToken(token).isPresent();
    }

    @Override
    public int saveToken(String token) {
        try {
            tokenBlacklistRepository.save(TokenBlacklist.builder()
                    .token(token)
                    .expiryDate(LocalDateTime.now().minusMinutes(10))
                    .build());
            return 1;
        } catch (Exception e) {
            return 0;
        }
    }

    
}
