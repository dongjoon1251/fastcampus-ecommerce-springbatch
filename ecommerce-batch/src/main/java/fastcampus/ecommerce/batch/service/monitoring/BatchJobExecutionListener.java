package fastcampus.ecommerce.batch.service.monitoring;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobExecutionListener;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class BatchJobExecutionListener implements JobExecutionListener {


  @Override
  public void beforeJob(JobExecution jobExecution) {
    log.info("listener: before Job");
  }

  @Override
  public void afterJob(JobExecution jobExecution) {
    log.info("listener: after Job {}", jobExecution.getExecutionContext());
  }

}
