package fastcampus.ecommerce.api.domain.product.report;

import java.time.LocalDate;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductStatusReportRepository extends
    JpaRepository<ProductStatusReport, ProductStatusReportId> {

  List<ProductStatusReport> findAllByStatDate(LocalDate statDate);

}
