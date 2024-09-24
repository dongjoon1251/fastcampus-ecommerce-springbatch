package fastcampus.ecommerce.api.domain.order;


import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import fastcampus.ecommerce.api.domain.payment.PaymentMethod;
import fastcampus.ecommerce.api.domain.payment.PaymentStatus;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;

class OrderTest {

  private Order order;
  private static final Long CUSTOMER_ID = 1L;

  @BeforeEach
  void setUp() {
    order = Order.createOrder(CUSTOMER_ID);
  }

  @Test
  @DisplayName("주문 생성 시 초기 상태가 올바르게 설정되어야 함")
  void testCreateOrder() {
    assertAll(
        () -> assertNull(order.getOrderId()),
        () -> assertNotNull(order.getOrderDate()),
        () -> assertEquals(CUSTOMER_ID, order.getCustomerId()),
        () -> assertEquals(OrderStatus.PENDING_PAYMENT, order.getOrderStatus()),
        () -> assertTrue(order.getOrderItems().isEmpty()),
        () -> assertNull(order.getPayment())
    );
  }

  @Test
  @DisplayName("주문 항목 추가 시 올바르게 처리되어야 함")
  void testAddOrderItem() {
    OrderItem item = order.addOrderItem("PROD001", 2, 1000);

    assertAll(
        () -> assertEquals(1, order.getOrderItems().size()),
        () -> assertEquals("PROD001", item.getProductId()),
        () -> assertEquals(2, item.getQuantity()),
        () -> assertEquals(1000, item.getUnitPrice()),
        () -> assertEquals(order, item.getOrder())
    );
  }

  @Test
  @DisplayName("결제 초기화 시 Payment 객체가 올바르게 생성되어야 함")
  void testInitPayment() {
    order.addOrderItem("PROD001", 2, 1000);
    order.initPayment(PaymentMethod.CREDIT_CARD);

    assertAll(
        () -> assertNotNull(order.getPayment()),
        () -> assertEquals(PaymentMethod.CREDIT_CARD, order.getPaymentMethod()),
        () -> assertEquals(2000, order.getPayment().getAmount()),
        () -> assertEquals(order, order.getPayment().getOrder())
    );
  }

  @Test
  @DisplayName("결제 완료 시 주문 상태와 결제 상태가 올바르게 변경되어야 함")
  void testCompletePayment() {
    order.addOrderItem("PROD001", 2, 1000);
    order.initPayment(PaymentMethod.CREDIT_CARD);

    order.completePayment(true);

    assertAll(
        () -> assertEquals(OrderStatus.PROCESSING, order.getOrderStatus()),
        () -> assertEquals(PaymentStatus.COMPLETED, order.getPaymentStatus()),
        () -> assertTrue(order.isPaymentSuccess())
    );
  }

  @Test
  @DisplayName("결제 실패 시 주문 상태와 결제 상태가 올바르게 변경되어야 함")
  void testFailPayment() {
    order.addOrderItem("PROD001", 2, 1000);
    order.initPayment(PaymentMethod.CREDIT_CARD);

    order.completePayment(false);

    assertAll(
        () -> assertEquals(OrderStatus.PROCESSING, order.getOrderStatus()),
        () -> assertEquals(PaymentStatus.FAILED, order.getPaymentStatus()),
        () -> assertFalse(order.isPaymentSuccess())
    );
  }

  @Test
  @DisplayName("결제 대기 상태가 아닌 주문에 대해 결제 처리 시 예외가 발생해야 함")
  void testCompletePaymentWithInvalidStatus() {
    order.addOrderItem("PROD001", 2, 1000);
    order.initPayment(PaymentMethod.CREDIT_CARD);

    order.completePayment(true);

    assertThrows(IllegalOrderStateException.class, () -> order.completePayment(true));
  }

  @Test
  @DisplayName("주문 취소 시 주문 상태와 결제 상태가 올바르게 변경되어야 함")
  void testCancelOrder() {
    order.addOrderItem("PROD001", 2, 1000);
    order.initPayment(PaymentMethod.CREDIT_CARD);
    order.completePayment(true);

    order.cancel();

    assertAll(
        () -> assertEquals(OrderStatus.CANCELLED, order.getOrderStatus()),
        () -> assertEquals(PaymentStatus.REFUNDED, order.getPaymentStatus())
    );
  }

  @Test
  @DisplayName("주문 완료 시 주문 상태가 올바르게 변경되어야 함")
  void testCompleteOrder() {
    order.addOrderItem("PROD001", 2, 1000);
    order.initPayment(PaymentMethod.CREDIT_CARD);
    order.completePayment(true);

    order.completeOrder();

    assertEquals(OrderStatus.COMPLETED, order.getOrderStatus());
  }

  @Test
  @DisplayName("처리 중이 아닌 주문 완료 시 예외가 발생해야 함")
  void testCompleteOrderWithInvalidStatus() {
    order.addOrderItem("PROD001", 2, 1000);
    order.initPayment(PaymentMethod.CREDIT_CARD);

    assertThrows(IllegalOrderStateException.class, () -> order.completeOrder());
  }

  @Test
  @DisplayName("결제가 완료되지 않은 주문 완료 시 예외가 발생해야 함")
  void testCompleteOrderWithIncompletePayment() {
    order.addOrderItem("PROD001", 2, 1000);
    order.initPayment(PaymentMethod.CREDIT_CARD);
    order.completePayment(false);

    assertThrows(IllegalOrderStateException.class, () -> order.completeOrder());
  }

  @Test
  @DisplayName("완료된 주문 취소 시 예외가 발생해야 함")
  void testCancelCompletedOrder() {
    order.addOrderItem("PROD001", 2, 1000);
    order.initPayment(PaymentMethod.CREDIT_CARD);
    order.completePayment(true);
    order.completeOrder();

    assertThrows(IllegalOrderStateException.class, () -> order.cancel());
  }

  @Test
  @DisplayName("총 주문 금액이 올바르게 계산되어야 함")
  void testCalculateTotalAmount() {
    order.addOrderItem("PROD001", 2, 1000);
    order.addOrderItem("PROD002", 1, 500);

    assertEquals(2500, order.calculateTotalAmount());
  }

  @Test
  @DisplayName("주문 상품 개수가 올바르게 계산되어야 함")
  void testCountProducts() {
    order.addOrderItem("PROD001", 2, 1000);
    order.addOrderItem("PROD002", 1, 500);

    assertEquals(2, order.countProducts());
  }

  @Test
  @DisplayName("총 주문 수량이 올바르게 계산되어야 함")
  void testCalculateTotalItemQuantity() {
    order.addOrderItem("PROD001", 2, 1000);
    order.addOrderItem("PROD002", 3, 500);

    assertEquals(5, order.calculateTotalItemQuantity());
  }

  @ParameterizedTest
  @EnumSource(PaymentMethod.class)
  @DisplayName("모든 결제 방식에 대해 결제 초기화가 정상 작동해야 함")
  void testInitPaymentWithDifferentMethods(PaymentMethod method) {
    order.addOrderItem("PROD001", 2, 1000);
    order.initPayment(method);

    assertEquals(method, order.getPaymentMethod());
  }

}