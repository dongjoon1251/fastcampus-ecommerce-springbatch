package fastcampus.ecommerce.batch.jobconfig.product.report;

import fastcampus.ecommerce.batch.domain.product.report.ProductStatusReport;
import jakarta.persistence.EntityManagerFactory;
import javax.sql.DataSource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.StepExecutionListener;
import org.springframework.batch.core.job.builder.FlowBuilder;
import org.springframework.batch.core.job.flow.Flow;
import org.springframework.batch.core.job.flow.support.SimpleFlow;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.database.JpaCursorItemReader;
import org.springframework.batch.item.database.JpaItemWriter;
import org.springframework.batch.item.database.builder.JpaCursorItemReaderBuilder;
import org.springframework.batch.item.database.builder.JpaItemWriterBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.PlatformTransactionManager;

@Slf4j
@Configuration
public class ProductStatusReportFlowConfiguration {

  @Bean
  public Flow productStatusReportFlow(Step productStatusReportStep) {
    return new FlowBuilder<SimpleFlow>("productStatusReportFlow")
        .start(productStatusReportStep)
        .build();
  }

  @Bean
  public Step productStatusReportStep(JobRepository jobRepository,
      PlatformTransactionManager transactionManager, DataSource dataSource,
      StepExecutionListener stepExecutionListener,
      ItemReader<ProductStatusReport> productStatusReportReader,
      ItemWriter<ProductStatusReport> productStatusReportWriter
  ) {
    return new StepBuilder("productStatusReportStep", jobRepository)
        .<ProductStatusReport, ProductStatusReport>chunk(10, transactionManager)
        .reader(productStatusReportReader)
        .writer(productStatusReportWriter)
        .allowStartIfComplete(true)
        .listener(stepExecutionListener)
        .build();
  }

  @Bean
  public JpaCursorItemReader<ProductStatusReport> productStatusReportReader(
      EntityManagerFactory entityManagerFactory) {
    return new JpaCursorItemReaderBuilder<ProductStatusReport>()
        .entityManagerFactory(entityManagerFactory)
        .name("productStatusReportReader")
        .queryString("SELECT new ProductStatusReport(p.productStatus,"
            + "       COUNT(p),"
            + "       AVG(p.stockQuantity)) "
            + "FROM Product p "
            + "GROUP BY p.productStatus")
        .build();
  }

  @Bean
  public JpaItemWriter<ProductStatusReport> productStatusReportWriter(
      EntityManagerFactory entityManagerFactory) {
    return new JpaItemWriterBuilder<ProductStatusReport>()
        .entityManagerFactory(entityManagerFactory)
        .usePersist(true)
        .build();
  }

}
