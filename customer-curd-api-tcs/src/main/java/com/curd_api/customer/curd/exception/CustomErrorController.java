package com.curd_api.customer.curd.exception;

import org.springframework.boot.web.error.ErrorAttributeOptions;
import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.boot.web.servlet.error.ErrorAttributes;
import org.springframework.web.context.request.WebRequest;

import java.time.LocalDateTime;
import java.util.LinkedHashMap;
import java.util.Map;

@RestController
@RequestMapping("/error")
public class CustomErrorController implements ErrorController {

    private final ErrorAttributes errorAttributes;

    public CustomErrorController(ErrorAttributes errorAttributes) {
        this.errorAttributes = errorAttributes;
    }

    @GetMapping
    public ResponseEntity<?> handleError(WebRequest webRequest) {
        Map<String, Object> defaultErrorAttributes =
                errorAttributes.getErrorAttributes(webRequest, ErrorAttributeOptions.of(
                        ErrorAttributeOptions.Include.MESSAGE,
                        ErrorAttributeOptions.Include.EXCEPTION,
                        ErrorAttributeOptions.Include.BINDING_ERRORS
                ));

        int status = (int) defaultErrorAttributes.getOrDefault("status", 500);
        String error = (String) defaultErrorAttributes.getOrDefault("error", "Internal Server Error");
        String message = (String) defaultErrorAttributes.getOrDefault("message", "Unexpected error");
        String path = (String) defaultErrorAttributes.getOrDefault("path", "N/A");

        Map<String, Object> response = new LinkedHashMap<>();
        response.put("timestamp", LocalDateTime.now());
        response.put("status", status);
        response.put("error", error);
        response.put("message", message);
        response.put("path", path);

        return ResponseEntity.status(status).body(response);
    }
}
