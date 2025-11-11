package com.kin1st.teddybearshopping.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import lombok.Getter;
import lombok.Setter;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "orders")
@Getter
@Setter
public class Order {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    @JsonIgnoreProperties({"orders", "cart"})
    @NotNull(message = "{order.user.notnull}")
    private User user;

    @NotBlank(message = "{order.customerName.notblank}")
    @Size(max = 100, message = "{order.customerName.size}")
    private String customerName;

    @NotBlank(message = "{order.customerEmail.notblank}")
    @Email(message = "{order.customerEmail.valid}")
    @Size(max = 100, message = "{order.customerEmail.size}")
    private String customerEmail;

    @NotBlank(message = "{order.customerAddress.notblank}")
    @Size(max = 255, message = "{order.customerAddress.size}")
    private String customerAddress;

    @PastOrPresent(message = "{order.createdAt.pastorpresent}")
    private LocalDateTime createdAt = LocalDateTime.now();

    @Enumerated(EnumType.STRING)
    @NotNull(message = "{order.status.notnull}")
    private OrderStatus status = OrderStatus.PENDING;

    @DecimalMin(value = "0.0", message = "{order.totalPrice.positive}")
    @NotNull(message = "{order.totalPrice.notnull}")
    private Double totalPrice = 0.0;

    @Valid
    @NotEmpty(message = "{order.items.notempty}")
    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<@Valid OrderItem> items = new ArrayList<>();

    @Size(max = 1000, message = "{order.notes.size}")
    private String notes;

    @Pattern(regexp = "^\\+?[0-9]{10,15}$", message = "{order.phoneNumber.invalid}")
    private String phoneNumber;
}
