package com.kin1st.teddybearshopping.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class LoginRequest {

    @NotBlank(message = "{user.username.notblank}")
    @Size(min = 4, max = 20, message = "{user.username.size}")
    @Pattern(regexp = "^[a-zA-Z0-9_]+$", message = "{user.username.pattern}")
    private String username;

    @NotBlank(message = "{user.password.notblank}")
    @Size(min = 6, max = 100, message = "{user.password.size}")
    private String password;
}
