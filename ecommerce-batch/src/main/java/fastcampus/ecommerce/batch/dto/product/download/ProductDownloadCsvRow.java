package fastcampus.ecommerce.batch.dto.product.download;


import fastcampus.ecommerce.batch.domain.product.Product;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class ProductDownloadCsvRow {

  private String productId;
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
  private String createdAt;
  private String updatedAt;

  public static ProductDownloadCsvRow from(Product product) {
    return new ProductDownloadCsvRow(
        product.getProductId(),
        product.getSellerId(),
        product.getCategory(),
        product.getProductName(),
        product.getSalesStartDate().toString(),
        product.getSalesEndDate().toString(),
        product.getProductStatus().name(),
        product.getBrand(),
        product.getManufacturer(),
        product.getSalesPrice(),
        product.getStockQuantity(),
        product.getCreatedAt().toString(),
        product.getUpdatedAt().toString()
    );
  }
}

