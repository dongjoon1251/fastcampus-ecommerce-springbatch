package fastcampus.ecommerce.batch.jobconfig.product.report;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

import fastcampus.ecommerce.batch.domain.product.Product;
import fastcampus.ecommerce.batch.domain.product.ProductStatus;
import fastcampus.ecommerce.batch.jobconfig.BaseBatchIntegrationTest;
import fastcampus.ecommerce.batch.service.product.ProductService;
import fastcampus.ecommerce.batch.service.product.report.ProductReportService;
import fastcampus.ecommerce.batch.util.DateTimeUtils;
import java.time.LocalDate;
import org.junit.jupiter.api.Test;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.TestPropertySource;

@TestPropertySource(properties = {"spring.batch.job.name=productReportJob"})
class ProductReportJobConfigurationTest extends BaseBatchIntegrationTest {

  @Autowired
  ProductService productService;
  @Autowired
  ProductReportService productReportService;

  @Test
  public void testJob(@Autowired Job productReportJob) throws Exception {
    LocalDate now = LocalDate.now();
    saveProducts();
    jobLauncherTestUtils.setJob(productReportJob);
    JobParameters jobParameters = new JobParametersBuilder().toJobParameters();

    JobExecution jobExecution = jobLauncherTestUtils.launchJob(jobParameters);

    assertAll(() -> assertThat(productReportService.countCategoryReport(now)).isEqualTo(1),
        () -> assertThat(productReportService.countBrandReport(now)).isEqualTo(2),
        () -> assertThat(productReportService.countManufacturerReport(now)).isEqualTo(2),
        () -> assertThat(productReportService.countProductStatusReport(now)).isEqualTo(1),
        () -> assertJobCompleted(jobExecution));
  }

  private void saveProducts() {
    productService.save(
        Product.of("1", 1L, "식품", "햇반", LocalDate.of(2023, 7, 4),
            LocalDate.of(2026, 5, 28), ProductStatus.AVAILABLE, "아모레퍼시픽1",
            "나이키코리아1", 25154, 439,
            DateTimeUtils.toLocalDateTime("2024-09-19 14:24:41.404"),
            DateTimeUtils.toLocalDateTime("2024-09-19 14:24:41.404")));
    productService.save(
        Product.of("2", 2L, "식품", "햇반", LocalDate.of(2023, 7, 4),
            LocalDate.of(2026, 5, 28), ProductStatus.AVAILABLE, "아모레퍼시픽2",
            "나이키코리아2", 25154, 439,
            DateTimeUtils.toLocalDateTime("2024-09-19 14:24:41.404"),
            DateTimeUtils.toLocalDateTime("2024-09-19 14:24:41.404")));
  }
}