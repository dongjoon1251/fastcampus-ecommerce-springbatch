package fastcampus.ecommerce.api.domain.transaction.report;


import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.IdClass;
import jakarta.persistence.Table;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "transaction_reports")
@Data
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@IdClass(TransactionReportId.class)
public class TransactionReport implements Serializable {

  @Id
  private LocalDate transactionDate;
  @Id
  private String transactionType;
  private Long transactionCount;
  private Long totalAmount;
  private Long customerCount;
  private Long orderCount;
  private Long paymentMethodCount;
  private BigDecimal avgProductCount;
  private Long totalItemQuantity;

}