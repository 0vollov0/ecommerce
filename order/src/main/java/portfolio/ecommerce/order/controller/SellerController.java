package portfolio.ecommerce.order.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import portfolio.ecommerce.order.dto.CreateSellerDto;
import portfolio.ecommerce.order.dto.RequestPagingDto;
import portfolio.ecommerce.order.dto.UpdateSellerDto;
import portfolio.ecommerce.order.entity.Seller;
import portfolio.ecommerce.order.response.ApiErrorResponses;
import portfolio.ecommerce.order.service.SellerService;

@Validated
@RestController()
@RequestMapping("/sellers")
@RequiredArgsConstructor
@Tag(name = "sellers")
public class SellerController {
    private final SellerService customerService;

    @ApiErrorResponses
    @GetMapping
    public ResponseEntity<Page<Seller>> getSellers(@ModelAttribute RequestPagingDto dto) {
        return ResponseEntity.ok().body(customerService.find(dto));
    }

    @ApiErrorResponses
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteSeller(@PathVariable @Min(1) Long id) {
        this.customerService.delete(id);
        return ResponseEntity.ok().build();
    }

    @Operation(
            summary = "Create customer",
            responses = {
                    @ApiResponse(responseCode = "201")
            }
    )
    @ApiErrorResponses
    @PostMapping
    public ResponseEntity<Seller> createSeller(@RequestBody @Valid CreateSellerDto dto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(this.customerService.create(dto));
    }

    @ApiErrorResponses
    @PatchMapping("/{id}")
    public ResponseEntity<Seller> updateSeller(@PathVariable @Min(1) Long id, @RequestBody @Valid UpdateSellerDto dto) {
        return ResponseEntity.ok().body(customerService.update(id, dto));
    }
}
