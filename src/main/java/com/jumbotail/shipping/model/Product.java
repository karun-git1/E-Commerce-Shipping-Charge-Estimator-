package com.jumbotail.shipping.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "products")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    private String description;
    private Double weight;
    private Double length;
    private Double width;
    private Double height;

    @ManyToOne
    @JoinColumn(name = "seller_id")
    private Seller seller;

    public double getVolumetricWeight() {
        if (length == null || width == null || height == null) {
            return 0.0;
        }
        return (length * width * height) / 5000.0;
    }

}
