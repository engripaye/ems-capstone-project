package com.ipaye.emscapstoneproject.Exception;


public class EmployeeNotFoundException extends RuntimeException{
    // Constructor with a custom error message
    public EmployeeNotFoundException(String message) {
        super(message);
    }

    // Constructor with a custom error message and a cause
    public EmployeeNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }
}
