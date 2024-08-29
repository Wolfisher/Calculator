package com.example.calculator.core;

import com.example.calculator.CalculationException;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;

class CalculatorTest {

    @Test
    void testAndThenFailFast_successfulChain() throws CalculationException {
        Calculator calculator = new Calculator(new BigDecimal("10"));
        Number result = calculator
                .andThenFailFast(Operation.ADD, new BigDecimal("5"))
                .andThenFailFast(Operation.MULTIPLY, new BigDecimal("2"))
                .getResult();

        assertEquals(new BigDecimal("30"), result);
    }

    @Test
    void testAndThenFailLast_successfulChain() throws CalculationException {
        Calculator calculator = new Calculator(new BigDecimal("10"));
        Number result = calculator
                .andThenFailLast(Operation.ADD, new BigDecimal("5"))
                .andThenFailLast(Operation.MULTIPLY, new BigDecimal("2"))
                .getResult();

        assertEquals(new BigDecimal("30"), result);
    }

    @Test
    void testAndThenFailFast_withException() {
        Calculator calculator = new Calculator(new BigDecimal("10"));

        IllegalStateException exception = assertThrows(IllegalStateException.class, () -> {
            calculator
                    .andThenFailFast(Operation.ADD, new BigDecimal("5"))
                    .andThenFailFast(Operation.DIVIDE, BigDecimal.ZERO) // This should throw an exception
                    .andThenFailFast(Operation.SUBTRACT, new BigDecimal("3")); // This should not execute
        });

        assertTrue(exception.getMessage().contains("Error occurred during operation"));
        assertNotNull(calculator.getExceptions());
        assertEquals(1, calculator.getExceptions().size());
        assertEquals(ArithmeticException.class, calculator.getExceptions().get(0).getClass());
    }

    @Test
    void testAndThenFailLast_withException() {
        Calculator calculator = new Calculator(new BigDecimal("10"));

        try {
            calculator
                    .andThenFailLast(Operation.ADD, new BigDecimal("5"))
                    .andThenFailLast(Operation.DIVIDE, BigDecimal.ZERO)
                    .andThenFailLast(Operation.SUBTRACT, new BigDecimal("3"));
            calculator.getResult();
            fail("Expected CalculationException to be thrown");
        } catch (CalculationException e) {
            assertNotNull(e.getMessages());
            assertEquals(1, e.getMessages().size());
        }
    }

    @Test
    void testAndThenFailLast_multipleExceptions() {
        Calculator calculator = new Calculator(new BigDecimal("10"));

        try {
            calculator
                    .andThenFailLast(Operation.ADD, new BigDecimal("5"))
                    .andThenFailLast(Operation.DIVIDE, BigDecimal.ZERO) // This should catch the exception and continue
                    .andThenFailLast(Operation.SUBTRACT, new BigDecimal("3"))
                    .andThenFailLast(Operation.DIVIDE, BigDecimal.ZERO); // This should catch another exception

            calculator.getResult();
            fail("Expected CalculationException to be thrown");
        } catch (CalculationException e) {
            assertNotNull(e.getMessages());
            assertEquals(2, e.getMessages().size());
        }
    }

    @Test
    void test_withNullOperation() {
        Calculator calculator = new Calculator(new BigDecimal("10"));

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
                () -> calculator.andThenFailFast(null, new BigDecimal("5")));

        assertTrue(exception.getMessage().contains("Operation is not supported for value 5"));
    }
}