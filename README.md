# Order Discount Optimizer

## Task Summary

1. Each order has a set of applicable promotions based on payment methods.
2. If the entire order is paid with a promotional payment method (e.g., bank card), a specific percentage discount is applied.
3. If at least 10% of the order (before discount) is paid using loyalty points, a 10% discount is applied to the whole order.
4. If the order is fully paid with loyalty points, the discount defined for the "PUNKTY" method is applied instead.

## Goal

Implement an algorithm that selects the best payment method for each order to maximize the total discount, considering method limits and discount rules.

## Project Structure

```
src/
├── pl/sankouski/
│   ├── Main.java
│   ├── Order.java
│   ├── PaymentMethod.java
│   ├── PaymentOptimization.java
│   └── Result.java
```

## How to Run

Requires Java 21. To run the application:

```bash
java -jar /path/to/app.jar /path/to/orders.json /path/to/paymentmethods.json
```

## Expected Output

The program prints the total amount spent, broken down by payment method, to the standard output.
