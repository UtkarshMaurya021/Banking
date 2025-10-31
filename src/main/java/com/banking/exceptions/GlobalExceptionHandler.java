package com.banking.exceptions;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.HashMap;
import java.util.Map;

public class GlobalExceptionHandler {

    // Returns a structured error response as a map (can be converted to JSON in controller)
    public static Map<String, Object> handleException(Exception ex) {
        Map<String, Object> errorResponse = new HashMap<>();
        int status = 500;
        String message = "Something went wrong";

        if (ex instanceof ResourceNotFoundException) {
            status = 404;
            message = ex.getMessage();
        } else if (ex instanceof IllegalArgumentException) {
            status = 400;
            message = ex.getMessage();
        }

        errorResponse.put("status", status);
        errorResponse.put("message", message);

        // Optional: include stack trace for logging (not for users)
        StringWriter sw = new StringWriter();
        ex.printStackTrace(new PrintWriter(sw));
        errorResponse.put("stackTrace", sw.toString());

        return errorResponse;
    }
}
