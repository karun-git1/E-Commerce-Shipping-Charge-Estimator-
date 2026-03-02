package com.jumbotail.shipping.controller;

import com.jumbotail.shipping.dto.response.ApiResponse;
import com.jumbotail.shipping.dto.response.NearestWarehouseResponse;
import com.jumbotail.shipping.model.Warehouse;
import com.jumbotail.shipping.service.WarehouseService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/warehouses")
@RequiredArgsConstructor
@Tag(name = "Warehouse API", description = "Endpoints for warehouse operations")
public class WarehouseController {

    private final WarehouseService warehouseService;

    @GetMapping("/nearest")
    @Operation(summary = "Find nearest warehouse", description = "Find the nearest active warehouse to given coordinates")
    public ResponseEntity<ApiResponse<NearestWarehouseResponse>> findNearestWarehouse(
            @RequestParam double latitude,
            @RequestParam double longitude) {
        NearestWarehouseResponse response = warehouseService.findNearestWarehouse(latitude, longitude);
        return ResponseEntity.ok(ApiResponse.success("Nearest warehouse found", response));
    }

    @GetMapping
    @Operation(summary = "Get all warehouses", description = "Retrieve all warehouses")
    public ResponseEntity<ApiResponse<List<Warehouse>>> getAllWarehouses() {
        List<Warehouse> warehouses = warehouseService.getAllWarehouses();
        return ResponseEntity.ok(ApiResponse.success("Warehouses retrieved", warehouses));
    }

}
