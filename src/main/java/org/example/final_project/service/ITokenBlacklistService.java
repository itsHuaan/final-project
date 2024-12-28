package org.example.final_project.service;

public interface ITokenBlacklistService {
    boolean isTokenPresent(String token);
    int saveToken(String token);
}
