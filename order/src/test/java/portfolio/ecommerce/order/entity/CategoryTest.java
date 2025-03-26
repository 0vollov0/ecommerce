package portfolio.ecommerce.order.entity;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class CategoryTest {

    @Test
    void categoryBuilderShouldCreateObjectCorrectly() {
        // Given
        Category category = Category.builder()
                .categoryId(1L)
                .name("Electronics")
                .build();

        // Then
        assertEquals(1L, category.getCategoryId());
        assertEquals("Electronics", category.getName());
    }

    @Test
    void setterShouldUpdateFieldsCorrectly() {
        // Given
        Category category = new Category();

        // When
        category.setCategoryId(2L);
        category.setName("Books");

        // Then
        assertEquals(2L, category.getCategoryId());
        assertEquals("Books", category.getName());
    }
}
