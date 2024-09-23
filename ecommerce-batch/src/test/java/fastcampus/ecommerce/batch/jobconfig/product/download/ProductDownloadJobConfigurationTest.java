package fastcampus.ecommerce.batch.jobconfig.product.download;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

import fastcampus.ecommerce.batch.domain.product.Product;
import fastcampus.ecommerce.batch.domain.product.ProductStatus;
import fastcampus.ecommerce.batch.jobconfig.BaseBatchIntegrationTest;
import fastcampus.ecommerce.batch.service.product.ProductService;
import fastcampus.ecommerce.batch.util.DateTimeUtils;
import fastcampus.ecommerce.batch.util.FileUtils;
import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDate;
import org.junit.jupiter.api.Test;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobParameter;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.test.context.TestPropertySource;

@TestPropertySource(properties = {"spring.batch.job.name=productDownloadJob"})
class ProductDownloadJobConfigurationTest extends BaseBatchIntegrationTest {

  @Value("classpath:/data/products_downloaded_expected.csv")
  private Resource expectedResource;
  private File outputFile;
  @Autowired
  private ProductService productService;

  @Test
  public void testJob(@Autowired Job productDownloadJob) throws Exception {
    saveProducts();
    outputFile = FileUtils.createTempFile("products_downloaded", ".csv");
    JobParameters jobParameters = jobParameters();
    jobLauncherTestUtils.setJob(productDownloadJob);

    JobExecution jobExecution = jobLauncherTestUtils.launchJob(jobParameters);

    assertAll(() -> assertThat(Files.readString(Path.of(outputFile.getPath()))).isEqualTo(
            Files.readString(Path.of(expectedResource.getFile().getPath()))),
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


  private JobParameters jobParameters() {
    return new JobParametersBuilder()
        .addJobParameter("outputFilePath",
            new JobParameter<>(outputFile.getPath(), String.class, false))
        .toJobParameters();
  }

}