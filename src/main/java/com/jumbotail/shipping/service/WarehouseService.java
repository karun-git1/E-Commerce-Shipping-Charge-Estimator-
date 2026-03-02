package com.jumbotail.shipping.service;

import com.jumbotail.shipping.dto.response.NearestWarehouseResponse;
import com.jumbotail.shipping.model.Warehouse;
import com.jumbotail.shipping.repository.WarehouseRepository;
import com.jumbotail.shipping.util.HaversineDistanceCalculator;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class WarehouseService {

    private final WarehouseRepository warehouseRepository;
    private final HaversineDistanceCalculator distanceCalculator;

    @Cacheable(value = "nearestWarehouse", key = "#latitude + '_' + #longitude")
    public NearestWarehouseResponse findNearestWarehouse(double latitude, double longitude) {
        log.info("START: Finding nearest warehouse | coordinates=[lat={}, lon={}]", latitude, longitude);

        log.debug("Fetching active warehouses from database");
        List<Warehouse> activeWarehouses = warehouseRepository.findByActiveTrue();
        log.debug("Found {} active warehouses", activeWarehouses.size());

        if (activeWarehouses.isEmpty()) {
            log.error("No active warehouses found in database");
            throw new IllegalStateException("No active warehouses found");
        }

        log.debug("Calculating distances to all active warehouses");
        Warehouse nearest = activeWarehouses.stream()
                .min(Comparator.comparingDouble(w -> 
                    distanceCalculator.calculateDistance(
                        latitude, longitude, 
                        w.getLatitude(), w.getLongitude()
                    )
                ))
                .orElseThrow(() -> {
                    log.error("Could not determine nearest warehouse from {} candidates", activeWarehouses.size());
                    return new IllegalStateException("Could not determine nearest warehouse");
                });

        double distance = distanceCalculator.calculateDistance(
                latitude, longitude,
                nearest.getLatitude(), nearest.getLongitude()
        );

        log.info("SUCCESS: Nearest warehouse found | id={}, name={}, code={}, distance={} km",
                nearest.getId(), nearest.getName(), nearest.getCode(), Math.round(distance * 100.0) / 100.0);

        return NearestWarehouseResponse.builder()
                .warehouseId(nearest.getId())
                .warehouseName(nearest.getName())
                .warehouseCode(nearest.getCode())
                .address(nearest.getAddress())
                .latitude(nearest.getLatitude())
                .longitude(nearest.getLongitude())
                .distanceKm(Math.round(distance * 100.0) / 100.0)
                .build();
    }

    public List<Warehouse> getAllWarehouses() {
        log.info("START: Fetching all warehouses");
        List<Warehouse> warehouses = warehouseRepository.findAll();
        log.info("SUCCESS: Retrieved {} warehouses", warehouses.size());
        return warehouses;
    }

}
