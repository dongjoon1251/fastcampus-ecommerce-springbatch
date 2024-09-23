package fastcampus.ecommerce.batch.util;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.api.io.TempDir;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@ExtendWith(SpringExtension.class)
class FileUtilsTest {

  @Value("classpath:/data/products_for_upload.csv")
  private Resource csvResource;
  @TempDir
  Path tempDir;

  @Test
  void splitCsv() throws IOException {
    List<File> files = FileUtils.splitCsv(csvResource.getFile(), 2);

    assertThat(files).hasSize(2);
  }

  @Test
  void createTempFile() throws IOException {
    File file = FileUtils.createTempFile("prefix", "suffix");

    assertThat(file.exists()).isTrue();
  }

  @Test
  void testMergeFiles_SuccessfulMerge() throws IOException {
    File file1 = createTempFile("part1.txt", "Content 1\n");
    File file2 = createTempFile("part2.txt", "Content 2\n");
    File outputFile = new File(tempDir.toFile(), "merged.txt");
    String header = "Header";

    FileUtils.mergeFiles(header, Arrays.asList(file1, file2), outputFile);

    assertThat(Files.readAllLines(outputFile.toPath())).containsExactly("Header", "Content 1",
        "Content 2");
  }

  @Test
  void testMergeFiles_EmptyInputFiles() throws IOException {
    File file1 = createTempFile("empty1.txt", "");
    File file2 = createTempFile("empty2.txt", "");
    File outputFile = new File(tempDir.toFile(), "merged_empty.txt");
    String header = "Header";

    FileUtils.mergeFiles(header, Arrays.asList(file1, file2), outputFile);

    assertThat(Files.readAllLines(outputFile.toPath())).containsExactly("Header");
  }

  @Test
  void testMergeFiles_NoInputFiles() throws IOException {
    File outputFile = new File(tempDir.toFile(), "merged_no_input.txt");
    String header = "Header";

    FileUtils.mergeFiles(header, List.of(), outputFile);

    assertThat(Files.readAllLines(outputFile.toPath())).containsExactly("Header");
  }

  @Test
  void testMergeFiles_NonExistentInputFile() throws IOException {
    File existingFile = createTempFile("existing.txt", "Content\n");
    File nonExistentFile = new File(tempDir.toFile(), "non_existent.txt");
    File outputFile = new File(tempDir.toFile(), "merged_with_non_existent.txt");
    String header = "Header";

    assertThatThrownBy(() ->
        FileUtils.mergeFiles(header, Arrays.asList(existingFile, nonExistentFile), outputFile)
    ).isInstanceOf(RuntimeException.class)
        .hasCauseInstanceOf(IOException.class);
  }

  private File createTempFile(String fileName, String content) throws IOException {
    File file = new File(tempDir.toFile(), fileName);
    Files.write(file.toPath(), content.getBytes());
    return file;
  }
}