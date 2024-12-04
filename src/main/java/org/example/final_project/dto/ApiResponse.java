package org.example.final_project.dto;

import lombok.*;
import org.springframework.http.HttpStatus;

import java.time.LocalDateTime;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class ApiResponse<T> {
    private int status;
    private String message;
    private T data;
    private LocalDateTime timestamp;

    public static ApiResponse<?> createResponse(HttpStatus status, String message, Object data) {
        return new ApiResponse<>(status.value(), message, data, LocalDateTime.now());
    }
}
