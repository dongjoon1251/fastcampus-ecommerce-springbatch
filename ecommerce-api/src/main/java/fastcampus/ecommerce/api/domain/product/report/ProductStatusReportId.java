package fastcampus.ecommerce.api.domain.product.report;

import fastcampus.ecommerce.api.domain.product.ProductStatus;
import java.io.Serializable;
import java.time.LocalDate;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProductStatusReportId implements Serializable {

  private LocalDate statDate;
  private ProductStatus productStatus;

}
