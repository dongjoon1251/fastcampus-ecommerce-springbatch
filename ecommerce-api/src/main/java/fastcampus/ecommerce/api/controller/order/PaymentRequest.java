package fastcampus.ecommerce.api.controller.order;


import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class PaymentRequest {

  private boolean success;

  public static PaymentRequest of(boolean success) {
    return new PaymentRequest(success);
  }
}