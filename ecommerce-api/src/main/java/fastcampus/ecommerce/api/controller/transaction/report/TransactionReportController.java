package fastcampus.ecommerce.api.controller.transaction.report;

import fastcampus.ecommerce.api.service.transaction.report.TransactionReportService;
import java.time.LocalDate;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.format.annotation.DateTimeFormat.ISO;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping("/v1/transactions/reports")
@RestController
@RequiredArgsConstructor
public class TransactionReportController {

  private final TransactionReportService transactionReportService;

  @GetMapping("")
  public TransactionReportResponses getTransactionReports(
      @RequestParam("dt") @DateTimeFormat(iso = ISO.DATE) LocalDate date) {
    return TransactionReportResponses.from(transactionReportService.findByDate(date));
  }

}
