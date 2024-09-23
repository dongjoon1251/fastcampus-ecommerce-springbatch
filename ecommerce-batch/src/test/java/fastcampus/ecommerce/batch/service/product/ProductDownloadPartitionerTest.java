package fastcampus.ecommerce.batch.service.product;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.mockito.Mockito.anyString;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import fastcampus.ecommerce.batch.domain.file.PartitionedFileRepository;
import fastcampus.ecommerce.batch.util.FileUtils;
import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.batch.item.ExecutionContext;

@ExtendWith(MockitoExtension.class)
class ProductDownloadPartitionerTest {

  @Mock
  private ProductService productService;

  @Mock
  private PartitionedFileRepository partitionedFileRepository;

  private ProductDownloadPartitioner partitioner;
  private File partitionedFile;

  @BeforeEach
  void setUp() throws IOException {
    partitioner = new ProductDownloadPartitioner(productService, partitionedFileRepository);
    partitionedFile = FileUtils.createTempFile("partition", ".csv");
  }

  @Test
  void testPartitionWithMultipleProducts() throws IOException {
    List<String> productIds = Arrays.asList("1", "2", "3", "4", "5");
    when(productService.getProductIds()).thenReturn(productIds);
    when(partitionedFileRepository.putFile(anyString(), anyString(), anyString())).thenReturn(
        partitionedFile);

    Map<String, ExecutionContext> result = partitioner.partition(2);

    assertAll(
        () -> assertThat(result).hasSize(2),
        () -> assertThat(result).containsKeys("partition0", "partition1"),
        () -> assertThat(result).isEqualTo(Map.of(
            "partition0", createExecutionContext("1", "3"),
            "partition1", createExecutionContext("4", "5")
        )),
        () -> verify(partitionedFileRepository, times(2)).putFile(anyString(), anyString(),
            anyString())
    );
  }

  @Test
  void testPartitionWithSingleProduct() throws IOException {
    List<String> productIds = List.of("1");
    when(productService.getProductIds()).thenReturn(productIds);
    when(partitionedFileRepository.putFile(anyString(), anyString(), anyString())).thenReturn(
        partitionedFile);

    Map<String, ExecutionContext> result = partitioner.partition(2);

    assertAll(
        () -> assertThat(result).hasSize(1),
        () -> assertThat(result).isEqualTo(Map.of(
            "partition0", createExecutionContext("1", "1")
        )),
        () -> verify(partitionedFileRepository, times(1)).putFile(anyString(), anyString(),
            anyString())
    );
  }

  @Test
  void testPartitionWithNoProducts() {
    List<String> productIds = List.of();
    when(productService.getProductIds()).thenReturn(productIds);

    Map<String, ExecutionContext> result = partitioner.partition(2);

    assertAll(
        () -> assertThat(result).isEmpty(),
        () -> verify(partitionedFileRepository, never()).putFile(anyString(), anyString(),
            anyString())
    );
  }

  @Test
  void testPartitionWithLargeGridSize() throws IOException {
    List<String> productIds = Arrays.asList("1", "2", "3", "4", "5");
    when(productService.getProductIds()).thenReturn(productIds);
    when(partitionedFileRepository.putFile(anyString(), anyString(), anyString())).thenReturn(
        partitionedFile);

    Map<String, ExecutionContext> result = partitioner.partition(10);

    assertAll(
        () -> assertThat(result).hasSize(5),
        () -> assertThat(result).isEqualTo(Map.of(
            "partition0", createExecutionContext("1", "1"),
            "partition1", createExecutionContext("2", "2"),
            "partition2", createExecutionContext("3", "3"),
            "partition3", createExecutionContext("4", "4"),
            "partition4", createExecutionContext("5", "5")
        )),
        () -> verify(partitionedFileRepository, times(5)).putFile(anyString(), anyString(),
            anyString())
    );
  }

  private ExecutionContext createExecutionContext(String minId, String maxId) {
    ExecutionContext context = new ExecutionContext();
    context.putString("minId", minId);
    context.putString("maxId", maxId);
    context.put("file", partitionedFile);
    return context;
  }
}