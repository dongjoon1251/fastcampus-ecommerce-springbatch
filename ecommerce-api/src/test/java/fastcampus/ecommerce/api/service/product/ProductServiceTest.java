package fastcampus.ecommerce.api.service.product;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import fastcampus.ecommerce.api.domain.product.InsufficientStockException;
import fastcampus.ecommerce.api.domain.product.Product;
import fastcampus.ecommerce.api.domain.product.ProductRepository;
import fastcampus.ecommerce.api.domain.product.ProductStatus;
import fastcampus.ecommerce.api.domain.product.StockQuantityArithmeticException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class ProductServiceTest {

  @Mock
  private ProductRepository productRepository;

  @InjectMocks
  private ProductService productService;

  private Product testProduct;

  @BeforeEach
  void setUp() {
    LocalDateTime now = LocalDateTime.now();
    testProduct = Product.of(
        "PROD001", 1L, "Electronics", "Test Product",
        LocalDate.now(), LocalDate.now().plusMonths(1), ProductStatus.AVAILABLE,
        "TestBrand", "TestManufacturer",
        1000, 100,
        now, now
    );
  }

  @Test
  @DisplayName("상품 조회 - 존재하는 상품이 주어졌을 때, 상품을 조회하면, 올바른 상품 정보를 반환해야 함")
  void findExistingProduct() {
    when(productRepository.findById("PROD001")).thenReturn(Optional.of(testProduct));

    ProductResult result = productService.findProduct("PROD001");

    assertAll(
        () -> assertEquals("PROD001", result.getProductId()),
        () -> assertEquals("Test Product", result.getProductName()),
        () -> assertEquals(100, result.getStockQuantity()),
        () -> assertEquals(1000, result.getSalesPrice()),
        () -> verify(productRepository, times(1)).findById("PROD001")
    );
  }

  @Test
  @DisplayName("상품 조회 - 존재하지 않는 상품이 주어졌을 때, 상품을 조회하면, ProductNotFoundException이 발생해야 함")
  void findNonExistingProduct() {
    when(productRepository.findById("PROD002")).thenReturn(Optional.empty());

    assertAll(
        () -> assertThrows(ProductNotFoundException.class,
            () -> productService.findProduct("PROD002")),
        () -> verify(productRepository, times(1)).findById("PROD002")
    );
  }

  @Test
  @DisplayName("재고 감소 - 충분한 재고가 있을 때, 재고를 감소시키면, 재고가 정상적으로 감소해야 함")
  void decreaseStockWithSufficientStock() {
    when(productRepository.findById("PROD001")).thenReturn(Optional.of(testProduct));

    assertAll(
        () -> assertDoesNotThrow(() -> productService.decreaseStock("PROD001", 50)),
        () -> assertEquals(50, testProduct.getStockQuantity()),
        () -> verify(productRepository, times(1)).findById("PROD001"),
        () -> verify(productRepository, times(1)).save(testProduct)
    );
  }

  @Test
  @DisplayName("재고 감소 - 재고가 부족할 때, 재고를 감소시키면, InsufficientStockException이 발생해야 함")
  void decreaseStockWithInsufficientStock() {
    when(productRepository.findById("PROD001")).thenReturn(Optional.of(testProduct));

    assertAll(
        () -> assertThrows(InsufficientStockException.class,
            () -> productService.decreaseStock("PROD001", 150)),
        () -> assertEquals(100, testProduct.getStockQuantity()),
        () -> verify(productRepository, times(1)).findById("PROD001"),
        () -> verify(productRepository, never()).save(any(Product.class))
    );
  }

  @Test
  @DisplayName("재고 감소 - 존재하지 않는 상품이 주어졌을 때, 재고를 감소시키면, ProductNotFoundException이 발생해야 함")
  void decreaseStockForNonExistingProduct() {
    when(productRepository.findById("PROD002")).thenReturn(Optional.empty());

    assertAll(
        () -> assertThrows(ProductNotFoundException.class,
            () -> productService.decreaseStock("PROD002", 50)),
        () -> verify(productRepository, times(1)).findById("PROD002"),
        () -> verify(productRepository, never()).save(any(Product.class))
    );
  }

  @Test
  @DisplayName("재고 증가 - 정상적인 상품이 주어졌을 때, 재고를 증가시키면, 재고가 정상적으로 증가해야 함")
  void increaseStockForNormalCase() {
    when(productRepository.findById("PROD001")).thenReturn(Optional.of(testProduct));

    assertAll(
        () -> assertDoesNotThrow(() -> productService.increaseStock("PROD001", 50)),
        () -> assertEquals(150, testProduct.getStockQuantity()),
        () -> verify(productRepository, times(1)).findById("PROD001"),
        () -> verify(productRepository, times(1)).save(testProduct)
    );
  }

  @Test
  @DisplayName("재고 증가 - 존재하지 않는 상품이 주어졌을 때, 재고를 증가시키면, ProductNotFoundException이 발생해야 함")
  void increaseStockForNonExistingProduct() {
    when(productRepository.findById("PROD002")).thenReturn(Optional.empty());

    assertAll(
        () -> assertThrows(ProductNotFoundException.class,
            () -> productService.increaseStock("PROD002", 50)),
        () -> verify(productRepository, times(1)).findById("PROD002"),
        () -> verify(productRepository, never()).save(any(Product.class))
    );
  }

  @Test
  @DisplayName("재고 증가 - 최대 재고 상품이 주어졌을 때, 재고를 증가시키면, ArithmeticException이 발생해야 함")
  void increaseStockExceedingMaxValue() {
    LocalDateTime now = LocalDateTime.now();
    Product maxStockProduct = Product.of(
        "PROD001", 1L, "Electronics", "Test Product",
        LocalDate.now(), LocalDate.now().plusMonths(1), ProductStatus.AVAILABLE,
        "TestBrand", "TestManufacturer",
        1000, Integer.MAX_VALUE,
        now, now
    );
    when(productRepository.findById("PROD003")).thenReturn(Optional.of(maxStockProduct));

    assertAll(
        () -> assertThrows(StockQuantityArithmeticException.class,
            () -> productService.increaseStock("PROD003", 1)),
        () -> assertEquals(Integer.MAX_VALUE, maxStockProduct.getStockQuantity()),
        () -> verify(productRepository, times(1)).findById("PROD003"),
        () -> verify(productRepository, never()).save(any(Product.class))
    );
  }
}