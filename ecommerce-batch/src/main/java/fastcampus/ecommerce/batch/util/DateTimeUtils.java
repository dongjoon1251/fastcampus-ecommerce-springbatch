package fastcampus.ecommerce.batch.util;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class DateTimeUtils {

  private static final DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern(
      "yyyy-MM-dd HH:mm:ss.SSS");
  private static final DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

  public static LocalDate toLocalDate(String date) {
    return LocalDate.parse(date, dateFormatter);
  }

}
