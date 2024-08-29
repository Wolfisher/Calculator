# Calculator Project

## Overview

This project provides a set of basic arithmetic operations encapsulated within an `Operation` enum. The operations include **addition**, **subtraction**, **multiplication**, and **division**. Each operation is designed to handle `Number` inputs and performs its calculation using `BigDecimal` to ensure precision.

The project also includes a `Calculator` class that allows chaining of these operations with fail-fast and fail-last error handling strategies.

## Assumptions/Design Based on Requirements

- **Extensibility**: The `Calculator` class should allow new operations to be added without requiring changes to its existing code.
  - New operations can be easily added by defining additional enum types in the `Operation` enum class, which can then be utilized during execution.

- **IoC Compatibility**: Ensure the design is compatible with an Inversion of Control (IoC) environment, allowing for external management of dependencies to enable easy testing and swapping of implementations.
  - This project leverages the Spring Boot framework with Maven to manage all dependencies, promoting a modular and testable design.

- **Error Handling**: The solution should handle invalid operations gracefully (e.g., operations not supported by the calculator).
  - Since Java enums are inherently type-safe, undefined enum values cannot be passed directly. If a `null` value is passed, an `IllegalArgumentException` will be thrown for unsupported operations.
  - A global custom exception class, `CalculationException`, is defined to handle errors:
    - It collects all exceptions that occur during the pipeline and wraps them. This design allows us to identify specific exceptions and handle them based on our needs.
    - This custom exception class can be extended to publish events, store records for audit purposes, or generate business reports in the future.

- **Testing**: Unit tests have been written to verify the solution, covering both normal cases and edge cases.
  - JUnit tests are included in the test package, along with a `CalculatorDemo` test class to demonstrate all use cases, including edge cases.

- **Chaining Functionality**: The chaining method should allow users to start with an initial value and apply multiple operations in sequence. For example, starting with 5, the user should be able to add 3, then multiply by 2, and retrieve the final result.
  - This project also employs the **Chain of Responsibility** design pattern.
  - The `Calculator` class allows for chaining operations:
    - `andThenFailFast`: Stops further operations if an error occurs.
    - `andThenFailLast`: Continues with further operations, accumulating any errors.
    - `getResult`: Returns the result of the operations or throws a `CalculationException` to collect all exceptions thrown in the operation pipeline.

## Prerequisites

- Java 17
- Maven (for building the project and running tests)