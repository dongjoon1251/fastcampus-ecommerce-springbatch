package fastcampus.ecommerce.api.service.order;


import fastcampus.ecommerce.api.domain.order.Order;
import fastcampus.ecommerce.api.domain.order.OrderRepository;
import fastcampus.ecommerce.api.domain.payment.PaymentMethod;
import fastcampus.ecommerce.api.service.product.ProductResult;
import fastcampus.ecommerce.api.service.product.ProductService;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class OrderService {

  private final OrderRepository orderRepository;
  private final ProductService productService;

  @Transactional
  public OrderResult order(Long customerId, List<OrderItemCommand> orderItems,
      PaymentMethod paymentMethod) {
    Order order = Order.createOrder(customerId);
    for (OrderItemCommand item : orderItems) {
      ProductResult product = productService.findProduct(item.getProductId());
      order.addOrderItem(product.getProductId(), item.getQuantity(),
          product.getSalesPrice());
    }
    order.initPayment(paymentMethod);
    return save(order);
  }

  private OrderResult save(Order order) {
    return OrderResult.from(orderRepository.save(order));
  }

}