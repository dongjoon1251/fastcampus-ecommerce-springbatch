package fastcampus.ecommerce.api.controller.product;

import fastcampus.ecommerce.api.domain.product.ProductStatus;
import fastcampus.ecommerce.api.service.product.ProductResult;
import java.time.LocalDate;
import java.time.LocalDateTime;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class ProductResponse {

  private String productId;

  private Long sellerId;

  private String category;
  private String productName;
  private LocalDate salesStartDate;
  private LocalDate salesEndDate;
  private ProductStatus productStatus;
  private String brand;
  private String manufacturer;

  private int salesPrice;
  private int stockQuantity;
  private LocalDateTime createdAt;
  private LocalDateTime updatedAt;

  public static ProductResponse from(ProductResult result) {
    return new ProductResponse(
        result.getProductId(),
        result.getSellerId(),
        result.getCategory(),
        result.getProductName(),
        result.getSalesStartDate(),
        result.getSalesEndDate(),
        result.getProductStatus(),
        result.getBrand(),
        result.getManufacturer(),
        result.getSalesPrice(),
        result.getStockQuantity(),
        result.getCreatedAt(),
        result.getUpdatedAt()
    );
  }

  public static ProductResponse of(String productId, Long sellerId, String category,
      String productName,
      LocalDate salesStartDate, LocalDate salesEndDate, ProductStatus productStatus,
      String brand, String manufacturer,
      int salesPrice,
      int stockQuantity,
      LocalDateTime createdAt, LocalDateTime updatedAt) {
    return new ProductResponse(productId, sellerId, category, productName, salesStartDate,
        salesEndDate, productStatus,
        brand, manufacturer, salesPrice,
        stockQuantity,
        createdAt, updatedAt);
  }
}
