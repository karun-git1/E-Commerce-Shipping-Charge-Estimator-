package com.jumbotail.shipping.controller;

import com.jumbotail.shipping.dto.request.ShippingCalculateRequest;
import com.jumbotail.shipping.dto.response.ApiResponse;
import com.jumbotail.shipping.dto.response.ShippingChargeResponse;
import com.jumbotail.shipping.service.ShippingService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/shipping")
@RequiredArgsConstructor
@Tag(name = "Shipping API", description = "Endpoints for shipping calculations")
public class ShippingController {

    private final ShippingService shippingService;

    @PostMapping("/calculate")
    @Operation(summary = "Calculate shipping charge", description = "Calculate shipping charge based on customer, product, delivery speed and transport mode")
    public ResponseEntity<ApiResponse<ShippingChargeResponse>> calculateShipping(
            @Valid @RequestBody ShippingCalculateRequest request) {
        ShippingChargeResponse response = shippingService.calculateShipping(request);
        return ResponseEntity.ok(ApiResponse.success("Shipping calculated successfully", response));
    }

}
