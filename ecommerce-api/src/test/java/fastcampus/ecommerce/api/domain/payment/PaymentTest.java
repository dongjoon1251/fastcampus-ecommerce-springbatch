package fastcampus.ecommerce.api.domain.payment;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import fastcampus.ecommerce.api.domain.order.Order;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;

class PaymentTest {

  private Order testOrder;

  @BeforeEach
  void setUp() {
    testOrder = Order.createOrder(1L);
  }

  @Test
  @DisplayName("결제 생성 시 초기 상태가 올바르게 설정되어야 함")
  void testCreatePayment() {
    Payment payment = Payment.createPayment(PaymentMethod.CREDIT_CARD, 1000, testOrder);

    assertAll(
        () -> assertEquals(PaymentMethod.CREDIT_CARD, payment.getPaymentMethod()),
        () -> assertEquals(PaymentStatus.PENDING, payment.getPaymentStatus()),
        () -> assertEquals(1000, payment.getAmount()),
        () -> assertEquals(testOrder, payment.getOrder()),
        () -> assertNotNull(payment.getPaymentDate()),
        () -> assertNull(payment.getPaymentId())
    );
  }

  @ParameterizedTest
  @EnumSource(PaymentMethod.class)
  @DisplayName("모든 결제 방식에 대해 결제 생성이 정상 작동해야 함")
  void testCreatePaymentWithDifferentMethods(PaymentMethod method) {
    Payment payment = Payment.createPayment(method, 1000, testOrder);

    assertEquals(method, payment.getPaymentMethod());
  }

  @Test
  @DisplayName("결제 완료 시 상태가 COMPLETED로 변경되어야 함")
  void testCompletePayment() {
    Payment payment = Payment.createPayment(PaymentMethod.CREDIT_CARD, 1000, testOrder);

    payment.complete();

    assertEquals(PaymentStatus.COMPLETED, payment.getPaymentStatus());
  }

  @Test
  @DisplayName("PENDING 상태가 아닌 결제 완료 시도 시 예외가 발생해야 함")
  void testCompletePaymentNotPending() {
    Payment payment = Payment.createPayment(PaymentMethod.CREDIT_CARD, 1000, testOrder);

    payment.complete();

    assertThrows(IllegalPaymentStateException.class, payment::complete);
  }

  @Test
  @DisplayName("결제 실패 시 상태가 FAILED로 변경되어야 함")
  void testFailPayment() {
    Payment payment = Payment.createPayment(PaymentMethod.CREDIT_CARD, 1000, testOrder);

    payment.fail();

    assertEquals(PaymentStatus.FAILED, payment.getPaymentStatus());
  }

  @Test
  @DisplayName("PENDING 상태가 아닌 결제 실패 시도 시 예외가 발생해야 함")
  void testFailPaymentNotPending() {
    Payment payment = Payment.createPayment(PaymentMethod.CREDIT_CARD, 1000, testOrder);

    payment.fail();

    assertThrows(IllegalPaymentStateException.class, payment::fail);
  }

  @Test
  @DisplayName("완료된 결제 취소 시 상태가 REFUNDED로 변경되어야 함")
  void testCancelCompletedPayment() {
    Payment payment = Payment.createPayment(PaymentMethod.CREDIT_CARD, 1000, testOrder);
    payment.complete();

    payment.cancel();

    assertEquals(PaymentStatus.REFUNDED, payment.getPaymentStatus());
  }

  @Test
  @DisplayName("대기 중인 결제 취소 시 상태가 CANCELLED로 변경되어야 함")
  void testCancelPendingPayment() {
    Payment payment = Payment.createPayment(PaymentMethod.CREDIT_CARD, 1000, testOrder);

    payment.cancel();

    assertEquals(PaymentStatus.CANCELLED, payment.getPaymentStatus());
  }

  @Test
  @DisplayName("실패한 결제 취소 시 상태가 CANCELLED로 변경되어야 함")
  void testCancelFailedPayment() {
    Payment payment = Payment.createPayment(PaymentMethod.CREDIT_CARD, 1000, testOrder);
    payment.fail();

    payment.cancel();

    assertEquals(PaymentStatus.CANCELLED, payment.getPaymentStatus());
  }

  @Test
  @DisplayName("이미 취소된 결제 취소 시도 시 예외가 발생해야 함")
  void testCancelCancelledPayment() {
    Payment payment = Payment.createPayment(PaymentMethod.CREDIT_CARD, 1000, testOrder);

    payment.cancel();

    assertThrows(IllegalPaymentStateException.class, payment::cancel);
  }

  @Test
  @DisplayName("이미 환불된 결제 취소 시도 시 예외가 발생해야 함")
  void testCancelRefundedPayment() {
    Payment payment = Payment.createPayment(PaymentMethod.CREDIT_CARD, 1000, testOrder);
    payment.complete();

    payment.cancel();

    assertThrows(IllegalPaymentStateException.class, payment::cancel);
  }

  @Test
  @DisplayName("isSuccess 메서드는 PENDING 상태의 결제에 대해 false를 반환해야 함")
  void testIsSuccessForPendingPayment() {
    Payment payment = Payment.createPayment(PaymentMethod.CREDIT_CARD, 1000, testOrder);

    assertFalse(payment.isSuccess());
  }

  @Test
  @DisplayName("isSuccess 메서드는 COMPLETED 상태의 결제에 대해 true를 반환해야 함")
  void testIsSuccessForCompletedPayment() {
    Payment payment = Payment.createPayment(PaymentMethod.CREDIT_CARD, 1000, testOrder);

    payment.complete();

    assertTrue(payment.isSuccess());
  }

  @Test
  @DisplayName("isSuccess 메서드는 FAILED 상태의 결제에 대해 false를 반환해야 함")
  void testIsSuccessForFailedPayment() {
    Payment failedPayment = Payment.createPayment(PaymentMethod.CREDIT_CARD, 1000, testOrder);

    failedPayment.fail();
    
    assertFalse(failedPayment.isSuccess());
  }
}