package com.jumbotail.shipping.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class NearestWarehouseResponse {

    private Long warehouseId;
    private String warehouseName;
    private String warehouseCode;
    private String address;
    private Double latitude;
    private Double longitude;
    private Double distanceKm;

}
