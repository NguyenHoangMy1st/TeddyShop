package com.kin1st.teddybearshopping.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import com.kin1st.teddybearshopping.validation.PhoneNumber;
import org.hibernate.validator.constraints.URL;

@Entity
@Table(name = "products")
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "{product.name.notblank}")
    @Size(min = 2, max = 100, message = "{product.name.size}")
    @Column(nullable = false)
    private String name;

    @NotNull(message = "{product.price.notnull}")
    @DecimalMin(value = "0.0", inclusive = false, message = "{product.price.positive}")
    @Column(nullable = false)
    private Double price;

    @Size(max = 50, message = "{product.size.size}")
    private String size;

    @Size(max = 50, message = "{product.color.size}")
    private String color;

    @Size(max = 1000, message = "{product.description.size}")
    @Column(length = 1000)
    private String description;

    @URL(message = "{product.imageUrl.valid}")
    @Size(max = 255, message = "{product.imageUrl.size}")
    private String imageUrl; // link ảnh sản phẩm

    private Boolean active = true; // để disable sản phẩm khi không bán nữa

    public Product() {
    }

    public Product(String name, Double price, String size, String color, String description, String imageUrl) {
        this.name = name;
        this.price = price;
        this.size = size;
        this.color = color;
        this.description = description;
        this.imageUrl = imageUrl;
        this.active = true;
    }

    // Getters & Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    public String getSize() {
        return size;
    }

    public void setSize(String size) {
        this.size = size;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public Boolean getActive() {
        return active;
    }

    public void setActive(Boolean active) {
        this.active = active;
    }
}
