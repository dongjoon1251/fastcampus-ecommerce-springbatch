package fastcampus.ecommerce.batch.jobconfig.transaction.report;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

import fastcampus.ecommerce.batch.domain.transaction.report.TransactionReportRepository;
import fastcampus.ecommerce.batch.jobconfig.BaseBatchIntegrationTest;
import java.io.IOException;
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

@TestPropertySource(properties = {"spring.batch.job.name=transactionReportJob"})
class TransactionReportJobConfigurationTest extends BaseBatchIntegrationTest {

  @Value("classpath:/logs/transaction.log")
  private Resource resource;

  @Autowired
  private TransactionReportRepository repository;

  @Test
  public void testJob(@Autowired Job transactionReportJob) throws Exception {
    jobLauncherTestUtils.setJob(transactionReportJob);
    JobParameters jobParameters = jobParameters();

    JobExecution jobExecution = jobLauncherTestUtils.launchJob(jobParameters);

    assertAll(
        () -> assertThat(repository.count()).isEqualTo(3),
        () -> assertJobCompleted(jobExecution));
  }

  private JobParameters jobParameters() throws IOException {
    return new JobParametersBuilder()
        .addJobParameter("inputFilePath",
            new JobParameter<>(resource.getFile().getPath(), String.class, false))
        .addJobParameter("gridSize",
            new JobParameter<>(3, Integer.class, false))
        .toJobParameters();
  }

}