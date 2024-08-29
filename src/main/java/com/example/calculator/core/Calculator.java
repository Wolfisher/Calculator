package com.example.calculator.core;


import com.example.calculator.CalculationException;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Slf4j
@Getter
public class Calculator {
    private Number result;
    private List<Exception> exceptions;

    public Calculator(Number initialValue) {
        this.result = initialValue;
        this.exceptions = new ArrayList<>();
    }

    public Calculator andThenFailFast(Operation operation, Number value) {
        log.info("Enter into method andThenFailFast");
        if (operation == null) {
            throw new IllegalArgumentException("Operation is not supported for value " + value);
        }
        try {
            this.result = operation.apply(this.result, value);
        } catch (Exception e) {
            this.exceptions = Collections.singletonList(e);
            throw new IllegalStateException("Error occurred during operation: " + e.getMessage(), e);
        }
        return this;
    }

    public Calculator andThenFailLast(Operation operation, Number value) {
        log.info("Enter into method andThenFailLast");
        if (operation == null) {
            throw new IllegalArgumentException("Operation is not supported for value " + value);
        }
        try {
            this.result = operation.apply(this.result, value);
        } catch (Exception e) {
            this.exceptions.add(e); // Continue execution and accumulate exceptions
        }
        return this;
    }

    public Number getResult() throws CalculationException {
        log.info("Enter into method getResult");
        if (!this.exceptions.isEmpty()) {
            log.error("List of exceptions during operation pipeline: {}", this.exceptions);
            throw new CalculationException(this.exceptions);
        } else {
            return this.result;
        }
    }
}