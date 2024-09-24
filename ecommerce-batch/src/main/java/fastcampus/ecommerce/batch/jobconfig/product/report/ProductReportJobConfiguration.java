package fastcampus.ecommerce.batch.jobconfig.product.report;

import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobExecutionListener;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.job.flow.Flow;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.task.TaskExecutor;

@Slf4j
@Configuration
public class ProductReportJobConfiguration {

  @Bean
  public Job productReportJob(JobRepository jobRepository, JobExecutionListener listener,
      TaskExecutor taskExecutor,
      Flow categoryReportFlow, Flow brandReportFlow, Flow manufacturerReportFlow,
      Flow productStatusReportFlow) {
    return new JobBuilder("productReportJob", jobRepository)
        .listener(listener)
        .start(categoryReportFlow)
        .split(taskExecutor)
        .add(brandReportFlow, manufacturerReportFlow, productStatusReportFlow)
        .end()
        .build();
  }

}
