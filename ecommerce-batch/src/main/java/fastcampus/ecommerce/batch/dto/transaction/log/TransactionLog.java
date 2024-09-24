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
public class TransactionLog {

  private String timestamp;
  private String level;
  private String logger;
  private String thread;
  private String message;
  private TransactionLogMdc mdc;

  public static TransactionLog of(
      String timestamp,
      String level,
      String logger,
      String thread,
      String message,
      TransactionLogMdc mdc) {
    return new TransactionLog(timestamp, level, logger, thread, message, mdc);
  }

  public String getTransactionType() {
    return mdc.getTransactionType();
  }

  public String getTransactionStatus() {
    return mdc.getTransactionStatus();
  }

  public String getOrderId() {
    return mdc.getOrderId();
  }

  public String getCustomerId() {
    return mdc.getCustomerId();
  }

  public String getTotalAmount() {
    String totalAmount = mdc.getTotalAmount();
    if (totalAmount.equals("N/A")) {
      return "0";
    }
    return totalAmount;
  }

  public String getPaymentMethod() {
    return mdc.getPaymentMethod();
  }

  public String getProductCount() {
    return mdc.getProductCount();
  }

  public String getTotalItemQuantity() {
    return mdc.getTotalItemQuantity();
  }
}
