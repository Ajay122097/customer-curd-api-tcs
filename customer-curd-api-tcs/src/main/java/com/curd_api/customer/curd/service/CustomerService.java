package com.curd_api.customer.curd.service;

import com.curd_api.customer.curd.dto.ApiResponse;
import com.curd_api.customer.curd.dto.CustomerRequest;
import com.curd_api.customer.curd.dto.CustomerResponse;
import com.curd_api.customer.curd.entity.Customer;
import com.curd_api.customer.curd.exception.CustomerNotFoundException;
import com.curd_api.customer.curd.repository.CustomerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import static com.curd_api.customer.curd.constant.CustomerTier.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Service
public class CustomerService {

    @Autowired
    private CustomerRepository customerRepository;

    public ApiResponse<CustomerResponse> createCustomer(CustomerRequest request) {
        Customer customer = new Customer();
        customer.setName(request.getName());
        customer.setEmail(request.getEmail());
        customer.setAnnualSpend(request.getAnnualSpend());
        customer.setLastPurchaseDate(request.getLastPurchaseDate());
        return buildResponse(HttpStatus.CREATED, "Customer created successfully", mapToResponse(customerRepository.save(customer)));
    }

    public ApiResponse<CustomerResponse> getCustomerById(UUID id) {
        Customer customer = customerRepository.findById(id)
                .orElseThrow(() -> new CustomerNotFoundException("Customer not found with ID: " + id));
        return buildResponse(HttpStatus.OK, "Customer fetched successfully", mapToResponse(customer));
    }

    public ApiResponse<CustomerResponse> getCustomerByName(String name) {
        Customer customer = customerRepository.findByName(name)
                .orElseThrow(() -> new CustomerNotFoundException("Customer not found with name: " + name));
        return buildResponse(HttpStatus.OK, "Customer fetched successfully", mapToResponse(customer));
    }

    public ApiResponse<CustomerResponse> getCustomerByEmail(String email) {
        Customer customer = customerRepository.findByEmail(email)
                .orElseThrow(() -> new CustomerNotFoundException("Customer not found with email: " + email));
        return buildResponse(HttpStatus.OK, "Customer fetched successfully", mapToResponse(customer));
    }

    public ApiResponse<CustomerResponse> updateCustomer(UUID id, CustomerRequest request) {
        Customer customer = customerRepository.findById(id)
                .orElseThrow(() -> new CustomerNotFoundException("Customer not found with ID: " + id));
        customer.setName(request.getName());
        customer.setEmail(request.getEmail());
        customer.setAnnualSpend(request.getAnnualSpend());
        customer.setLastPurchaseDate(request.getLastPurchaseDate());
        return buildResponse(HttpStatus.OK, "Customer updated successfully", mapToResponse(customerRepository.save(customer)));
    }

    public ApiResponse<String> deleteCustomer(UUID id) {
        Customer customer = customerRepository.findById(id)
                .orElseThrow(() -> new CustomerNotFoundException("Customer not found with ID: " + id));
        customerRepository.delete(customer);
        return buildResponse(HttpStatus.OK, "Customer deleted successfully", "Deleted ID: " + id);
    }

    public ApiResponse<CustomerResponse> patchCustomer(UUID id, CustomerRequest request) {
        Customer customer = customerRepository.findById(id)
                .orElseThrow(() -> new CustomerNotFoundException("Customer not found with ID: " + id));

        updateIfPresent(request.getName(), customer::setName);
        updateIfPresent(request.getEmail(), customer::setEmail);
        updateIfPresent(request.getAnnualSpend(), customer::setAnnualSpend);
        updateIfPresent(request.getLastPurchaseDate(), customer::setLastPurchaseDate);

        return buildResponse(HttpStatus.OK, "Customer partially updated", mapToResponse(customerRepository.save(customer)));
    }

    private <T> void updateIfPresent(T value, java.util.function.Consumer<T> setter) {
        if (value != null) {
            setter.accept(value);
        }
    }

    private CustomerResponse mapToResponse(Customer customer) {
        return CustomerResponse.builder()
                .id(customer.getId())
                .name(customer.getName())
                .email(customer.getEmail())
                .annualSpend(customer.getAnnualSpend())
                .lastPurchaseDate(customer.getLastPurchaseDate())
                .tier(calculateTier(customer.getAnnualSpend(), customer.getLastPurchaseDate()))
                .build();
    }

    private String calculateTier(BigDecimal spend, LocalDateTime lastPurchase) {
        if (spend == null) return SILVER;
        if (spend.compareTo(new BigDecimal("10000")) >= 0 && lastPurchase != null && lastPurchase.isAfter(LocalDateTime.now().minusMonths(6))) {
            return PLATINUM;
        } else if (spend.compareTo(new BigDecimal("1000")) >= 0 && lastPurchase != null && lastPurchase.isAfter(LocalDateTime.now().minusMonths(12))) {
            return GOLD;
        } else {
            return SILVER;
        }
    }

    private <T> ApiResponse<T> buildResponse(HttpStatus status, String message, T data) {
        return ApiResponse.<T>builder()
                .timestamp(LocalDateTime.now())
                .status(status.value())
                .message(message)
                .data(data)
                .build();
    }
}
