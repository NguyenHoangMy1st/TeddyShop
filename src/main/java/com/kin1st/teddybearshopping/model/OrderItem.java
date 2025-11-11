package com.kin1st.teddybearshopping.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "order_items")
@Getter
@Setter
public class OrderItem {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "order_id", nullable = false)
    @NotNull(message = "{orderItem.order.notnull}")
    private Order order;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id", nullable = false)
    @NotNull(message = "{orderItem.product.notnull}")
    private Product product;

    @Min(value = 1, message = "{orderItem.quantity.min}")
    @Max(value = 1000, message = "{orderItem.quantity.max}")
    private int quantity = 1;

    @DecimalMin(value = "0.0", inclusive = false, message = "{orderItem.price.positive}")
    @NotNull(message = "{orderItem.price.notnull}")
    private Double price;

    @Size(max = 500, message = "{orderItem.notes.size}")
    private String notes;
}