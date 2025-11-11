package com.kin1st.teddybearshopping.common;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
public class ApiResponse<T> {
    private boolean success;
    private String message;
    private T data;
    private String error;
    private int status;
    private LocalDateTime timestamp = LocalDateTime.now();

    public ApiResponse(boolean success, String message, T data, String error, int status) {
        this.success = success;
        this.message = message;
        this.data = data;
        this.error = error;
        this.status = status;
    }
}
