package fastcampus.ecommerce.batch.domain.transaction.report;


import fastcampus.ecommerce.batch.dto.transaction.log.TransactionLog;
import fastcampus.ecommerce.batch.util.DateTimeUtils;
import jakarta.persistence.Id;
import jakarta.persistence.Transient;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class TransactionReport implements Serializable {

  private static final long INIT_COUNT = 1L;

  @Id
  private LocalDate transactionDate;
  @Id
  private String transactionType;
  private Long transactionCount;
  private Long totalAmount;
  private Long customerCount;
  private Long orderCount;
  private Long paymentMethodCount;
  private BigDecimal avgProductCount;
  private Long totalItemQuantity;

  @Transient
  private Set<String> customerSet;
  @Transient
  private Set<String> orderSet;
  @Transient
  private Set<String> paymentMethodSet;
  @Transient
  private Long sumProductCount;

  public static TransactionReport from(TransactionLog log) {
    return new TransactionReport(DateTimeUtils.toLocalDateTime(log.getTimestamp()).toLocalDate(),
        log.getTransactionType(), INIT_COUNT, Long.parseLong(log.getTotalAmount()),
        INIT_COUNT, INIT_COUNT, INIT_COUNT,
        new BigDecimal(Long.parseLong(log.getProductCount())),
        Long.parseLong(log.getTotalItemQuantity()),
        new HashSet<>(List.of(log.getCustomerId())),
        new HashSet<>(List.of(log.getOrderId())),
        new HashSet<>(List.of(log.getPaymentMethod())),
        Long.parseLong(log.getProductCount()));
  }

  public static TransactionReport of(String transactionDate, String transactionType,
      Long transactionCount, Long totalAmount, Long customerCount, Long orderCount,
      Long paymentMethodCount, BigDecimal avgProductCount, Long totalItemQuantity,
      Set<String> customerSet, Set<String> orderSet, Set<String> paymentMethodSet,
      Long sumProductCount) {
    return new TransactionReport(DateTimeUtils.toLocalDate(transactionDate), transactionType,
        transactionCount, totalAmount,
        customerCount, orderCount, paymentMethodCount, avgProductCount, totalItemQuantity,
        customerSet, orderSet, paymentMethodSet, sumProductCount);
  }

  public void add(TransactionReport transactionReport) {
    transactionCount += transactionReport.getTransactionCount();
    totalAmount += transactionReport.getTotalAmount();
    customerSet.addAll(transactionReport.getCustomerSet());
    orderSet.addAll(transactionReport.getOrderSet());
    paymentMethodSet.addAll(transactionReport.getPaymentMethodSet());
    customerCount = (long) customerSet.size();
    orderCount = (long) orderSet.size();
    paymentMethodCount = (long) paymentMethodSet.size();
    sumProductCount += transactionReport.getSumProductCount();
    avgProductCount = new BigDecimal((double) sumProductCount / transactionCount);
    totalItemQuantity += transactionReport.getTotalItemQuantity();
  }
}