package com.jumbotail.shipping.service;

import com.jumbotail.shipping.dto.request.ShippingCalculateRequest;
import com.jumbotail.shipping.dto.response.ShippingChargeResponse;
import com.jumbotail.shipping.exception.ResourceNotFoundException;
import com.jumbotail.shipping.exception.ShippingCalculationException;
import com.jumbotail.shipping.model.Customer;
import com.jumbotail.shipping.model.Product;
import com.jumbotail.shipping.model.Warehouse;
import com.jumbotail.shipping.model.enums.DeliverySpeed;
import com.jumbotail.shipping.repository.CustomerRepository;
import com.jumbotail.shipping.repository.ProductRepository;
import com.jumbotail.shipping.repository.WarehouseRepository;
import com.jumbotail.shipping.service.shipping.ExpressShippingStrategy;
import com.jumbotail.shipping.service.shipping.ShippingStrategy;
import com.jumbotail.shipping.service.shipping.StandardShippingStrategy;
import com.jumbotail.shipping.util.HaversineDistanceCalculator;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.Comparator;

@Slf4j
@Service
@RequiredArgsConstructor
public class ShippingService {

    private final CustomerRepository customerRepository;
    private final ProductRepository productRepository;
    private final WarehouseRepository warehouseRepository;
    private final HaversineDistanceCalculator distanceCalculator;
    private final StandardShippingStrategy standardShippingStrategy;
    private final ExpressShippingStrategy expressShippingStrategy;

    @Cacheable(value = "shippingCharges", key = "#request.customerId + '_' + #request.productId + '_' + #request.deliverySpeed + '_' + #request.transportMode")
    public ShippingChargeResponse calculateShipping(ShippingCalculateRequest request) {
        log.info("START: Calculating shipping charge | customerId={}, productId={}, deliverySpeed={}, transportMode={}",
                request.getCustomerId(), request.getProductId(), request.getDeliverySpeed(), request.getTransportMode());

        log.debug("Fetching customer with id={}", request.getCustomerId());
        Customer customer = customerRepository.findById(request.getCustomerId())
                .orElseThrow(() -> {
                    log.error("Customer not found with id={}", request.getCustomerId());
                    return new ResourceNotFoundException("Customer", "id", request.getCustomerId());
                });
        log.debug("Customer found: name={}, lat={}, lon={}", customer.getName(), customer.getLatitude(), customer.getLongitude());

        log.debug("Fetching product with id={}", request.getProductId());
        Product product = productRepository.findById(request.getProductId())
                .orElseThrow(() -> {
                    log.error("Product not found with id={}", request.getProductId());
                    return new ResourceNotFoundException("Product", "id", request.getProductId());
                });
        log.debug("Product found: name={}, weight={}, volumetricWeight={}", 
                product.getName(), product.getWeight(), product.getVolumetricWeight());

        if (customer.getLatitude() == null || customer.getLongitude() == null) {
            log.error("Customer location coordinates missing for customerId={}", request.getCustomerId());
            throw new ShippingCalculationException("Customer location coordinates are required");
        }

        log.debug("Finding nearest warehouse for customer location: lat={}, lon={}", 
                customer.getLatitude(), customer.getLongitude());
        Warehouse nearestWarehouse = findNearestWarehouse(customer.getLatitude(), customer.getLongitude());
        log.info("Nearest warehouse selected: id={}, name={}", nearestWarehouse.getId(), nearestWarehouse.getName());

        double distance = distanceCalculator.calculateDistance(
                customer.getLatitude(), customer.getLongitude(),
                nearestWarehouse.getLatitude(), nearestWarehouse.getLongitude()
        );
        log.debug("Distance calculated: {} km from warehouse to customer", Math.round(distance * 100.0) / 100.0);

        ShippingStrategy strategy = getStrategy(request.getDeliverySpeed());
        log.debug("Shipping strategy selected: {}", strategy.getClass().getSimpleName());

        ShippingChargeResponse response = strategy.calculateShipping(
                customer, product, nearestWarehouse, distance, request.getTransportMode()
        );

        log.info("SUCCESS: Shipping calculated | totalCharge={} INR, baseCharge={} INR, expressSurcharge={} INR",
                response.getTotalCharge(), response.getBaseCharge(), response.getExpressSurcharge());

        return response;
    }

    private Warehouse findNearestWarehouse(double customerLat, double customerLon) {
        return warehouseRepository.findByActiveTrue().stream()
                .min(Comparator.comparingDouble(w ->
                    distanceCalculator.calculateDistance(
                        customerLat, customerLon,
                        w.getLatitude(), w.getLongitude()
                    )
                ))
                .orElseThrow(() -> new ShippingCalculationException("No active warehouse available"));
    }

    private ShippingStrategy getStrategy(DeliverySpeed deliverySpeed) {
        return switch (deliverySpeed) {
            case STANDARD -> standardShippingStrategy;
            case EXPRESS -> expressShippingStrategy;
        };
    }

}
