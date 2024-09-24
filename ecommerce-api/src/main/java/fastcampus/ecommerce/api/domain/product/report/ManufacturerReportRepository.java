package fastcampus.ecommerce.api.domain.product.report;

import java.time.LocalDate;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ManufacturerReportRepository extends
    JpaRepository<ManufacturerReport, ManufacturerReportId> {

  List<ManufacturerReport> findAllByStatDate(LocalDate statDate);

}
