package com.kin1st.teddybearshopping.exception;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.Map;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ErrorResponse {
    public ErrorResponse(String message, int status, Map<String, String> errors) {
        this.message = message;
        this.status = status;
        this.errors = errors;
        // timestamp will be set automatically by the field's default value
    }
    private String message;
    private int status;
    private Map<String, String> errors;
    private LocalDateTime timestamp = LocalDateTime.now();
}
