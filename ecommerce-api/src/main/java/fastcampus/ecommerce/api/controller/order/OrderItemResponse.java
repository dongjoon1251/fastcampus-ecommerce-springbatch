package fastcampus.ecommerce.api.controller.order;

import fastcampus.ecommerce.api.service.order.OrderItemResult;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class OrderItemResponse {

  private Long orderItemId;
  private Integer quantity;
  private Integer unitPrice;
  private String productId;

  public static OrderItemResponse from(OrderItemResult result) {
    return new OrderItemResponse(result.getOrderItemId(),
        result.getQuantity(), result.getUnitPrice(), result.getProductId());
  }
}
