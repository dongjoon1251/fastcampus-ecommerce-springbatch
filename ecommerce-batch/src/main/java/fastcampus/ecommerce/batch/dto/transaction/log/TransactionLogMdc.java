package fastcampus.ecommerce.batch.dto.transaction.log;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
@NoArgsConstructor
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class TransactionLogMdc {

  private String transactionType;
  private String transactionStatus;
  private String orderId;
  private String customerId;
  private String totalAmount;
  private String paymentMethod;
  private String productCount;
  private String totalItemQuantity;

  public static TransactionLogMdc of(
      String transactionType,
      String transactionStatus,
      String orderId,
      String customerId,
      String totalAmount,
      String paymentMethod,
      String productCount,
      String totalItemQuantity) {
    return new TransactionLogMdc(
        transactionType,
        transactionStatus,
        orderId,
        customerId,
        totalAmount,
        paymentMethod,
        productCount,
        totalItemQuantity
    );
  }
}