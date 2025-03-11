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
import portfolio.ecommerce.order.dto.CreateProductDto;
import portfolio.ecommerce.order.dto.RequestPagingDto;
import portfolio.ecommerce.order.dto.UpdateProductDto;
import portfolio.ecommerce.order.entity.Product;
import portfolio.ecommerce.order.service.ProductService;

import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


@ExtendWith(MockitoExtension.class)
class ProductControllerTest {

    private MockMvc mockMvc;

    @Mock
    private ProductService productService;

    @BeforeEach
    void setUp() {
        ProductController productController = new ProductController(productService);
        mockMvc = MockMvcBuilders.standaloneSetup(productController).build();
    }

    @Test
    void getProducts() throws Exception {
        when(productService.find(any(RequestPagingDto.class))).thenReturn(any());

        mockMvc.perform(get("/products")
                        .param("page", "0")
                        .param("pageSize", "10"))
                .andExpect(status().isOk());

        verify(productService, times(1)).find(any(RequestPagingDto.class));
    }

    @Test
    void getProductById_Found() throws Exception {
        Product product = Product.builder().productId(1L).name("TEST_PRODUCT").build();

        when(productService.findById(1L)).thenReturn(Optional.of(product));

        mockMvc.perform(get("/products/{id}", 1L))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("TEST_PRODUCT"));

        verify(productService, times(1)).findById(1L);
    }

    @Test
    void getProductById_NotFound() throws Exception {
        when(productService.findById(1L)).thenReturn(Optional.empty());

        mockMvc.perform(get("/products/{id}", 1L))
                .andExpect(status().isOk()); // Optional을 반환하므로 404가 아닌 200 응답

        verify(productService, times(1)).findById(1L);
    }

    @Test
    void createProduct() throws Exception {
        CreateProductDto dto = new CreateProductDto(1L, 1L, "CreatedProduct", 3000, 10);
        ObjectMapper objectMapper = new ObjectMapper();
        String json = objectMapper.writeValueAsString(dto);
        Product product = Product.builder().productId(1L).name(dto.getName()).stock(dto.getStock()).salesPrice(dto.getSalesPrice()).build();

        when(productService.create(any(CreateProductDto.class))).thenReturn(product);

        mockMvc.perform(post("/products")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.name").value("CreatedProduct"))
                .andExpect(jsonPath("$.stock").value(10))
                .andExpect(jsonPath("$.salesPrice").value(3000));

        verify(productService, times(1)).create(any(CreateProductDto.class));
    }

    @Test
    void updateProduct() throws Exception {
        UpdateProductDto dto = new UpdateProductDto(1L, "UpdatedProduct", 1000, 100);
        ObjectMapper objectMapper = new ObjectMapper();
        String json = objectMapper.writeValueAsString(dto);

        Product product = Product.builder().productId(1L).name(dto.getName()).stock(dto.getStock()).salesPrice(dto.getSalesPrice()).build();

        when(productService.update(eq(1L), any(UpdateProductDto.class))).thenReturn(product);

        mockMvc.perform(patch("/products/{id}", 1L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("UpdatedProduct"))
                .andExpect(jsonPath("$.stock").value(100))
                .andExpect(jsonPath("$.salesPrice").value(1000));

        verify(productService, times(1)).update(eq(1L), any(UpdateProductDto.class));
    }

    @Test
    void deleteProduct() throws Exception {
        doNothing().when(productService).delete(1L);

        mockMvc.perform(delete("/products/{id}", 1L))
                .andExpect(status().isOk());

        verify(productService, times(1)).delete(1L);
    }
}
