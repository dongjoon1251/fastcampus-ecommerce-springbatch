package fastcampus.ecommerce.batch.domain.transaction.report;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;

import java.math.BigDecimal;
import java.util.HashSet;
import java.util.List;
import org.junit.jupiter.api.Test;

class TransactionReportMapRepositoryTest {

  @Test
  void testPutAndGetTransactionReports() {
    TransactionReportMapRepository repository = new TransactionReportMapRepository();

    TransactionReport report1 = TransactionReport.of("2024-09-21", "PURCHASE", 1L, 1000L, 1L, 1L,
        1L, BigDecimal.ONE, 1L, new HashSet<>(), new HashSet<>(), new HashSet<>(), 1L);
    TransactionReport report2 = TransactionReport.of("2024-09-21", "PURCHASE", 1L, 2000L, 1L, 1L,
        1L, BigDecimal.ONE, 1L, new HashSet<>(), new HashSet<>(), new HashSet<>(), 1L);

    repository.put(report1);
    repository.put(report2);

    List<TransactionReport> reports = repository.getTransactionReports();

    assertAll(
        () -> assertEquals(1, reports.size()),
        () -> assertEquals(2L, reports.get(0).getTransactionCount()),
        () -> assertEquals(3000L, reports.get(0).getTotalAmount())
    );
  }
}