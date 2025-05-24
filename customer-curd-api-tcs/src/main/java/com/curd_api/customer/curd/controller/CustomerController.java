package com.curd_api.customer.curd.controller;

import com.curd_api.customer.curd.dto.ApiResponse;
import com.curd_api.customer.curd.dto.CustomerRequest;
import com.curd_api.customer.curd.dto.CustomerResponse;
import com.curd_api.customer.curd.service.CustomerService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/customers")
@RequiredArgsConstructor
public class CustomerController {

    private final CustomerService customerService;

    @PostMapping
    public ResponseEntity<ApiResponse<CustomerResponse>> createCustomer(@RequestBody CustomerRequest request) {
        return ResponseEntity
                .status(201)
                .body(customerService.createCustomer(request));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<CustomerResponse>> getCustomerById(@PathVariable UUID id) {
        return ResponseEntity.ok(customerService.getCustomerById(id));
    }

    @GetMapping(params = "name")
    public ResponseEntity<ApiResponse<CustomerResponse>> getCustomerByName(@RequestParam String name) {
        return ResponseEntity.ok(customerService.getCustomerByName(name));
    }

    @GetMapping(params = "email")
    public ResponseEntity<ApiResponse<CustomerResponse>> getCustomerByEmail(@RequestParam String email) {
        return ResponseEntity.ok(customerService.getCustomerByEmail(email));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<CustomerResponse>> updateCustomer(
            @PathVariable UUID id,
            @RequestBody CustomerRequest request) {
        return ResponseEntity.ok(customerService.updateCustomer(id, request));
    }

    @PatchMapping("/{id}")
    public ResponseEntity<ApiResponse<CustomerResponse>> patchCustomer(
            @PathVariable UUID id,
            @RequestBody CustomerRequest request) {
        return ResponseEntity.ok(customerService.patchCustomer(id, request));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<String>> deleteCustomer(@PathVariable UUID id) {
        return ResponseEntity.ok(customerService.deleteCustomer(id));
    }
}
