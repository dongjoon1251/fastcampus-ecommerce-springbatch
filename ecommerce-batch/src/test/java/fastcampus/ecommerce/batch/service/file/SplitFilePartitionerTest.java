package fastcampus.ecommerce.batch.service.file;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;
import org.springframework.batch.item.ExecutionContext;

class SplitFilePartitionerTest {

  @TempDir
  Path tempDir;

  private List<File> createTempFiles(int count) throws IOException {
    List<File> files = new ArrayList<>();
    for (int i = 0; i < count; i++) {
      File file = File.createTempFile("test", ".tmp", tempDir.toFile());
      files.add(file);
    }
    return files;
  }

  @Test
  void testPartitionWithMultipleFiles() throws IOException {
    List<File> files = createTempFiles(3);
    SplitFilePartitioner partitioner = new SplitFilePartitioner(files);

    Map<String, ExecutionContext> result = partitioner.partition(2);

    assertAll(
        () -> assertThat(result).hasSize(3),
        () -> assertThat(result).isEqualTo(Map.of(
            "partition0", new ExecutionContext(Map.of("file", files.getFirst())),
            "partition1", new ExecutionContext(Map.of("file", files.get(1))),
            "partition2", new ExecutionContext(Map.of("file", files.get(2)))
        ))
    );
  }

  @Test
  void testPartitionWithEmptyFileList() {
    SplitFilePartitioner partitioner = new SplitFilePartitioner(new ArrayList<>());

    Map<String, ExecutionContext> result = partitioner.partition(2);

    assertThat(result).isEqualTo(Map.of());
  }

  @Test
  void testPartitionWithSingleFile() throws IOException {
    List<File> files = createTempFiles(1);
    SplitFilePartitioner partitioner = new SplitFilePartitioner(files);

    Map<String, ExecutionContext> result = partitioner.partition(2);

    assertAll(
        () -> assertThat(result).hasSize(1),
        () -> assertThat(result).isEqualTo(Map.of(
            "partition0", new ExecutionContext(Map.of("file", files.getFirst()))
        ))
    );
  }

  @Test
  void testPartitionIgnoresGridSize() throws IOException {
    List<File> files = createTempFiles(5);
    SplitFilePartitioner partitioner = new SplitFilePartitioner(files);

    Map<String, ExecutionContext> result = partitioner.partition(2);

    assertAll(
        () -> assertThat(result).hasSize(5),
        () -> assertThat(result).isEqualTo(Map.of(
            "partition0", new ExecutionContext(Map.of("file", files.getFirst())),
            "partition1", new ExecutionContext(Map.of("file", files.get(1))),
            "partition2", new ExecutionContext(Map.of("file", files.get(2))),
            "partition3", new ExecutionContext(Map.of("file", files.get(3))),
            "partition4", new ExecutionContext(Map.of("file", files.get(4)))
        ))
    );
  }
}