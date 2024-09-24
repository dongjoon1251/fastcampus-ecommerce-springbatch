package fastcampus.ecommerce.api.domain.product;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.time.LocalDate;
import java.time.LocalDateTime;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class ProductTest {

  private Product product;

  @BeforeEach
  void setUp() {
    LocalDateTime now = LocalDateTime.now();
    product = Product.of(
        "PROD001", 1L, "Electronics", "Test Product",
        LocalDate.now(), LocalDate.now().plusMonths(1), ProductStatus.AVAILABLE,
        "TestBrand", "TestManufacturer",
        1000, 100,
        now, now
    );
  }

  @Test
  @DisplayName("재고 증가가 올바르게 동작해야 함")
  void testIncreaseStock() {
    product.increaseStock(50);
    assertEquals(150, product.getStockQuantity());
  }

  @Test
  @DisplayName("재고 증가 시 음수가 되는 경우 예외가 발생해야 함")
  void testIncreaseStockWithNegativeResult() {
    assertThrows(StockQuantityArithmeticException.class,
        () -> product.increaseStock(Integer.MIN_VALUE));
  }

  @Test
  @DisplayName("재고 감소가 올바르게 동작해야 함")
  void testDecreaseStock() {
    product.decreaseStock(50);
    assertEquals(50, product.getStockQuantity());
  }

  @Test
  @DisplayName("재고보다 많은 수량을 감소시키려 할 때 예외가 발생해야 함")
  void testDecreaseStockWithInsufficientStock() {
    assertThrows(InsufficientStockException.class, () -> product.decreaseStock(150));
  }

}