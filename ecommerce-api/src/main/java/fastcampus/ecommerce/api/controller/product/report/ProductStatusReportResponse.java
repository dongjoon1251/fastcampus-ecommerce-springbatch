package fastcampus.ecommerce.api.controller.product.report;


import fastcampus.ecommerce.api.domain.product.ProductStatus;
import fastcampus.ecommerce.api.service.product.report.ProductStatusReportResult;
import java.time.LocalDate;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class ProductStatusReportResponse {

  private LocalDate statDate;
  private ProductStatus productStatus;
  private Long productCount;
  private Double avgStockQuantity;

  public static ProductStatusReportResponse from(ProductStatusReportResult result) {
    return new ProductStatusReportResponse(
        result.getStatDate(),
        result.getProductStatus(),
        result.getProductCount(),
        result.getAvgStockQuantity()
    );
  }
}
