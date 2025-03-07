package portfolio.ecommerce.order.service;

import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import portfolio.ecommerce.order.dto.CreateSellerDto;
import portfolio.ecommerce.order.dto.RequestPagingDto;
import portfolio.ecommerce.order.dto.UpdateSellerDto;
import portfolio.ecommerce.order.entity.Seller;
import portfolio.ecommerce.order.repository.SellerRepository;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class SellerService {
    private final SellerRepository sellerRepository;

    public Seller create(CreateSellerDto dto) {
        return sellerRepository.save(Seller.builder().name(dto.getName()).build());
    }

    public Optional<Seller> findById(Long id) {
        return sellerRepository.findById(id);
    }

    public Page<Seller> find(RequestPagingDto dto) {
        return sellerRepository.findAll(PageRequest.of(dto.getPage(), dto.getPageSize(), Sort.by("createdAt").descending()));
    }

    @Transactional
    public Seller update(Long id, UpdateSellerDto dto) {
        Seller seller = sellerRepository.findById(id).orElseThrow(EntityNotFoundException::new);
        seller.setName(dto.getName());
        return seller;
    }

    public void delete(Long id) {
        Seller seller = sellerRepository.findById(id).orElseThrow(EntityNotFoundException::new);
        seller.setDeleted(true);
    }
}
