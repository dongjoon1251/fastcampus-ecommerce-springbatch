package fastcampus.ecommerce.batch.util;

import static org.assertj.core.api.Assertions.assertThat;

import java.time.LocalDate;
import org.junit.jupiter.api.Test;

class DateTimeUtilsTest {

  @Test
  void testToLocalDate() {
    String date = "2024-09-21";

    LocalDate result = DateTimeUtils.toLocalDate(date);

    assertThat(result).isEqualTo(LocalDate.of(2024, 9, 21));
  }

}
