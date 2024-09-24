package fastcampus.ecommerce.api.controller.order;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class OrderItemRequest {

  private String productId;
  private int quantity;

  public static OrderItemRequest of(String productId, int quantity) {
    return new OrderItemRequest(productId, quantity);
  }
}