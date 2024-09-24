package fastcampus.ecommerce.batch.jobconfig.product.report;

import fastcampus.ecommerce.batch.domain.product.report.BrandReport;
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
  public JdbcCursorItemReader<BrandReport> brandReportReader(DataSource dataSource) {
    return new JdbcCursorItemReaderBuilder<BrandReport>()
        .dataSource(dataSource)
        .name("brandReportReader")
        .sql("SELECT brand,"
            + "       COUNT(*)                          product_count,"
            + "       AVG(sales_price)                  avg_sales_price,"
            + "       MAX(sales_price)                  max_sales_price,"
            + "       MIN(sales_price)                  min_sales_price,"
            + "       SUM(stock_quantity)               total_stock_quantity,"
            + "       AVG(stock_quantity)               avg_stock_quantity,"
            + "       SUM(sales_price * stock_quantity) total_stock_value "
            + "FROM products "
            + "GROUP BY brand")
        .beanRowMapper(BrandReport.class)
        .build();
  }

  @Bean
  public JdbcBatchItemWriter<BrandReport> brandReportWriter(DataSource dataSource) {
    return new JdbcBatchItemWriterBuilder<BrandReport>()
        .dataSource(dataSource)
        .sql(
            "INSERT INTO brand_reports(stat_date,brand,product_count,avg_sales_price,max_sales_price,min_sales_price,"
                + "total_stock_quantity,avg_stock_quantity,total_stock_value) "
                + "VALUES ( :statDate, :brand, :productCount, :avgSalesPrice, :maxSalesPrice, "
                + ":minSalesPrice, :totalStockQuantity, :avgStockQuantity,  :totalStockValue )")
        .beanMapped()
        .build();
  }

}
