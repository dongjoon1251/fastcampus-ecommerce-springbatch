package fastcampus.ecommerce.api.domain.payment;


import com.fasterxml.jackson.annotation.JsonIgnore;
import fastcampus.ecommerce.api.domain.order.Order;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import java.sql.Timestamp;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Entity
@Table(name = "payment")
@Data
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class Payment {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long paymentId;

  @Enumerated(EnumType.STRING)
  private PaymentMethod paymentMethod;

  @Enumerated(EnumType.STRING)
  private PaymentStatus paymentStatus;

  private Timestamp paymentDate;
  private Integer amount;

  @OneToOne
  @JoinColumn(name = "order_id")
  @ToString.Exclude
  private Order order;

  public static Payment createPayment(PaymentMethod paymentMethod, Integer amount, Order order) {
    return new Payment(null, paymentMethod, PaymentStatus.PENDING,
        new Timestamp(System.currentTimeMillis()), amount, order);
  }

  public void complete() {
    if (paymentStatus != PaymentStatus.PENDING) {
      throw new IllegalPaymentStateException("결제중에만 완료할 수 있습니다.");
    }
    paymentStatus = PaymentStatus.COMPLETED;
  }

  public void fail() {
    if (paymentStatus != PaymentStatus.PENDING) {
      throw new IllegalPaymentStateException("결제시도중에만 실패할 수 있습니다.");
    }
    paymentStatus = PaymentStatus.FAILED;
  }

  public void cancel() {
    switch (paymentStatus) {
      case COMPLETED -> paymentStatus = PaymentStatus.REFUNDED;
      case PENDING, FAILED -> paymentStatus = PaymentStatus.CANCELLED;
      case CANCELLED -> throw new IllegalPaymentStateException("이미 취소가 완료되었습니다.");
      case REFUNDED -> throw new IllegalPaymentStateException("이미 환불이 완료되었습니다.");
    }
  }

  @JsonIgnore
  public boolean isSuccess() {
    return paymentStatus == PaymentStatus.COMPLETED;
  }
}