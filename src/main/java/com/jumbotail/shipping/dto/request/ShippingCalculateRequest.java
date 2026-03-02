package com.jumbotail.shipping.dto.request;

import com.jumbotail.shipping.model.enums.DeliverySpeed;
import com.jumbotail.shipping.model.enums.TransportMode;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ShippingCalculateRequest {

    @NotNull(message = "Customer ID is required")
    private Long customerId;

    @NotNull(message = "Product ID is required")
    private Long productId;

    @NotNull(message = "Delivery speed is required")
    private DeliverySpeed deliverySpeed;

    @NotNull(message = "Transport mode is required")
    private TransportMode transportMode;

}
