package fastcampus.ecommerce.api.controller.product.report;

import fastcampus.ecommerce.api.service.product.report.ProductReportResults;
import java.util.List;
import java.util.stream.Collectors;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class ProductReportResponse {

  private List<BrandReportResponse> brandReports;
  private List<CategoryReportResponse> categoryReports;
  private List<ManufacturerReportResponse> manufacturerReports;
  private List<ProductStatusReportResponse> productStatusReports;

  public static ProductReportResponse from(ProductReportResults results) {
    return new ProductReportResponse(
        results.getBrandReports().stream()
            .map(BrandReportResponse::from)
            .collect(Collectors.toList()),
        results.getCategoryReports().stream()
            .map(CategoryReportResponse::from)
            .collect(Collectors.toList()),
        results.getManufacturerReports().stream()
            .map(ManufacturerReportResponse::from)
            .collect(Collectors.toList()),
        results.getProductStatusReports().stream()
            .map(ProductStatusReportResponse::from)
            .collect(Collectors.toList())
    );
  }
}