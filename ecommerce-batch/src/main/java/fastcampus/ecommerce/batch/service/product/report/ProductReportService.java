package fastcampus.ecommerce.batch.service.product.report;

import java.time.LocalDate;
import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ProductReportService {

  private final JdbcTemplate jdbcTemplate;

  public Long countCategoryReport(LocalDate statDate) {
    return jdbcTemplate.queryForObject(
        "select count(*) from category_reports where stat_date = '" + statDate + "'", Long.class);
  }

  public Long countBrandReport(LocalDate statDate) {
    return jdbcTemplate.queryForObject(
        "select count(*) from brand_reports where stat_date = '" + statDate + "'", Long.class);
  }

  public Long countManufacturerReport(LocalDate statDate) {
    return jdbcTemplate.queryForObject(
        "select count(*) from manufacturer_reports where stat_date = '" + statDate + "'",
        Long.class);
  }

  public Long countProductStatusReport(LocalDate statDate) {
    return jdbcTemplate.queryForObject(
        "select count(*) from product_status_reports where stat_date = '" + statDate + "'",
        Long.class);
  }
}
