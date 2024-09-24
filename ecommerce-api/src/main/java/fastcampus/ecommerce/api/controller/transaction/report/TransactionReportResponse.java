package fastcampus.ecommerce.api.controller.transaction.report;


import fastcampus.ecommerce.api.service.transaction.report.TransactionReportResult;
import java.math.BigDecimal;
import java.time.LocalDate;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class TransactionReportResponse {

  private LocalDate transactionDate;
  private String transactionType;
  private Long transactionCount;
  private Long totalAmount;
  private Long customerCount;
  private Long orderCount;
  private Long paymentMethodCount;
  private BigDecimal avgProductCount;
  private Long totalItemQuantity;

  public static TransactionReportResponse from(TransactionReportResult result) {
    return new TransactionReportResponse(
        result.getTransactionDate(),
        result.getTransactionType(),
        result.getTransactionCount(),
        result.getTotalAmount(),
        result.getCustomerCount(),
        result.getOrderCount(),
        result.getPaymentMethodCount(),
        result.getAvgProductCount(),
        result.getTotalItemQuantity()
    );
  }
}