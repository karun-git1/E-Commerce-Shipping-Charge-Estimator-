package com.jumbotail.shipping.dto.response;

import com.jumbotail.shipping.model.enums.DeliverySpeed;
import com.jumbotail.shipping.model.enums.TransportMode;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ShippingChargeResponse {

    private Long customerId;
    private String customerName;
    private Long productId;
    private String productName;
    private Long warehouseId;
    private String warehouseName;
    private Double distanceKm;
    private Double actualWeight;
    private Double volumetricWeight;
    private Double chargeableWeight;
    private DeliverySpeed deliverySpeed;
    private TransportMode transportMode;
    private Double baseCharge;
    private Double expressSurcharge;
    private Double totalCharge;
    private String estimatedDeliveryTime;

}
