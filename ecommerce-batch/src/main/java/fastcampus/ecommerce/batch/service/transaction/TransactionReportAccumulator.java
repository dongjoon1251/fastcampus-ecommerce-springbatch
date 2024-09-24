package fastcampus.ecommerce.batch.service.transaction;


import fastcampus.ecommerce.batch.domain.transaction.report.TransactionReport;
import fastcampus.ecommerce.batch.domain.transaction.report.TransactionReportMapRepository;
import fastcampus.ecommerce.batch.dto.transaction.log.TransactionLog;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class TransactionReportAccumulator {

  private final TransactionReportMapRepository repository;

  public void accumulate(TransactionLog transactionLog) {
    if (!"SUCCESS".equalsIgnoreCase(transactionLog.getTransactionStatus())) {
      return;
    }
    repository.put(TransactionReport.from(transactionLog));
  }

}