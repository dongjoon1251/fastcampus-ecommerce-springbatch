package fastcampus.ecommerce.batch.jobconfig.product.report;

import fastcampus.ecommerce.batch.domain.product.report.BrandReport;
import jakarta.persistence.EntityManagerFactory;
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
public class BrandReportFlowConfiguration {

  @Bean
  public Flow brandReportFlow(Step brandReportStep) {
    return new FlowBuilder<SimpleFlow>("brandReportFlow")
        .start(brandReportStep)
        .build();
  }

  @Bean
  public Step brandReportStep(JobRepository jobRepository,
      PlatformTransactionManager transactionManager,
      StepExecutionListener stepExecutionListener,
      ItemReader<BrandReport> brandReportReader,
      ItemWriter<BrandReport> brandReportWriter
  ) {
    return new StepBuilder("brandReportStep", jobRepository)
        .<BrandReport, BrandReport>chunk(10, transactionManager)
        .reader(brandReportReader)
        .writer(brandReportWriter)
        .allowStartIfComplete(true)
        .listener(stepExecutionListener)
        .build();
  }

  @Bean
  public JpaCursorItemReader<BrandReport> brandReportReader(
      EntityManagerFactory entityManagerFactory) {
    return new JpaCursorItemReaderBuilder<BrandReport>()
        .entityManagerFactory(entityManagerFactory)
        .name("brandReportReader")
        .queryString("SELECT new BrandReport(p.brand,"
            + "       COUNT(p),"
            + "       AVG(p.salesPrice),"
            + "       MAX(p.salesPrice),"
            + "       MIN(p.salesPrice),"
            + "       SUM(p.stockQuantity),"
            + "       AVG(p.stockQuantity),"
            + "       SUM(p.salesPrice * p.stockQuantity)) "
            + "FROM Product p "
            + "GROUP BY p.brand")
        .build();
  }

  @Bean
  public JpaItemWriter<BrandReport> brandReportWriter(EntityManagerFactory entityManagerFactory) {
    return new JpaItemWriterBuilder<BrandReport>()
        .entityManagerFactory(entityManagerFactory)
        .usePersist(true)
        .build();
  }

}
