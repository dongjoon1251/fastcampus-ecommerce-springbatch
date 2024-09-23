package fastcampus.ecommerce.batch.dto.product.upload;


import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class ProductUploadCsvRow {

  private Long sellerId;
  private String category;
  private String productName;
  private String salesStartDate;
  private String salesEndDate;
  private String productStatus;
  private String brand;
  private String manufacturer;

  private int salesPrice;
  private int stockQuantity;

  public static ProductUploadCsvRow of(Long sellerId, String category, String productName,
      String salesStartDate, String salesEndDate, String productStatus, String brand,
      String manufacturer, int salesPrice, int stockQuantity) {
    return new ProductUploadCsvRow(sellerId, category, productName, salesStartDate, salesEndDate,
        productStatus, brand, manufacturer, salesPrice, stockQuantity);
  }
}

