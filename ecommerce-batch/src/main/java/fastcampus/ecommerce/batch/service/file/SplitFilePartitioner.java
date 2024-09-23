package fastcampus.ecommerce.batch.service.file;

import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.springframework.batch.core.partition.support.Partitioner;
import org.springframework.batch.item.ExecutionContext;

public class SplitFilePartitioner implements Partitioner {

  private final List<File> splitFiles;

  public SplitFilePartitioner(List<File> splitFiles) {
    this.splitFiles = splitFiles;
  }

  @Override
  public Map<String, ExecutionContext> partition(int gridSize) {
    Map<String, ExecutionContext> partitionMap = new HashMap<>();

    for (int i = 0; i < splitFiles.size(); i++) {
      ExecutionContext context = new ExecutionContext();
      context.put("file", splitFiles.get(i));
      partitionMap.put("partition" + i, context);
    }

    return partitionMap;
  }
}
