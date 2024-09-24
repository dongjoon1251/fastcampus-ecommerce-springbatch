package fastcampus.ecommerce.api.domain.product.report;

import java.io.Serializable;
import java.time.LocalDate;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ManufacturerReportId implements Serializable {

  private LocalDate statDate;
  private String manufacturer;

}
