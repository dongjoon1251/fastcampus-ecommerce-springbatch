package fastcampus.ecommerce.batch.domain.product.report;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.IdClass;
import jakarta.persistence.Table;
import java.time.LocalDate;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Entity
@Table(name = "manufacturer_reports")
@Data
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@IdClass(ManufacturerReportId.class)
public class ManufacturerReport {

  @Id
  private LocalDate statDate;
  @Id
  private String manufacturer;
  private Long productCount;
  private Double avgSalesPrice;
  private Long totalStockQuantity;

  public ManufacturerReport(String manufacturer, Long productCount, Double avgSalesPrice,
      Long totalStockQuantity) {
    this(LocalDate.now(), manufacturer, productCount, avgSalesPrice, totalStockQuantity);
  }
}
