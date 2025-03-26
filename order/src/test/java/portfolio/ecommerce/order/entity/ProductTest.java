package portfolio.ecommerce.order.entity;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ProductTest {

    @Test
    void productBuilderShouldCreateObjectCorrectly() {
        // Given
        Category category = new Category(1L, "Electronics");
        Seller seller = new Seller(2L, "Amazon", false);

        Product product = Product.builder()
                .productId(100L)
                .name("Laptop")
                .salesPrice(1500)
                .stock(50)
                .category(category)
                .seller(seller)
                .deleted(false)
                .build();

        // Then
        assertEquals(100L, product.getProductId());
        assertEquals("Laptop", product.getName());
        assertEquals(1500, product.getSalesPrice());
        assertEquals(50, product.getStock());
        assertEquals(1L, product.getCategory().getCategoryId());
        assertEquals(2L, product.getSeller().getSellerId());
        assertFalse(product.isDeleted());
    }

    @Test
    void setterShouldUpdateFieldsCorrectly() {
        // Given
        Product product = new Product();

        // When
        product.setProductId(200L);
        product.setName("Smartphone");
        product.setSalesPrice(800);
        product.setStock(30);
        product.setDeleted(true);

        // Then
        assertEquals(200L, product.getProductId());
        assertEquals("Smartphone", product.getName());
        assertEquals(800, product.getSalesPrice());
        assertEquals(30, product.getStock());
        assertTrue(product.isDeleted());
    }
}
