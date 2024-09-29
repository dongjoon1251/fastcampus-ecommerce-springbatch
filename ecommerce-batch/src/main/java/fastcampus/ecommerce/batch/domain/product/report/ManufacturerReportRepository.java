package fastcampus.ecommerce.batch.domain.product.report;

import java.time.LocalDate;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ManufacturerReportRepository extends
    JpaRepository<ManufacturerReport, ManufacturerReportId> {


  Long countByStatDate(LocalDate statDate);
}
