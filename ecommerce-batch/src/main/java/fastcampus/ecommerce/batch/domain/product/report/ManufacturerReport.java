package fastcampus.ecommerce.batch.domain.product.report;

import java.math.BigDecimal;
import java.time.LocalDate;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@Getter
@Setter
@NoArgsConstructor
public class ManufacturerReport {

  private LocalDate statDate = LocalDate.now();
  private String manufacturer;
  private Integer productCount;
  private BigDecimal avgSalesPrice;
  private Integer totalStockQuantity;
}
