package fastcampus.ecommerce.api.service.order;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import fastcampus.ecommerce.api.domain.order.Order;
import fastcampus.ecommerce.api.domain.order.OrderRepository;
import fastcampus.ecommerce.api.domain.order.OrderStatus;
import fastcampus.ecommerce.api.domain.payment.PaymentMethod;
import fastcampus.ecommerce.api.domain.payment.PaymentStatus;
import fastcampus.ecommerce.api.domain.product.ProductStatus;
import fastcampus.ecommerce.api.service.product.ProductResult;
import fastcampus.ecommerce.api.service.product.ProductService;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class OrderServiceTest {

  @Mock
  private OrderRepository orderRepository;

  @Mock
  private ProductService productService;

  @InjectMocks
  private OrderService orderService;

  private Order testOrder;
  private ProductResult testProduct;

  @BeforeEach
  void setUp() {
    LocalDateTime now = LocalDateTime.now();
    testProduct = ProductResult.of(
        "PROD001", 1L, "Electronics", "Test Product",
        LocalDate.now(), LocalDate.now().plusMonths(1), ProductStatus.AVAILABLE,
        "TestBrand", "TestManufacturer",
        1000, 100,
        now, now
    );
    ;

    testOrder = Order.createOrder(100L);
    testOrder.addOrderItem("PROD001", 2, 1000);
    testOrder.initPayment(PaymentMethod.CREDIT_CARD);
  }

  @Test
  @DisplayName("주문 생성 - 정상 케이스")
  void testOrder() {
    List<OrderItemCommand> orderItems = Arrays.asList(
        new OrderItemCommand("PROD001", 2)
    );

    when(productService.findProduct("PROD001")).thenReturn(testProduct);
    when(orderRepository.save(any(Order.class))).thenReturn(testOrder);

    OrderResult result = orderService.order(100L, orderItems, PaymentMethod.CREDIT_CARD);

    assertAll(
        () -> assertEquals(100L, result.getCustomerId()),
        () -> assertEquals(OrderStatus.PENDING_PAYMENT, result.getOrderStatus()),
        () -> assertEquals(PaymentMethod.CREDIT_CARD, result.getPaymentMethod()),
        () -> assertEquals(PaymentStatus.PENDING, result.getPaymentStatus()),
        () -> assertEquals(2000, result.getTotalAmount()),
        () -> assertFalse(result.isPaymentSuccess()),
        () -> verify(productService).findProduct("PROD001"),
        () -> verify(orderRepository).save(any(Order.class))
    );
  }


  @Test
  @DisplayName("결제 완료 - 성공 케이스")
  void testCompletePayment_Success() {
    when(orderRepository.findById(any())).thenReturn(Optional.of(testOrder));
    when(orderRepository.save(any(Order.class))).thenAnswer(
        invocation -> invocation.getArgument(0));

    OrderResult result = orderService.completePayment(testOrder.getOrderId(), true);

    assertAll(
        () -> assertEquals(OrderStatus.PROCESSING, result.getOrderStatus()),
        () -> assertEquals(PaymentStatus.COMPLETED, result.getPaymentStatus()),
        () -> assertTrue(result.isPaymentSuccess()),
        () -> verify(productService).decreaseStock("PROD001", 2),
        () -> verify(orderRepository).save(any(Order.class))
    );
  }

  @Test
  @DisplayName("결제 완료 - 실패 케이스")
  void testCompletePayment_Failure() {
    when(orderRepository.findById(any())).thenReturn(Optional.of(testOrder));
    when(orderRepository.save(any(Order.class))).thenAnswer(
        invocation -> invocation.getArgument(0));

    OrderResult result = orderService.completePayment(testOrder.getOrderId(), false);

    assertAll(
        () -> assertEquals(OrderStatus.PROCESSING, result.getOrderStatus()),
        () -> assertEquals(PaymentStatus.FAILED, result.getPaymentStatus()),
        () -> assertFalse(result.isPaymentSuccess()),
        () -> verify(productService, never()).decreaseStock(anyString(), anyInt()),
        () -> verify(orderRepository).save(any(Order.class))
    );
  }

  @Test
  @DisplayName("주문 완료")
  void testCompleteOrder() {
    testOrder.completePayment(true);
    when(orderRepository.findById(any())).thenReturn(Optional.of(testOrder));
    when(orderRepository.save(any(Order.class))).thenAnswer(
        invocation -> invocation.getArgument(0));

    OrderResult result = orderService.completeOrder(testOrder.getOrderId());

    assertAll(
        () -> assertEquals(OrderStatus.COMPLETED, result.getOrderStatus()),
        () -> assertEquals(PaymentStatus.COMPLETED, result.getPaymentStatus()),
        () -> verify(orderRepository).save(any(Order.class))
    );
  }

  @Test
  @DisplayName("주문 취소")
  void testCancelOrder() {
    testOrder.completePayment(true);
    when(orderRepository.findById(any())).thenReturn(Optional.of(testOrder));
    when(orderRepository.save(any(Order.class))).thenAnswer(
        invocation -> invocation.getArgument(0));

    OrderResult result = orderService.cancelOrder(testOrder.getOrderId());

    assertAll(
        () -> assertEquals(OrderStatus.CANCELLED, result.getOrderStatus()),
        () -> assertEquals(PaymentStatus.REFUNDED, result.getPaymentStatus()),
        () -> verify(productService).increaseStock("PROD001", 2),
        () -> verify(orderRepository).save(any(Order.class))
    );
  }

  @Test
  @DisplayName("존재하지 않는 주문 - OrderNotFoundException 발생")
  void testOrderNotFound() {
    when(orderRepository.findById(999L)).thenReturn(Optional.empty());

    assertAll(
        () -> assertThrows(OrderNotFoundException.class,
            () -> orderService.completePayment(999L, true)),
        () -> assertThrows(OrderNotFoundException.class, () -> orderService.cancelOrder(999L))
    );
  }
}