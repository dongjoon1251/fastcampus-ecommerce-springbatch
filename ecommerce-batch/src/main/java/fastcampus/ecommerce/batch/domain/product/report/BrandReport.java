package fastcampus.ecommerce.batch.domain.product.report;

import java.math.BigDecimal;
import java.time.LocalDate;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class BrandReport {

  private LocalDate statDate = LocalDate.now();
  private String brand;
  private Integer productCount;
  private BigDecimal avgSalesPrice;
  private BigDecimal maxSalesPrice;
  private BigDecimal minSalesPrice;
  private Integer totalStockQuantity;
  private BigDecimal avgStockQuantity;
  private BigDecimal totalStockValue;
}
