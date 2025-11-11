package com.kin1st.teddybearshopping.dto;

import jakarta.validation.constraints.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SignupRequest {
    @NotBlank(message = "{user.username.notblank}")
    @Size(min = 4, max = 20, message = "{user.username.size}")
    @Pattern(regexp = "^[a-zA-Z0-9_]+$", message = "{user.username.pattern}")
    private String username;

    @NotBlank(message = "{user.password.notblank}")
    @Size(min = 6, max = 100, message = "{user.password.size}")
    private String password;

    @NotBlank(message = "{user.email.notblank}")
    @Email(message = "{user.email.valid}")
    private String email;
    
    @Size(max = 100, message = "{user.fullname.size}")
    private String fullName;
    
    @Pattern(regexp = "^\\+?[0-9]{10,15}$", message = "{user.phone.invalid}")
    private String phoneNumber;
}
