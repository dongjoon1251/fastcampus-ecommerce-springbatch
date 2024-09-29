package fastcampus.ecommerce.batch.domain.product.report;

import java.time.LocalDate;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BrandReportRepository extends JpaRepository<BrandReport, BrandReportId> {


  Long countByStatDate(LocalDate statDate);
}
