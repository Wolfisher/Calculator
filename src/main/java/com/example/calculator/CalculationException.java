package com.example.calculator;

import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

@Getter
public class CalculationException extends Exception {
    private final List<String> messages;

    public CalculationException(List<Exception> exceptions) {
        this.messages = new ArrayList<>();
        for (Exception exception : exceptions) {
            this.messages.add(exception.getMessage());
        }
    }
}
