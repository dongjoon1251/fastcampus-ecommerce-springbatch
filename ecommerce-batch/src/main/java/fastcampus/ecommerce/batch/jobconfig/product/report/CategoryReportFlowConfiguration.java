package fastcampus.ecommerce.batch.jobconfig.product.report;

import fastcampus.ecommerce.batch.domain.product.report.CategoryReport;
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
public class CategoryReportFlowConfiguration {

  @Bean
  public Flow categoryReportFlow(Step categoryReportStep) {
    return new FlowBuilder<SimpleFlow>("categoryReportFlow")
        .start(categoryReportStep)
        .build();
  }

  @Bean
  public Step categoryReportStep(JobRepository jobRepository,
      PlatformTransactionManager transactionManager,
      StepExecutionListener stepExecutionListener,
      ItemReader<CategoryReport> categoryReportReader,
      ItemWriter<CategoryReport> categoryReportWriter
  ) {
    return new StepBuilder("categoryReportStep", jobRepository)
        .<CategoryReport, CategoryReport>chunk(10, transactionManager)
        .reader(categoryReportReader)
        .writer(categoryReportWriter)
        .allowStartIfComplete(true)
        .listener(stepExecutionListener)
        .build();
  }

  @Bean
  public JdbcCursorItemReader<CategoryReport> categoryReportReader(DataSource dataSource) {
    return new JdbcCursorItemReaderBuilder<CategoryReport>()
        .dataSource(dataSource)
        .name("categoryReportReader")
        .sql("SELECT category,"
            + "       COUNT(*)                           product_count,"
            + "       AVG(sales_price)                   avg_sales_price,"
            + "       MAX(sales_price)                   max_sales_price,"
            + "       MIN(sales_price)                   min_sales_price,"
            + "       SUM(stock_quantity)                total_stock_quantity,"
            + "       SUM(sales_price * stock_quantity)  potential_sales_amount "
            + "FROM products "
            + "GROUP BY category")
        .beanRowMapper(CategoryReport.class)
        .build();
  }

  @Bean
  public JdbcBatchItemWriter<CategoryReport> categoryReportWriter(DataSource dataSource) {
    return new JdbcBatchItemWriterBuilder<CategoryReport>()
        .dataSource(dataSource)
        .sql(
            "INSERT INTO category_reports(stat_date,category,product_count,avg_sales_price,"
                + "max_sales_price,min_sales_price,total_stock_quantity,potential_sales_amount) "
                + "VALUES ( :statDate, :category, :productCount, :avgSalesPrice, :maxSalesPrice, "
                + ":minSalesPrice, :totalStockQuantity, :potentialSalesAmount)")
        .beanMapped()
        .build();
  }

}
