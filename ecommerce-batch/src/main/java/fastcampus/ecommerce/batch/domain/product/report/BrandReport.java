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
@Table(name = "brand_reports")
@Data
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@IdClass(BrandReportId.class)
public class BrandReport {

  @Id
  private LocalDate statDate;
  @Id
  private String brand;
  private Long productCount;
  private Double avgSalesPrice;
  private Integer maxSalesPrice;
  private Integer minSalesPrice;
  private Long totalStockQuantity;
  private Double avgStockQuantity;
  private Long totalStockValue;

  public BrandReport(String brand, Long productCount, Double avgSalesPrice, Integer maxSalesPrice,
      Integer minSalesPrice, Long totalStockQuantity, Double avgStockQuantity,
      Long totalStockValue) {
    this(LocalDate.now(), brand, productCount, avgSalesPrice, maxSalesPrice, minSalesPrice,
        totalStockQuantity, avgStockQuantity, totalStockValue);
  }
}
