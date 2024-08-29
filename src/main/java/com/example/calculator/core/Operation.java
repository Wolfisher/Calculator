package com.example.calculator.core;


import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.function.BiFunction;

public enum Operation {
    ADD(
            (num1, num2) -> toBigDecimal(num1).add(toBigDecimal(num2))
    ),
    SUBTRACT(
            (num1, num2) -> toBigDecimal(num1).subtract(toBigDecimal(num2))
    ),
    MULTIPLY(
            (num1, num2) -> toBigDecimal(num1).multiply(toBigDecimal(num2))
    ),
    DIVIDE(
            (num1, num2) -> {
                if (toBigDecimal(num2).compareTo(BigDecimal.ZERO) == 0) {
                    throw new ArithmeticException("Cannot divide by zero");
                }
                return toBigDecimal(num1).divide(toBigDecimal(num2), RoundingMode.HALF_UP);
            }
    ),
    MODULUS(
            (num1, num2) -> toBigDecimal(num1).remainder(toBigDecimal(num2))
    );

    private final BiFunction<Number, Number, Number> operation;

    Operation(BiFunction<Number, Number, Number> operation) {
        this.operation = operation;
    }

    public Number apply(Number num1, Number num2) {
        return operation.apply(num1, num2);
    }

    private static BigDecimal toBigDecimal(Number number) {
        if (number instanceof BigDecimal) {
            return (BigDecimal) number;
        } else {
            return new BigDecimal(number.toString());
        }
    }
}