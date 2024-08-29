package com.example.calculator.demo;

import com.example.calculator.CalculationException;
import com.example.calculator.core.Calculator;
import com.example.calculator.core.Operation;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;

class CalculatorDemo {

    @Test
    void testSimpleCalculation() throws CalculationException {
        Calculator calculator = new Calculator(new BigDecimal("10"));
        Number result = calculator
                .andThenFailFast(Operation.ADD, new BigDecimal("5"))
                .andThenFailFast(Operation.SUBTRACT, new BigDecimal("3"))
                .andThenFailFast(Operation.MULTIPLY, new BigDecimal("2"))
                .andThenFailFast(Operation.DIVIDE, new BigDecimal("6"))
                .getResult();

        assertEquals(new BigDecimal("4"), result);
    }

    @Test
    void testFailFastWithDivisionByZero() {
        Calculator calculator = new Calculator(new BigDecimal("10"));

        IllegalStateException exception = assertThrows(IllegalStateException.class, () -> {
            calculator
                    .andThenFailFast(Operation.ADD, new BigDecimal("5"))
                    .andThenFailFast(Operation.DIVIDE, BigDecimal.ZERO)
                    .andThenFailFast(Operation.SUBTRACT, new BigDecimal("2")); // This should not execute
        });

        assertTrue(exception.getMessage().contains("Cannot divide by zero"));
        assertNotNull(calculator.getExceptions());
        assertEquals(1, calculator.getExceptions().size());
        assertEquals(ArithmeticException.class, calculator.getExceptions().get(0).getClass());
    }

    @Test
    void testFailLastWithDivisionByZero() {
        Calculator calculator = new Calculator(new BigDecimal("10"));

        try {
            calculator
                    .andThenFailLast(Operation.ADD, new BigDecimal("5"))
                    .andThenFailLast(Operation.DIVIDE, BigDecimal.ZERO) // This should be caught and continue
                    .andThenFailLast(Operation.SUBTRACT, new BigDecimal("2"))
                    .andThenFailLast(Operation.MULTIPLY, new BigDecimal("3"));
            calculator.getResult();
            fail("Expected CalculationException to be thrown");
        } catch (CalculationException e) {
            assertNotNull(e.getMessages());
            assertEquals(1, e.getMessages().size());
            assertEquals("Cannot divide by zero", e.getMessages().get(0));
        }
    }

    @Test
    void testFailLastWithMultipleExceptions() {
        Calculator calculator = new Calculator(new BigDecimal("20"));

        try {
            calculator
                    .andThenFailLast(Operation.SUBTRACT, new BigDecimal("10"))
                    .andThenFailLast(Operation.DIVIDE, BigDecimal.ZERO) // First exception (division by zero)
                    .andThenFailLast(Operation.MODULUS, BigDecimal.ZERO) // Second exception (modulus by zero)
                    .andThenFailLast(Operation.MULTIPLY, new BigDecimal("5"));
            calculator.getResult();
            fail("Expected CalculationException to be thrown");
        } catch (CalculationException e) {
            assertNotNull(e.getMessages());
            assertEquals(2, e.getMessages().size());
        }
    }

    @Test
    void testOperationWithNegativeNumbers() throws CalculationException {
        Calculator calculator = new Calculator(new BigDecimal("10"));
        Number result = calculator
                .andThenFailFast(Operation.ADD, new BigDecimal("-5"))
                .andThenFailFast(Operation.MULTIPLY, new BigDecimal("-2"))
                .andThenFailFast(Operation.SUBTRACT, new BigDecimal("5"))
                .getResult();

        assertEquals(new BigDecimal("-15"), result);
    }

    @Test
    void testOperationWithDecimals() throws CalculationException {
        Calculator calculator = new Calculator(new BigDecimal("10.5"));
        Number result = calculator
                .andThenFailFast(Operation.ADD, new BigDecimal("5.3"))
                .andThenFailFast(Operation.MULTIPLY, new BigDecimal("2"))
                .andThenFailFast(Operation.DIVIDE, new BigDecimal("3"))
                .getResult();

        assertEquals(new BigDecimal("10.5"), ((BigDecimal) result).stripTrailingZeros());
    }

    @Test
    void testModulusWithFailFast() throws CalculationException {
        Calculator calculator = new Calculator(new BigDecimal("10"));
        Number result = calculator
                .andThenFailFast(Operation.MODULUS, new BigDecimal("3"))
                .andThenFailFast(Operation.ADD, new BigDecimal("2"))
                .getResult();

        assertEquals(new BigDecimal("3"), result);
    }

    @Test
    void testFailFastWithNullOperation() {
        Calculator calculator = new Calculator(new BigDecimal("10"));

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
                () -> calculator.andThenFailFast(null, new BigDecimal("5")));

        assertTrue(exception.getMessage().contains("Operation is not supported for value 5"));
    }

    @Test
    void testFailLastWithNullOperation() {
        Calculator calculator = new Calculator(new BigDecimal("10"));

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
                () -> calculator.andThenFailLast(null, new BigDecimal("5")));

        assertTrue(exception.getMessage().contains("Operation is not supported for value 5"));
    }
}