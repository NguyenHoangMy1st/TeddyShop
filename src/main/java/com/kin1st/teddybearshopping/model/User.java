package com.kin1st.teddybearshopping.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.kin1st.teddybearshopping.validation.PhoneNumber;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "users")
@Getter
@Setter
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "{user.username.notblank}")
    @Size(min = 4, max = 20, message = "{user.username.size}")
    @Pattern(regexp = "^[a-zA-Z0-9_]+$", message = "{user.username.pattern}")
    @Column(nullable = false, unique = true)
    private String username;

    @JsonIgnore
    @NotBlank(message = "{user.password.notblank}")
    @Size(min = 6, max = 100, message = "{user.password.size}")
    @Column(nullable = false)
    private String password;

    @NotBlank(message = "{user.email.notblank}")
    @Email(message = "{user.email.valid}")
    @Column(nullable = false, unique = true)
    private String email;

    @NotBlank(message = "{user.role.notblank}")
    @Pattern(regexp = "^(ROLE_USER|ROLE_ADMIN)$", message = "{user.role.invalid}")
    private String role = "ROLE_USER";
    
    @Pattern(regexp = "^\\+?[0-9]{10,15}$", message = "{user.phone.invalid}")
    private String phoneNumber;
    
    @Size(max = 100, message = "{user.fullname.size}")
    private String fullName;
    
    private boolean enabled = true;

}
