package fastcampus.ecommerce.batch.domain.product.report;


import fastcampus.ecommerce.batch.domain.product.ProductStatus;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Id;
import jakarta.persistence.IdClass;
import jakarta.persistence.Table;
import java.time.LocalDate;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "product_status_reports")
@Data
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@IdClass(ProductStatusReportId.class)
public class ProductStatusReport {

  @Id
  private LocalDate statDate;
  @Id
  @Enumerated(EnumType.STRING)
  private ProductStatus productStatus;
  private Long productCount;
  private Double avgStockQuantity;

  public ProductStatusReport(ProductStatus productStatus, Long productCount,
      Double avgStockQuantity) {
    this(LocalDate.now(), productStatus, productCount, avgStockQuantity);
  }
}
