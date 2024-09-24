package fastcampus.ecommerce.batch.domain.transaction.report;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;

import fastcampus.ecommerce.batch.dto.transaction.log.TransactionLog;
import fastcampus.ecommerce.batch.dto.transaction.log.TransactionLogMdc;
import java.math.BigDecimal;
import java.util.HashSet;
import java.util.List;
import org.junit.jupiter.api.Test;

class TransactionReportTest {

  @Test
  void testFromTransactionLog() {
    TransactionLog log = createSampleTransactionLog();
    TransactionReport report = TransactionReport.from(log);

    assertAll(
        () -> assertEquals("PURCHASE", report.getTransactionType()),
        () -> assertEquals(1L, report.getTransactionCount()),
        () -> assertEquals(1000L, report.getTotalAmount()),
        () -> assertEquals(1L, report.getCustomerCount()),
        () -> assertEquals(1L, report.getOrderCount()),
        () -> assertEquals(1L, report.getPaymentMethodCount()),
        () -> assertEquals(new BigDecimal(2), report.getAvgProductCount()),
        () -> assertEquals(3L, report.getTotalItemQuantity())
    );
  }

  @Test
  void testAddTransactionReport() {
    TransactionReport report1 = createSampleTransactionReport("2024-09-21", "PURCHASE", 1L, 1000L,
        "CUST-1", "ORDER-1", "CREDIT_CARD", 2L, 3L);
    TransactionReport report2 = createSampleTransactionReport("2024-09-21", "PURCHASE", 1L, 2000L,
        "CUST-2", "ORDER-2", "DEBIT_CARD", 3L, 4L);

    report1.add(report2);

    assertAll(
        () -> assertEquals(2L, report1.getTransactionCount()),
        () -> assertEquals(3000L, report1.getTotalAmount()),
        () -> assertEquals(2L, report1.getCustomerCount()),
        () -> assertEquals(2L, report1.getOrderCount()),
        () -> assertEquals(2L, report1.getPaymentMethodCount()),
        () -> assertEquals(new BigDecimal("2.5"), report1.getAvgProductCount()),
        () -> assertEquals(7L, report1.getTotalItemQuantity())
    );
  }

  private TransactionLog createSampleTransactionLog() {
    return TransactionLog.of(
        "2024-09-23 19:46:45.120",
        "INFO",
        "TransactionLogger",
        "main",
        "Transaction completed",
        TransactionLogMdc.of(
            "PURCHASE",
            "COMPLETED",
            "ORDER-1",
            "CUST-1",
            "1000",
            "CREDIT_CARD",
            "2",
            "3")
    );
  }

  private TransactionReport createSampleTransactionReport(String date, String type, Long count,
      Long amount, String customerId, String orderId, String paymentMethod, Long productCount,
      Long itemQuantity) {
    return TransactionReport.of(
        date, type, count, amount, 1L, 1L, 1L,
        new BigDecimal(productCount), itemQuantity,
        new HashSet<>(List.of(customerId)),
        new HashSet<>(List.of(orderId)),
        new HashSet<>(List.of(paymentMethod)),
        productCount
    );
  }

}