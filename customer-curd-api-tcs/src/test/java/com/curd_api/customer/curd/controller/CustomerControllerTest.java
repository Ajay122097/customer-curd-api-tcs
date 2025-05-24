package com.curd_api.customer.curd.controller;

import com.curd_api.customer.curd.dto.ApiResponse;
import com.curd_api.customer.curd.dto.CustomerRequest;
import com.curd_api.customer.curd.dto.CustomerResponse;
import com.curd_api.customer.curd.service.CustomerService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

public class CustomerControllerTest {

    private MockMvc mockMvc;
    private ObjectMapper objectMapper = new ObjectMapper();
    private CustomerService customerService;
    private CustomerController controller;

    private final UUID id = UUID.randomUUID();
    private final CustomerResponse mockResponse = CustomerResponse.builder()
            .id(id)
            .name("John")
            .email("john@example.com")
            .annualSpend(BigDecimal.valueOf(5000))
            .lastPurchaseDate(LocalDateTime.now())
            .tier("GOLD")
            .build();

    @BeforeEach
    void setUp() {
        customerService = Mockito.mock(CustomerService.class);
        controller = new CustomerController(customerService);
        mockMvc = MockMvcBuilders.standaloneSetup(controller).build();
    }

    @Test
    void testCreateCustomer() throws Exception {
        when(customerService.createCustomer(any()))
                .thenReturn(new ApiResponse<>(LocalDateTime.now(), 201, "Customer created", mockResponse));

        mockMvc.perform(post("/customers")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(new CustomerRequest())))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.message").value("Customer created"));
    }

    @Test
    void testGetCustomerById() throws Exception {
        when(customerService.getCustomerById(id))
                .thenReturn(new ApiResponse<>(LocalDateTime.now(), 200, "Customer found", mockResponse));

        mockMvc.perform(get("/customers/{id}", id))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("Customer found"));
    }

    @Test
    void testGetCustomerByName() throws Exception {
        when(customerService.getCustomerByName("John"))
                .thenReturn(new ApiResponse<>(LocalDateTime.now(), 200, "Customer found", mockResponse));

        mockMvc.perform(get("/customers").param("name", "John"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("Customer found"));
    }

    @Test
    void testGetCustomerByEmail() throws Exception {
        when(customerService.getCustomerByEmail("john@example.com"))
                .thenReturn(new ApiResponse<>(LocalDateTime.now(), 200, "Customer found", mockResponse));

        mockMvc.perform(get("/customers").param("email", "john@example.com"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("Customer found"));
    }

    @Test
    void testUpdateCustomer() throws Exception {
        when(customerService.updateCustomer(eq(id), any()))
                .thenReturn(new ApiResponse<>(LocalDateTime.now(), 200, "Customer updated", mockResponse));

        mockMvc.perform(put("/customers/{id}", id)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(new CustomerRequest())))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("Customer updated"));
    }

    @Test
    void testPatchCustomer() throws Exception {
        when(customerService.patchCustomer(eq(id), any()))
                .thenReturn(new ApiResponse<>(LocalDateTime.now(), 200, "Customer patched", mockResponse));

        mockMvc.perform(patch("/customers/{id}", id)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(new CustomerRequest())))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("Customer patched"));
    }

    @Test
    void testDeleteCustomer() throws Exception {
        when(customerService.deleteCustomer(id))
                .thenReturn(new ApiResponse<>(LocalDateTime.now(), 200, "Customer deleted", "Deleted ID: " + id));

        mockMvc.perform(delete("/customers/{id}", id))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("Customer deleted"));
    }
}
