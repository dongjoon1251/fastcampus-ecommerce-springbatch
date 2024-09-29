package fastcampus.ecommerce.batch.domain.product.report;

import java.time.LocalDate;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CategoryReportRepository extends JpaRepository<CategoryReport, CategoryReportId> {

  Long countByStatDate(LocalDate statDate);
}
