package fastcampus.ecommerce.api.domain.order;


import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Entity
@Table(name = "order_items")
@Data
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class OrderItem {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long orderItemId;

  private Integer quantity;
  private Integer unitPrice;
  private String productId;

  @ManyToOne
  @JoinColumn(name = "order_id")
  @ToString.Exclude
  private Order order;

  private OrderItem(Integer quantity, Integer unitPrice, String productId, Order order) {
    this.quantity = quantity;
    this.unitPrice = unitPrice;
    this.productId = productId;
    this.order = order;
  }

  public static OrderItem createOrderItem(String productId, Integer quantity, Integer unitPrice,
      Order order) {
    return new OrderItem(quantity, unitPrice, productId, order);
  }


}