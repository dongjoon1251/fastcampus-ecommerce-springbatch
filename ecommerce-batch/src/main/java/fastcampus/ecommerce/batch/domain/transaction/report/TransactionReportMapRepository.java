package fastcampus.ecommerce.batch.domain.transaction.report;

import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import org.springframework.stereotype.Repository;

@Repository
public class TransactionReportMapRepository {

  private final ConcurrentMap<String, TransactionReport> reportMap = new ConcurrentHashMap<>();

  public void put(TransactionReport transactionReport) {
    String key = getKey(transactionReport);
    reportMap.compute(key, (k, report) -> {
      if (report == null) {
        return transactionReport;
      }
      report.add(transactionReport);
      return report;
    });
  }

  private String getKey(TransactionReport transactionReport) {
    return transactionReport.getTransactionDate() + "|" + transactionReport.getTransactionType();
  }

  public List<TransactionReport> getTransactionReports() {
    return reportMap.values().stream()
        .toList();
  }
}
