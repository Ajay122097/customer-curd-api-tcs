package com.curd_api.customer.curd.dto;

import lombok.*;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ApiResponse<T> {
    private LocalDateTime timestamp;
    private int status;
    private String message;
    private T data;

    public static <T> ApiResponse<T> success(String message, T data) {
        return new ApiResponse<>(LocalDateTime.now(), 200, message, data);
    }

    public static <T> ApiResponse<T> created(String message, T data) {
        return new ApiResponse<>(LocalDateTime.now(), 201, message, data);
    }

    public static <T> ApiResponse<T> error(String message) {
        return new ApiResponse<>(LocalDateTime.now(), 500, message, null);
    }
}
