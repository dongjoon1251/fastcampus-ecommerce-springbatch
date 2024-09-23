package fastcampus.ecommerce.batch.util;

import static org.assertj.core.api.Assertions.assertThat;

import java.time.LocalDate;
import java.time.LocalDateTime;
import org.junit.jupiter.api.Test;

class DateTimeUtilsTest {

  @Test
  void testToLocalDate() {
    String date = "2024-09-21";

    LocalDate result = DateTimeUtils.toLocalDate(date);

    assertThat(result).isEqualTo(LocalDate.of(2024, 9, 21));
  }

  @Test
  void testToLocalDateTime() {
    String dateTimeStr = "2024-09-21 10:15:30.123";

    LocalDateTime result = DateTimeUtils.toLocalDateTime(dateTimeStr);

    assertThat(result).isEqualTo(LocalDateTime.of(2024, 9, 21, 10, 15, 30, 123000000));
  }
}
