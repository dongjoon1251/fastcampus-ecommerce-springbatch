package fastcampus.ecommerce.api.domain.payment;

public class IllegalPaymentStateException extends IllegalStateException {

  public IllegalPaymentStateException(String msg) {
    super(msg);
  }
}
