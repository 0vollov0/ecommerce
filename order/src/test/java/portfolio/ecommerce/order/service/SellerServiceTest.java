package portfolio.ecommerce.order.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import portfolio.ecommerce.order.dto.CreateSellerDto;
import portfolio.ecommerce.order.dto.RequestPagingDto;
import portfolio.ecommerce.order.dto.UpdateSellerDto;
import portfolio.ecommerce.order.entity.Seller;
import portfolio.ecommerce.order.repository.SellerRepository;

import java.util.Collections;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class SellerServiceTest {

    @Mock
    private SellerRepository sellerRepository;

    private SellerService sellerService;

    @BeforeEach
    void setUp() {
        sellerService = new SellerService(sellerRepository);
    }

    @Test
    void createSeller() {
        CreateSellerDto dto = new CreateSellerDto("TEST_SELLER");
        Seller seller = Seller.builder().name(dto.getName()).build();

        when(sellerRepository.save(any(Seller.class))).thenReturn(seller);

        Seller savedSeller = sellerService.create(dto);

        assertNotNull(savedSeller);
        assertEquals(dto.getName(), savedSeller.getName());
        verify(sellerRepository, times(1)).save(any(Seller.class));
    }

    @Test
    void findSellerById() {
        Seller seller = Seller.builder().sellerId(1L).name("TEST_SELLER").build();

        when(sellerRepository.findById(1L)).thenReturn(Optional.of(seller));

        Optional<Seller> foundSeller = sellerService.findById(1L);

        assertTrue(foundSeller.isPresent());
        assertEquals("TEST_SELLER", foundSeller.get().getName());
        verify(sellerRepository, times(1)).findById(1L);
    }

    @Test
    void findSellers() {
        RequestPagingDto dto = new RequestPagingDto(0, 10);
        Page<Seller> page = new PageImpl<>(Collections.singletonList(new Seller()));

        when(sellerRepository.findAllByDeleted(eq(false), any(PageRequest.class))).thenReturn(page);

        Page<Seller> result = sellerService.find(dto);

        assertNotNull(result);
        assertFalse(result.isEmpty());
        verify(sellerRepository, times(1)).findAllByDeleted(eq(false), any(PageRequest.class));
    }

    @Test
    void updateSeller() {
        UpdateSellerDto dto = new UpdateSellerDto("TEST_SELLER_UPDATED");
        Seller seller = Seller.builder().sellerId(1L).name("TEST_SELLER").build();

        when(sellerRepository.findById(1L)).thenReturn(Optional.of(seller));

        Seller updatedSeller = sellerService.update(1L, dto);

        assertNotNull(updatedSeller);
        assertEquals("TEST_SELLER_UPDATED", updatedSeller.getName());
        verify(sellerRepository, times(1)).findById(1L);
    }

    @Test
    void deleteSeller() {
        Seller seller = Seller.builder().sellerId(1L).name("TEST_SELLER").deleted(false).build();

        when(sellerRepository.findById(1L)).thenReturn(Optional.of(seller));

        sellerService.delete(1L);

        assertTrue(seller.isDeleted());
        verify(sellerRepository, times(1)).findById(1L);
    }
}