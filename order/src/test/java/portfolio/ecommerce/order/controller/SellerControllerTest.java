package portfolio.ecommerce.order.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import portfolio.ecommerce.order.dto.CreateSellerDto;
import portfolio.ecommerce.order.dto.RequestPagingDto;
import portfolio.ecommerce.order.dto.UpdateSellerDto;
import portfolio.ecommerce.order.entity.Seller;
import portfolio.ecommerce.order.service.SellerService;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


@ExtendWith(MockitoExtension.class)
class SellerControllerTest {

    private MockMvc mockMvc;

    @Mock
    private SellerService sellerService;

    @BeforeEach
    void setUp() {
        SellerController sellerController = new SellerController(sellerService);
        mockMvc = MockMvcBuilders.standaloneSetup(sellerController).build();
    }

    @Test
    void getSellers() throws Exception {
        when(sellerService.find(any(RequestPagingDto.class))).thenReturn(any());

        mockMvc.perform(get("/sellers")
                        .param("page", "0")
                        .param("pageSize", "10"))
                .andExpect(status().isOk());

        verify(sellerService, times(1)).find(any(RequestPagingDto.class));
    }

    @Test
    void createSeller() throws Exception {
        CreateSellerDto dto = new CreateSellerDto("TestSeller");
        ObjectMapper objectMapper = new ObjectMapper();
        String json = objectMapper.writeValueAsString(dto);

        Seller seller = Seller.builder().sellerId(1L).name(dto.getName()).build();

        when(sellerService.create(any(CreateSellerDto.class))).thenReturn(seller);

        mockMvc.perform(post("/sellers")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.name").value("TestSeller"));

        verify(sellerService, times(1)).create(any(CreateSellerDto.class));
    }

    @Test
    void updateSeller() throws Exception {
        UpdateSellerDto dto = new UpdateSellerDto("UpdatedSeller");
        ObjectMapper objectMapper = new ObjectMapper();
        String json = objectMapper.writeValueAsString(dto);
        Seller seller = Seller.builder().sellerId(1L).name(dto.getName()).build();

        when(sellerService.update(eq(1L), any(UpdateSellerDto.class))).thenReturn(seller);

        mockMvc.perform(patch("/sellers/{id}", 1L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("UpdatedSeller"));

        verify(sellerService, times(1)).update(eq(1L), any(UpdateSellerDto.class));
    }

    @Test
    void deleteSeller() throws Exception {
        doNothing().when(sellerService).delete(1L);

        mockMvc.perform(delete("/sellers/{id}", 1L))
                .andExpect(status().isOk());

        verify(sellerService, times(1)).delete(1L);
    }
}
