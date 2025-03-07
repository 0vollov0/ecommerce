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
import portfolio.ecommerce.order.dto.CreateProductDto;
import portfolio.ecommerce.order.dto.RequestPagingDto;
import portfolio.ecommerce.order.dto.UpdateProductDto;
import portfolio.ecommerce.order.entity.Product;
import portfolio.ecommerce.order.service.ProductService;

import java.util.Optional;

@Validated
@RestController()
@RequestMapping("/products")
@RequiredArgsConstructor
@Tag(name = "products")
public class ProductController {
    private final ProductService productService;

    @GetMapping
    public ResponseEntity<Page<Product>> getProducts(@ModelAttribute RequestPagingDto dto) {
        return ResponseEntity.ok().body(productService.find(dto));
    }

    @GetMapping("/{id}")
    public ResponseEntity<Optional<Product>> getProduct(@PathVariable @Min(1) Long id) {
        return ResponseEntity.ok().body(productService.findById(id));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteProduct(@PathVariable @Min(1) Long id) {
        this.productService.delete(id);
        return ResponseEntity.ok().build();
    }

    @Operation(
            summary = "Create product",
            responses = {
                    @ApiResponse(responseCode = "201")
            }
    )
    @PostMapping
    public ResponseEntity<Product> createProduct(@RequestBody @Validated CreateProductDto dto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(this.productService.create(dto));
    }

    @PatchMapping("/{id}")
    public ResponseEntity<Product> updateProduct(@PathVariable @Min(1) Long id, @RequestBody @Validated UpdateProductDto dto) {
        return ResponseEntity.ok().body(productService.update(id, dto));
    }
}
