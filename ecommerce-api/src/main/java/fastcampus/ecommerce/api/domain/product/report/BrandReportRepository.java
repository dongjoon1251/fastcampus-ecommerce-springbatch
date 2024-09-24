package fastcampus.ecommerce.api.domain.product.report;

import java.time.LocalDate;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BrandReportRepository extends JpaRepository<BrandReport, BrandReportId> {

  List<BrandReport> findAllByStatDate(LocalDate statDate);
}
