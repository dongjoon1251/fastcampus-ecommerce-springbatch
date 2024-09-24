package fastcampus.ecommerce.api.controller.product.report;

import fastcampus.ecommerce.api.service.product.report.BrandReportResult;
import java.time.LocalDate;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class BrandReportResponse {

  private LocalDate statDate;
  private String brand;
  private Long productCount;
  private Double avgSalesPrice;
  private Integer maxSalesPrice;
  private Integer minSalesPrice;
  private Long totalStockQuantity;
  private Double avgStockQuantity;
  private Long totalStockValue;

  public static BrandReportResponse from(BrandReportResult result) {
    return new BrandReportResponse(
        result.getStatDate(),
        result.getBrand(),
        result.getProductCount(),
        result.getAvgSalesPrice(),
        result.getMaxSalesPrice(),
        result.getMinSalesPrice(),
        result.getTotalStockQuantity(),
        result.getAvgStockQuantity(),
        result.getTotalStockValue()
    );
  }
}
