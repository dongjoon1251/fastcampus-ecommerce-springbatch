package fastcampus.ecommerce.api.controller.order;

import fastcampus.ecommerce.api.service.order.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 주문 관련 API 컨트롤러
 */
@RestController
@RequestMapping("/v1/orders")
public class OrderController {

  @Autowired
  private OrderService orderService;

  @PostMapping
  public OrderResponse createOrder(@RequestBody OrderRequest orderRequest) {
    return OrderResponse.from(orderService.order(orderRequest.getCustomerId(),
        orderRequest.toOrderItemCommands(), orderRequest.getPaymentMethod()));
  }

  @PostMapping("/{orderId}/payment")
  public OrderResponse completePayment(@PathVariable Long orderId,
      @RequestBody PaymentRequest paymentRequest) {
    return OrderResponse.from(orderService.completePayment(orderId, paymentRequest.isSuccess()));
  }

  @PostMapping("/{orderId}/complete")
  public OrderResponse completeOrder(@PathVariable Long orderId) {
    return OrderResponse.from(orderService.completeOrder(orderId));
  }

  @PostMapping("/{orderId}/cancel")
  public OrderResponse cancelOrder(@PathVariable Long orderId) {
    return OrderResponse.from(orderService.cancelOrder(orderId));
  }
}