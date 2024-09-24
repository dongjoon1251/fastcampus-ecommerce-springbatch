package fastcampus.ecommerce.api.domain.product.report;

import java.time.LocalDate;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CategoryReportRepository extends JpaRepository<CategoryReport, CategoryReportId> {

  List<CategoryReport> findAllByStatDate(LocalDate statDate);

}
