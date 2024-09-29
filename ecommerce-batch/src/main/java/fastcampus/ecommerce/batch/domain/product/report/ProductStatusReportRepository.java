package fastcampus.ecommerce.batch.domain.product.report;

import java.time.LocalDate;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductStatusReportRepository extends
    JpaRepository<ProductStatusReport, ProductStatusReportId> {


  Long countByStatDate(LocalDate statDate);
}
