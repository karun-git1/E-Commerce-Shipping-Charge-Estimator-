package com.jumbotail.shipping.model.enums;

import lombok.Getter;

@Getter
public enum TransportMode {
    ROAD(5.0, 1.0, 100),
    RAIL(3.0, 1.5, 500),
    AIR(15.0, 0.5, 50);

    private final double baseRatePerKm;
    private final double volumetricDivisor;
    private final double minCharge;

    TransportMode(double baseRatePerKm, double volumetricDivisor, double minCharge) {
        this.baseRatePerKm = baseRatePerKm;
        this.volumetricDivisor = volumetricDivisor;
        this.minCharge = minCharge;
    }

    public double calculateCharge(double distance, double weight, double volumetricWeight) {
        double chargeableWeight = Math.max(weight, volumetricWeight);
        double charge = distance * baseRatePerKm * chargeableWeight;
        return Math.max(charge, minCharge);
    }
}
