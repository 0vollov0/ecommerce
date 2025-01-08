package portfolio.ecommerce.main.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import portfolio.ecommerce.main.dto.CreateSellerDTO;
import portfolio.ecommerce.main.entity.Seller;
import portfolio.ecommerce.main.repository.SellerRepository;

@Service
public class SellerService {
    @Autowired
    private SellerRepository sellerRepository;

    public Seller create(CreateSellerDTO dto) {
        Seller seller = toEntity(dto);
        return sellerRepository.save(seller);
    }

    private Seller toEntity(CreateSellerDTO dto) {
        return Seller.builder().name(dto.getName()).build();
    }
}
