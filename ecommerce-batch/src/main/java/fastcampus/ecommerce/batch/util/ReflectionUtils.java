package fastcampus.ecommerce.batch.util;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

public class ReflectionUtils {

  public static List<String> getFieldNames(Class<?> clazz) {
    List<String> fieldNames = new ArrayList<>();
    Field[] fields = clazz.getDeclaredFields();

    for (Field field : fields) {
      if (!java.lang.reflect.Modifier.isStatic(field.getModifiers())) {
        fieldNames.add(field.getName());
      }
    }

    return fieldNames;
  }

}
