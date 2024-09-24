package fastcampus.ecommerce.api.controller.product.report;

import fastcampus.ecommerce.api.service.product.report.CategoryReportResult;
import java.time.LocalDate;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class CategoryReportResponse {

  private LocalDate statDate;
  private String category;
  private Long productCount;
  private Double avgSalesPrice;
  private Integer maxSalesPrice;
  private Integer minSalesPrice;
  private Long totalStockQuantity;
  private Long potentialSalesAmount;

  public static CategoryReportResponse from(CategoryReportResult result) {
    return new CategoryReportResponse(
        result.getStatDate(),
        result.getCategory(),
        result.getProductCount(),
        result.getAvgSalesPrice(),
        result.getMaxSalesPrice(),
        result.getMinSalesPrice(),
        result.getTotalStockQuantity(),
        result.getPotentialSalesAmount()
    );
  }
}
