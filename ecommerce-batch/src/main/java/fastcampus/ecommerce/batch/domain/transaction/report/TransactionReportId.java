package fastcampus.ecommerce.batch.domain.transaction.report;

import java.io.Serializable;
import java.time.LocalDate;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TransactionReportId implements Serializable {

  private LocalDate transactionDate;
  private String transactionType;
}
