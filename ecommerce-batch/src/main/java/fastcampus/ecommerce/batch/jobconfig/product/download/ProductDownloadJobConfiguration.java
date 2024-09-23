package fastcampus.ecommerce.batch.jobconfig.product.download;

import fastcampus.ecommerce.batch.domain.product.Product;
import fastcampus.ecommerce.batch.dto.product.download.ProductDownloadCsvRow;
import fastcampus.ecommerce.batch.util.ReflectionUtils;
import java.util.List;
import javax.sql.DataSource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobExecutionListener;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.StepExecutionListener;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.database.JdbcPagingItemReader;
import org.springframework.batch.item.database.PagingQueryProvider;
import org.springframework.batch.item.database.builder.JdbcPagingItemReaderBuilder;
import org.springframework.batch.item.database.support.SqlPagingQueryProviderFactoryBean;
import org.springframework.batch.item.file.FlatFileItemWriter;
import org.springframework.batch.item.file.builder.FlatFileItemWriterBuilder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.FileSystemResource;
import org.springframework.transaction.PlatformTransactionManager;

@Slf4j
@Configuration
public class ProductDownloadJobConfiguration {

  @Bean
  public Job productDownloadJob(JobRepository jobRepository, JobExecutionListener listener,
      Step productPagingStep) {
    return new JobBuilder("productDownloadJob", jobRepository)
        .start(productPagingStep)
        .listener(listener)
        .build();
  }

  @Bean
  public Step productPagingStep(JobRepository jobRepository,
      PlatformTransactionManager transactionManager,
      JdbcPagingItemReader<Product> productPagingReader,
      ItemProcessor<Product, ProductDownloadCsvRow> productDownloadProcessor,
      ItemWriter<ProductDownloadCsvRow> productCsvWriter,
      StepExecutionListener stepExecutionListener) {
    return new StepBuilder("productPagingStep", jobRepository)
        .<Product, ProductDownloadCsvRow>chunk(10000, transactionManager)
        .reader(productPagingReader)
        .processor(productDownloadProcessor)
        .writer(productCsvWriter)
        .allowStartIfComplete(true)
        .listener(stepExecutionListener)
        .build();
  }

  @Bean
  public JdbcPagingItemReader<Product> productPagingReader(DataSource dataSource,
      PagingQueryProvider productPagingQueryProvider) {
    return new JdbcPagingItemReaderBuilder<Product>()
        .dataSource(dataSource)
        .name("productPagingReader")
        .queryProvider(productPagingQueryProvider)
        .pageSize(1000)
        .beanRowMapper(Product.class)
        .build();
  }

  @Bean
  public SqlPagingQueryProviderFactoryBean productPagingQueryProvider(DataSource dataSource) {
    SqlPagingQueryProviderFactoryBean provider = new SqlPagingQueryProviderFactoryBean();
    provider.setSelectClause(
        "select product_id, seller_id, category, product_name, sales_start_date, sales_end_date, "
            + "product_status, brand, manufacturer,sales_price, stock_quantity, "
            + "created_at, updated_at");
    provider.setFromClause("from products");
    provider.setSortKey("product_id");
    provider.setDataSource(dataSource);
    return provider;
  }

  @Bean
  public ItemProcessor<Product, ProductDownloadCsvRow> productDownloadProcessor() {
    return ProductDownloadCsvRow::from;
  }

  @Bean
  @StepScope
  public FlatFileItemWriter<ProductDownloadCsvRow> productCsvWriter(
      @Value("#{jobParameters['outputFilePath']}") String path) {
    List<String> columns = ReflectionUtils.getFieldNames(ProductDownloadCsvRow.class);
    return new FlatFileItemWriterBuilder<ProductDownloadCsvRow>()
        .name("productCsvWriter")
        .resource(new FileSystemResource(path))
        .delimited()
        .names(columns.toArray(String[]::new))
        .headerCallback(writer -> writer.write(String.join(",", columns)))
        .build();
  }

}
