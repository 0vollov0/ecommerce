package portfolio.ecommerce.main.controller;

import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;
import portfolio.ecommerce.main.dto.CreateSellerDTO;
import portfolio.ecommerce.main.entity.Seller;
import portfolio.ecommerce.main.service.SellerService;

@RestController
public class SellerController {
    @Autowired
    private SellerService sellerService;

    @PostMapping
    public Seller create(@Valid CreateSellerDTO dto) {
        return sellerService.create(dto);
    }

    @GetMapping
    public Iterable<Seller> get() {
        return sellerService.find();
    }
}
