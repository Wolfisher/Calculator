package com.example.calculator.core;

import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;

class OperationTest {

    @Test
    void testAddOperation() {
        BigDecimal result = (BigDecimal) Operation.ADD.apply(new BigDecimal("10"), new BigDecimal("5"));
        assertEquals(new BigDecimal("15"), result);

        result = (BigDecimal) Operation.ADD.apply(new BigDecimal("-10"), new BigDecimal("5"));
        assertEquals(new BigDecimal("-5"), result);

        result = (BigDecimal) Operation.ADD.apply(new BigDecimal("10.5"), new BigDecimal("0.5"));
        assertEquals(new BigDecimal("11.0"), result);
    }

    @Test
    void testSubtractOperation() {
        BigDecimal result = (BigDecimal) Operation.SUBTRACT.apply(new BigDecimal("10"), new BigDecimal("5"));
        assertEquals(new BigDecimal("5"), result);

        result = (BigDecimal) Operation.SUBTRACT.apply(new BigDecimal("-10"), new BigDecimal("5"));
        assertEquals(new BigDecimal("-15"), result);

        result = (BigDecimal) Operation.SUBTRACT.apply(new BigDecimal("10.5"), new BigDecimal("0.5"));
        assertEquals(new BigDecimal("10.0"), result);
    }

    @Test
    void testMultiplyOperation() {
        BigDecimal result = (BigDecimal) Operation.MULTIPLY.apply(new BigDecimal("10"), new BigDecimal("5"));
        assertEquals(new BigDecimal("50"), result);

        result = (BigDecimal) Operation.MULTIPLY.apply(new BigDecimal("-10"), new BigDecimal("5"));
        assertEquals(new BigDecimal("-50"), result);

        result = (BigDecimal) Operation.MULTIPLY.apply(new BigDecimal("10.5"), new BigDecimal("0.5"));
        assertEquals(new BigDecimal("5.25"), result);
    }

    @Test
    void testDivideOperation() {
        BigDecimal result = (BigDecimal) Operation.DIVIDE.apply(new BigDecimal("10"), new BigDecimal("5"));
        assertEquals(new BigDecimal("2"), result);

        result = (BigDecimal) Operation.DIVIDE.apply(new BigDecimal("10.5"), new BigDecimal("0.5"));
        assertEquals(new BigDecimal("21.0"), result);

        ArithmeticException exception = assertThrows(ArithmeticException.class, () -> {
            Operation.DIVIDE.apply(new BigDecimal("10"), BigDecimal.ZERO);
        });
        assertEquals("Cannot divide by zero", exception.getMessage());
    }

    @Test
    void testModulusOperation() {
        BigDecimal result = (BigDecimal) Operation.MODULUS.apply(new BigDecimal("10"), new BigDecimal("3"));
        assertEquals(new BigDecimal("1"), result);

        result = (BigDecimal) Operation.MODULUS.apply(new BigDecimal("10.5"), new BigDecimal("0.5"));
        assertEquals(new BigDecimal("0.0"), result);

        result = (BigDecimal) Operation.MODULUS.apply(new BigDecimal("10"), new BigDecimal("2"));
        assertEquals(new BigDecimal("0"), result);

        ArithmeticException exception = assertThrows(ArithmeticException.class, () -> {
            Operation.MODULUS.apply(new BigDecimal("10"), BigDecimal.ZERO);
        });
        assertEquals("Division by zero", exception.getMessage());
    }
}