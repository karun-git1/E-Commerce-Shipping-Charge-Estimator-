package com.jumbotail.shipping.service.shipping;

import com.jumbotail.shipping.dto.response.ShippingChargeResponse;
import com.jumbotail.shipping.model.Customer;
import com.jumbotail.shipping.model.Product;
import com.jumbotail.shipping.model.Warehouse;
import com.jumbotail.shipping.model.enums.TransportMode;

public interface ShippingStrategy {

    ShippingChargeResponse calculateShipping(
            Customer customer,
            Product product,
            Warehouse warehouse,
            double distance,
            TransportMode transportMode
    );

}
