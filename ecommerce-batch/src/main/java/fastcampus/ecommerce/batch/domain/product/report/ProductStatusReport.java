package fastcampus.ecommerce.batch.domain.product.report;


import java.math.BigDecimal;
import java.time.LocalDate;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class ProductStatusReport {

  private LocalDate statDate = LocalDate.now();
  private String productStatus;
  private Integer productCount;
  private BigDecimal avgStockQuantity;
}
