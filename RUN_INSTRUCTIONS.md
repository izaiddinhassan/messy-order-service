
# How to Run and Test

## Prerequisites
- **JDK 17+** (It seems you have this installed)
- **Maven**: Since `mvn` is not in your path and the project doesn't have a wrapper, you need to either:
  1. **Install Maven**: [Download Apache Maven](https://maven.apache.org/download.cgi), extract it, and add the `bin` folder to your PATH environment variable.
  2. **Use an IDE**: Open the project in **IntelliJ IDEA** or **Eclipse** or **VS Code** (with Java extension). These IDEs usually have bundled Maven and can run the project "Run" button.

## 1. Run the Application
### Option A: Via Command Line (after installing Maven)
```powershell
mvn spring-boot:run
```

### Option B: Via IDE
- Open `OrderServiceApplication.java`
- Click the "Run" icon (green arrow) next to the `main` method.

The application will start on port **8082**.

## 2. Test Endpoints

Run these commands in PowerShell to test.

### Create Order (POST)
**URL**: `http://localhost:8082/api/orders`

```powershell
$body = @{
    customerType = "VIP"
    items = @(
        @{ productId = 1; quantity = 2 }
    )
} | ConvertTo-Json -Depth 5

Invoke-RestMethod -Uri "http://localhost:8082/api/orders" -Method Post -Body $body -ContentType "application/json"
```

### Get Order (GET)
**URL**: `http://localhost:8082/api/orders/{id}`

```powershell
Invoke-RestMethod -Uri "http://localhost:8082/api/orders/1" -Method Get
```

### Popular Products (GET)
**URL**: `http://localhost:8082/api/orders/analytics/popular`

```powershell
Invoke-RestMethod -Uri "http://localhost:8082/api/orders/analytics/popular" -Method Get
```
