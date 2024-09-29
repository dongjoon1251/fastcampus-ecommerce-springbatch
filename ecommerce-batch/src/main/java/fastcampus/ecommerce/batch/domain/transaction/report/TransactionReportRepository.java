package fastcampus.ecommerce.batch.domain.transaction.report;

import java.time.LocalDate;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TransactionReportRepository extends
    JpaRepository<TransactionReport, TransactionReportId> {

  List<TransactionReport> findAllByTransactionDate(LocalDate transactionDate);
}
