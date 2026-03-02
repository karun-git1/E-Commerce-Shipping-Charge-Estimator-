package com.jumbotail.shipping.service.shipping;

import com.jumbotail.shipping.dto.response.ShippingChargeResponse;
import com.jumbotail.shipping.model.Customer;
import com.jumbotail.shipping.model.Product;
import com.jumbotail.shipping.model.Warehouse;
import com.jumbotail.shipping.model.enums.DeliverySpeed;
import com.jumbotail.shipping.model.enums.TransportMode;
import org.springframework.stereotype.Component;

@Component
public class ExpressShippingStrategy implements ShippingStrategy {

    private static final double EXPRESS_SURCHARGE_MULTIPLIER = 1.5;

    @Override
    public ShippingChargeResponse calculateShipping(
            Customer customer,
            Product product,
            Warehouse warehouse,
            double distance,
            TransportMode transportMode) {

        double actualWeight = product.getWeight() != null ? product.getWeight() : 0.0;
        double volumetricWeight = product.getVolumetricWeight();
        double chargeableWeight = Math.max(actualWeight, volumetricWeight);

        double baseCharge = transportMode.calculateCharge(distance, actualWeight, volumetricWeight);
        double expressSurcharge = baseCharge * (EXPRESS_SURCHARGE_MULTIPLIER - 1);
        double totalCharge = baseCharge * EXPRESS_SURCHARGE_MULTIPLIER;

        return ShippingChargeResponse.builder()
                .customerId(customer.getId())
                .customerName(customer.getName())
                .productId(product.getId())
                .productName(product.getName())
                .warehouseId(warehouse.getId())
                .warehouseName(warehouse.getName())
                .distanceKm(Math.round(distance * 100.0) / 100.0)
                .actualWeight(Math.round(actualWeight * 100.0) / 100.0)
                .volumetricWeight(Math.round(volumetricWeight * 100.0) / 100.0)
                .chargeableWeight(Math.round(chargeableWeight * 100.0) / 100.0)
                .deliverySpeed(DeliverySpeed.EXPRESS)
                .transportMode(transportMode)
                .baseCharge(Math.round(baseCharge * 100.0) / 100.0)
                .expressSurcharge(Math.round(expressSurcharge * 100.0) / 100.0)
                .totalCharge(Math.round(totalCharge * 100.0) / 100.0)
                .estimatedDeliveryTime(getEstimatedDeliveryTime(transportMode))
                .build();
    }

    private String getEstimatedDeliveryTime(TransportMode transportMode) {
        return switch (transportMode) {
            case ROAD -> "1-2 business days";
            case RAIL -> "2-3 business days";
            case AIR -> "Same day or next day";
        };
    }

}
