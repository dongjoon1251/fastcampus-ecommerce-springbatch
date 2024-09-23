package fastcampus.ecommerce.batch.domain.product;


import fastcampus.ecommerce.batch.dto.product.upload.ProductUploadCsvRow;
import fastcampus.ecommerce.batch.util.DateTimeUtils;
import fastcampus.ecommerce.batch.util.RandomUtils;
import java.time.LocalDate;
import java.time.LocalDateTime;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class Product {

  private String productId;
  private Long sellerId;

  private String category;
  private String productName;
  private LocalDate salesStartDate;
  private LocalDate salesEndDate;
  private String productStatus;
  private String brand;
  private String manufacturer;

  private int salesPrice;
  private int stockQuantity;
  private LocalDateTime createdAt;
  private LocalDateTime updatedAt;

  public static Product from(ProductUploadCsvRow row) {
    LocalDateTime now = LocalDateTime.now();
    return new Product(
        RandomUtils.generateRandomId(),
        row.getSellerId(),
        row.getCategory(),
        row.getProductName(),
        DateTimeUtils.toLocalDate(row.getSalesStartDate()),
        DateTimeUtils.toLocalDate(row.getSalesEndDate()),
        row.getProductStatus(),
        row.getBrand(),
        row.getManufacturer(),
        row.getSalesPrice(),
        row.getStockQuantity(),
        now,
        now
    );
  }
}

