package fastcampus.ecommerce.batch.jobconfig.product.report;

import fastcampus.ecommerce.batch.domain.product.report.ProductStatusReport;
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
import org.springframework.batch.item.database.JdbcBatchItemWriter;
import org.springframework.batch.item.database.JdbcCursorItemReader;
import org.springframework.batch.item.database.builder.JdbcBatchItemWriterBuilder;
import org.springframework.batch.item.database.builder.JdbcCursorItemReaderBuilder;
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
  public JdbcCursorItemReader<ProductStatusReport> productStatusReportReader(
      DataSource dataSource) {
    return new JdbcCursorItemReaderBuilder<ProductStatusReport>()
        .dataSource(dataSource)
        .name("productStatusReportReader")
        .sql("SELECT product_status,"
            + "       COUNT(*)            product_count,"
            + "       AVG(stock_quantity) avg_stock_quantity "
            + "FROM products "
            + "GROUP BY product_status")
        .beanRowMapper(ProductStatusReport.class)
        .build();
  }

  @Bean
  public JdbcBatchItemWriter<ProductStatusReport> productStatusReportWriter(DataSource dataSource) {
    return new JdbcBatchItemWriterBuilder<ProductStatusReport>()
        .dataSource(dataSource)
        .sql(
            "INSERT INTO product_status_reports(stat_date,product_status,product_count,avg_stock_quantity) "
                + "VALUES ( :statDate, :productStatus, :productCount, :avgStockQuantity) ")
        .beanMapped()
        .build();
  }

}
