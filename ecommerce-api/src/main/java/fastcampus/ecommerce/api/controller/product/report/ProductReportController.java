package fastcampus.ecommerce.api.controller.product.report;

import fastcampus.ecommerce.api.service.product.report.ProductReportService;
import java.time.LocalDate;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.format.annotation.DateTimeFormat.ISO;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping("/v1/products/reports")
@RestController
@RequiredArgsConstructor
public class ProductReportController {

  private final ProductReportService productReportService;

  @GetMapping("")
  public ProductReportResponse getProductReports(
      @RequestParam("dt") @DateTimeFormat(iso = ISO.DATE) LocalDate date) {
    return ProductReportResponse.from(productReportService.findReports(date));
  }

}
