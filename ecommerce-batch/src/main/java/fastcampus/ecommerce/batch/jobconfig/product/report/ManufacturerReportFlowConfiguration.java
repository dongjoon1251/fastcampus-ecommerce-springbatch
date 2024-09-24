package fastcampus.ecommerce.batch.jobconfig.product.report;

import fastcampus.ecommerce.batch.domain.product.report.ManufacturerReport;
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
public class ManufacturerReportFlowConfiguration {

  @Bean
  public Flow manufacturerReportFlow(Step manufacturerReportStep) {
    return new FlowBuilder<SimpleFlow>("manufacturerReportFlow")
        .start(manufacturerReportStep)
        .build();
  }

  @Bean
  public Step manufacturerReportStep(JobRepository jobRepository,
      PlatformTransactionManager transactionManager,
      StepExecutionListener stepExecutionListener,
      ItemReader<ManufacturerReport> manufacturerReportReader,
      ItemWriter<ManufacturerReport> manufacturerReportWriter
  ) {
    return new StepBuilder("manufacturerReportStep", jobRepository)
        .<ManufacturerReport, ManufacturerReport>chunk(10, transactionManager)
        .reader(manufacturerReportReader)
        .writer(manufacturerReportWriter)
        .allowStartIfComplete(true)
        .listener(stepExecutionListener)
        .build();
  }

  @Bean
  public JdbcCursorItemReader<ManufacturerReport> manufacturerReportReader(DataSource dataSource) {
    return new JdbcCursorItemReaderBuilder<ManufacturerReport>()
        .dataSource(dataSource)
        .name("manufacturerReportReader")
        .sql("SELECT manufacturer,"
            + "       COUNT(*)              product_count,"
            + "       AVG(sales_price)      avg_sales_price,"
            + "       SUM(stock_quantity)   total_stock_quantity "
            + "FROM products "
            + "GROUP BY manufacturer")
        .beanRowMapper(ManufacturerReport.class)
        .build();
  }

  @Bean
  public JdbcBatchItemWriter<ManufacturerReport> manufacturerReportWriter(DataSource dataSource) {
    return new JdbcBatchItemWriterBuilder<ManufacturerReport>()
        .dataSource(dataSource)
        .sql(
            "INSERT INTO manufacturer_reports(stat_date,manufacturer,product_count,avg_sales_price,"
                + "total_stock_quantity) "
                + "VALUES ( :statDate, :manufacturer, :productCount, :avgSalesPrice, "
                + ":totalStockQuantity) ")
        .beanMapped()
        .build();
  }

}
