package fastcampus.ecommerce.batch.service.product.report;

import fastcampus.ecommerce.batch.domain.product.report.BrandReportRepository;
import fastcampus.ecommerce.batch.domain.product.report.CategoryReportRepository;
import fastcampus.ecommerce.batch.domain.product.report.ManufacturerReportRepository;
import fastcampus.ecommerce.batch.domain.product.report.ProductStatusReportRepository;
import java.time.LocalDate;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ProductReportService {

  private final BrandReportRepository brandReportRepository;
  private final CategoryReportRepository categoryReportRepository;
  private final ManufacturerReportRepository manufacturerReportRepository;
  private final ProductStatusReportRepository productStatusReportRepository;

  public Long countCategoryReport(LocalDate statDate) {
    return categoryReportRepository.countByStatDate(statDate);
  }

  public Long countBrandReport(LocalDate statDate) {
    return brandReportRepository.countByStatDate(statDate);
  }

  public Long countManufacturerReport(LocalDate statDate) {
    return manufacturerReportRepository.countByStatDate(statDate);
  }

  public Long countProductStatusReport(LocalDate statDate) {
    return productStatusReportRepository.countByStatDate(statDate);
  }
}
