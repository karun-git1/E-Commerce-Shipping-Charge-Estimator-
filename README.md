# Jumbotail Shipping Estimator

A production-ready shipping cost calculation system built with Spring Boot, featuring real-time distance calculation, volumetric weight computation, and multiple transport modes.

## Features

- **Real-time Distance Calculation**: Uses Haversine formula to calculate accurate distances between warehouse and customer locations
- **Volumetric Weight Support**: Calculates chargeable weight based on both actual and volumetric weight
- **Multiple Transport Modes**: Supports Road, Rail, and Air transport with different pricing
- **Delivery Speed Options**: Standard and Express delivery with appropriate surcharges
- **Caching**: Caffeine caching for improved performance
- **RESTful API**: Clean API design with proper error handling
- **Interactive UI**: Simple HTML frontend for testing

## Technology Stack

- Java 17
- Spring Boot 3.2.0
- Spring Data JPA
- H2 Database (in-memory)
- Caffeine Cache
- Lombok
- Swagger/OpenAPI 3.0

## Getting Started

### Prerequisites

- Java 17 or higher
- Maven 3.6+ (or use included Maven wrapper)

### Running the Application

```bash
# Using Maven wrapper
./mvnw spring-boot:run

# Or using Maven
mvn spring-boot:run
```

The application will start on port 8081.

### Access Points

- **Frontend UI**: http://localhost:8081
- **Swagger API Docs**: http://localhost:8081/swagger-ui.html
- **H2 Console**: http://localhost:8081/h2-console
  - JDBC URL: `jdbc:h2:mem:jumbotaildb`
  - Username: `sa`
  - Password: (leave empty)

## API Endpoints

### Shipping API

| Method | Endpoint | Description |
|--------|----------|-------------|
| POST | `/api/shipping/calculate` | Calculate shipping charge |

### Warehouse API

| Method | Endpoint | Description |
|--------|----------|-------------|
| GET | `/api/warehouses` | Get all warehouses |
| GET | `/api/warehouses/nearest` | Find nearest warehouse by coordinates |

## Architecture Overview

The project follows a layered architecture:

```
Controller → Service → Repository → Database
```

### Design Patterns Used

1. **Strategy Pattern**:
   - [ShippingStrategy](src/main/java/com/jumbotail/shipping/service/shipping/ShippingStrategy.java) interface
   - [StandardShippingStrategy](src/main/java/com/jumbotail/shipping/service/shipping/StandardShippingStrategy.java) - Standard delivery calculation
   - [ExpressShippingStrategy](src/main/java/com/jumbotail/shipping/service/shipping/ExpressShippingStrategy.java) - Express delivery with surcharge

2. **Enum-Based Transport Selection**:
   - [TransportMode](src/main/java/com/jumbotail/shipping/model/enums/TransportMode.java) with embedded pricing logic
   - Each mode has base rate, volumetric divisor, and minimum charge

3. **Repository Pattern**:
   - Spring Data JPA repositories for data access
   - Clean separation between business logic and data layer

4. **DTO Pattern**:
   - Separate request/response objects for API contracts
   - [ShippingCalculateRequest](src/main/java/com/jumbotail/shipping/dto/request/ShippingCalculateRequest.java)
   - [ShippingChargeResponse](src/main/java/com/jumbotail/shipping/dto/response/ShippingChargeResponse.java)

5. **Global Exception Handling**:
   - [GlobalExceptionHandler](src/main/java/com/jumbotail/shipping/exception/GlobalExceptionHandler.java)
   - Consistent error responses with ApiResponse wrapper

## Assumptions

- Transport mode is selected by the user (not auto-selected based on distance)
- Volumetric weight formula = (L × W × H) / 5000 (industry standard)
- Express delivery adds 50% surcharge on base charge
- Minimum charges apply per transport mode: Road ₹100, Rail ₹500, Air ₹50
- Only active warehouses are considered for shipping
- In-memory H2 database used for demo purposes (can be replaced with any RDBMS)
- Distance calculated using Haversine formula (straight-line, not road distance)
- Coordinates are in decimal degrees (latitude/longitude)
- All monetary values are in Indian Rupees (INR)
- Weight is measured in kilograms (kg)
- Dimensions are measured in centimeters (cm)

## Shipping Calculation Formula

```
Chargeable Weight = Max(Actual Weight, Volumetric Weight)
Base Charge = Distance (km) × Base Rate × Chargeable Weight
Total Charge = Base Charge + Express Surcharge (if applicable)
```

### Transport Mode Rates

| Mode | Base Rate/km | Min Charge |
|------|--------------|------------|
| Road | ₹5.0 | ₹100 |
| Rail | ₹3.0 | ₹500 |
| Air | ₹15.0 | ₹50 |

## Example Request

```bash
curl -X POST http://localhost:8081/api/shipping/calculate \
  -H "Content-Type: application/json" \
  -d '{
    "customerId": 1,
    "productId": 1,
    "deliverySpeed": "STANDARD",
    "transportMode": "ROAD"
  }'
```

## Example Response

```json
{
  "success": true,
  "message": "Shipping calculated successfully",
  "data": {
    "customerId": 1,
    "customerName": "Rajesh Kumar",
    "productId": 1,
    "productName": "Rice Bag 25kg",
    "warehouseId": 1,
    "warehouseName": "Bangalore Central Warehouse",
    "distanceKm": 15.23,
    "actualWeight": 25.0,
    "volumetricWeight": 9.6,
    "chargeableWeight": 25.0,
    "deliverySpeed": "STANDARD",
    "transportMode": "ROAD",
    "baseCharge": 1903.75,
    "expressSurcharge": 0.0,
    "totalCharge": 1903.75,
    "estimatedDeliveryTime": "3-5 business days"
  },
  "timestamp": "2026-03-02T10:30:00"
}
```

## Logging

The application includes comprehensive production-style logging:

- **INFO level**: Key operations like shipping calculation start/end
- **DEBUG level**: Detailed steps like distance calculation, warehouse selection
- **ERROR level**: Failure scenarios with context

Example log output:
```
INFO  - START: Calculating shipping charge | customerId=1, productId=1, deliverySpeed=STANDARD, transportMode=ROAD
DEBUG - Distance calculated: 15.23 km from warehouse to customer
INFO  - SUCCESS: Shipping calculated | totalCharge=1903.75 INR, baseCharge=1903.75 INR, expressSurcharge=0.0 INR
```

## Edge Case Handling

The application handles the following edge cases:

| Scenario | Response |
|----------|----------|
| Invalid customer ID | 404 Not Found with error message |
| Invalid product ID | 404 Not Found with error message |
| Missing customer coordinates | 400 Bad Request with error message |
| No active warehouses | 500 Internal Server Error with error message |
| Invalid delivery speed | Handled by enum validation |
| Invalid transport mode | Handled by enum validation |

## Future Enhancements

- Integration with external mapping APIs for actual road distance
- Multi-currency support
- Real-time tracking integration
- Email/SMS notifications
- Admin dashboard for warehouse management
- Rate limiting and API throttling

## License

This project is for demonstration purposes.
