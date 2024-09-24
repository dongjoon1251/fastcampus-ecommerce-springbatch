package fastcampus.ecommerce.api.domain.order;


import fastcampus.ecommerce.api.domain.payment.Payment;
import fastcampus.ecommerce.api.domain.payment.PaymentMethod;
import fastcampus.ecommerce.api.domain.payment.PaymentStatus;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "orders")
@Data
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class Order {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long orderId;

  private Timestamp orderDate;
  private Long customerId;

  @Enumerated(EnumType.STRING)
  private OrderStatus orderStatus;

  @OneToMany(mappedBy = "order", cascade = CascadeType.ALL)
  @Setter(AccessLevel.NONE)
  private List<OrderItem> orderItems = new ArrayList<>();

  @OneToOne(mappedBy = "order", cascade = CascadeType.ALL)
  private Payment payment;

  public static Order createOrder(Long customerId) {
    return new Order(null, new Timestamp(System.currentTimeMillis()), customerId,
        OrderStatus.PENDING_PAYMENT, new ArrayList<>(), null);
  }

  public OrderItem addOrderItem(String productId, Integer quantity, Integer unitPrice) {
    OrderItem orderItem = OrderItem.createOrderItem(productId, quantity, unitPrice, this);
    orderItems.add(orderItem);
    return orderItem;
  }

  public void initPayment(PaymentMethod paymentMethod) {
    payment = Payment.createPayment(paymentMethod, calculateTotalAmount(), this);
  }

  public void completePayment(boolean isSuccess) {
    if (orderStatus != OrderStatus.PENDING_PAYMENT) {
      throw new IllegalOrderStateException("결제를 처리할 수 없는 상태의 주문입니다.");
    }
    orderStatus = OrderStatus.PROCESSING;
    if (isSuccess) {
      payment.complete();
    } else {
      payment.fail();
    }
  }

  public void completeOrder() {
    if (orderStatus != OrderStatus.PROCESSING) {
      throw new IllegalOrderStateException("처리 중인 주문만 완료할 수 있습니다.");
    }
    if (!isPaymentSuccess()) {
      throw new IllegalOrderStateException("결제가 완료되지 않은 주문은 완료할 수 없습니다.");
    }
    orderStatus = OrderStatus.COMPLETED;
  }

  public void cancel() {
    if (orderStatus == OrderStatus.COMPLETED) {
      throw new IllegalOrderStateException("완료된 주문은 취소할 수 없습니다.");
    }
    orderStatus = OrderStatus.CANCELLED;
    payment.cancel();
  }

  public int calculateTotalAmount() {
    return orderItems.stream()
        .mapToInt(item -> item.getUnitPrice() * item.getQuantity())
        .sum();
  }

  public boolean isPaymentSuccess() {
    return payment.isSuccess();
  }

  public PaymentMethod getPaymentMethod() {
    return payment.getPaymentMethod();
  }

  public Long countProducts() {
    return (long) orderItems.size();
  }

  public Long calculateTotalItemQuantity() {
    return orderItems.stream()
        .mapToLong(OrderItem::getQuantity)
        .sum();
  }

  public Long getPaymentId() {
    return payment.getPaymentId();
  }

  public PaymentStatus getPaymentStatus() {
    return payment.getPaymentStatus();
  }

  public Timestamp getPaymentDate() {
    return payment.getPaymentDate();
  }
}