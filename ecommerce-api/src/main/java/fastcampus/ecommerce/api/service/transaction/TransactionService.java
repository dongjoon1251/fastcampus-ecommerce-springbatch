package fastcampus.ecommerce.api.service.transaction;


import fastcampus.ecommerce.api.domain.transaction.TransactionStatus;
import fastcampus.ecommerce.api.domain.transaction.TransactionType;
import fastcampus.ecommerce.api.service.order.OrderResult;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;
import org.springframework.stereotype.Service;

@Service
public class TransactionService {

  protected static Logger logger = LoggerFactory.getLogger(TransactionService.class);

  public void logTransaction(TransactionType transactionType, TransactionStatus transactionStatus,
      String message, OrderResult order) {
    try {
      putMdc(transactionType, transactionStatus, order);
      log(transactionStatus, message);
    } finally {
      MDC.clear();
    }
  }

  private void putMdc(TransactionType transactionType, TransactionStatus transactionStatus,
      OrderResult order) {
    Optional.ofNullable(order)
        .ifPresentOrElse(this::putOrder, this::putNAOrder);
    putTransaction(transactionType, transactionStatus);
  }

  private void putOrder(OrderResult order) {
    MDC.put("orderId", order.getOrderId().toString());
    MDC.put("customerId", order.getCustomerId().toString());
    MDC.put("totalAmount", String.valueOf(order.getTotalAmount()));
    MDC.put("paymentMethod", order.getPaymentMethod().toString());
    MDC.put("productCount", order.getProductCount().toString());
    MDC.put("totalItemQuantity", order.getTotalItemQuantity().toString());
  }

  private void putNAOrder() {
    MDC.put("orderId", "N/A");
    MDC.put("customerId", "N/A");
    MDC.put("totalAmount", "N/A");
    MDC.put("paymentMethod", "N/A");
    MDC.put("productCount", "N/A");
    MDC.put("totalItemQuantity", "N/A");
  }

  private void putTransaction(TransactionType transactionType,
      TransactionStatus transactionStatus) {
    MDC.put("transactionType", transactionType.name());
    MDC.put("transactionStatus", transactionStatus.name());
  }

  private void log(TransactionStatus transactionStatus, String message) {
    if (transactionStatus == TransactionStatus.SUCCESS) {
      logger.info(message);
    } else {
      logger.error(message);
    }
  }

}