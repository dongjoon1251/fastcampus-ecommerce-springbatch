package fastcampus.ecommerce.api.service.product.report;


import fastcampus.ecommerce.api.domain.product.ProductStatus;
import fastcampus.ecommerce.api.domain.product.report.ProductStatusReport;
import java.time.LocalDate;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class ProductStatusReportResult {

  private LocalDate statDate;
  private ProductStatus productStatus;
  private Long productCount;
  private Double avgStockQuantity;

  public static ProductStatusReportResult from(ProductStatusReport report) {
    return new ProductStatusReportResult(
        report.getStatDate(),
        report.getProductStatus(),
        report.getProductCount(),
        report.getAvgStockQuantity()
    );
  }
}
