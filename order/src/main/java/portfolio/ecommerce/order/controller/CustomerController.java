package portfolio.ecommerce.order.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.constraints.Min;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import portfolio.ecommerce.order.dto.CreateCustomerDto;
import portfolio.ecommerce.order.dto.RequestPagingDto;
import portfolio.ecommerce.order.dto.UpdateCustomerDto;
import portfolio.ecommerce.order.entity.Customer;
import portfolio.ecommerce.order.service.CustomerService;

@Validated
@RestController()
@RequestMapping("/customers")
@RequiredArgsConstructor
@Tag(name = "customers")
public class CustomerController {
    private final CustomerService customerService;

    @GetMapping
    public ResponseEntity<Page<Customer>> getCustomers(@ModelAttribute RequestPagingDto dto) {
        return ResponseEntity.ok().body(customerService.find(dto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCustomer(@PathVariable @Min(1) Long id) {
        this.customerService.delete(id);
        return ResponseEntity.ok().build();
    }

    @Operation(
            summary = "Create customer",
            responses = {
                    @ApiResponse(responseCode = "201")
            }
    )
    @PostMapping
    public ResponseEntity<Customer> createCustomer(@RequestBody @Validated CreateCustomerDto dto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(this.customerService.create(dto));
    }

    @PatchMapping("/{id}")
    public ResponseEntity<Customer> updateCustomer(@PathVariable @Min(1) Long id, @RequestBody @Validated UpdateCustomerDto dto) {
        return ResponseEntity.ok().body(customerService.update(id, dto));
    }
}
