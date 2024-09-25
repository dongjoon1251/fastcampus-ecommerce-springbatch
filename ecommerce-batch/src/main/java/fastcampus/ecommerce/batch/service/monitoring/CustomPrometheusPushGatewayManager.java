package fastcampus.ecommerce.batch.service.monitoring;


import io.prometheus.client.CollectorRegistry;
import io.prometheus.client.exporter.PushGateway;
import java.io.IOException;
import java.time.Duration;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.actuate.metrics.export.prometheus.PrometheusPushGatewayManager;
import org.springframework.stereotype.Component;

@Component
public class CustomPrometheusPushGatewayManager extends PrometheusPushGatewayManager {

  private static final Logger logger = LoggerFactory.getLogger(
      CustomPrometheusPushGatewayManager.class);

  private final PushGateway pushGateway;
  private final CollectorRegistry registry;
  private final String job;

  public CustomPrometheusPushGatewayManager(
      PushGateway pushGateway,
      CollectorRegistry registry,
      @Value("${prometheus.job.name:spring-batch}") String job) {
    super(pushGateway, registry, Duration.ofSeconds(30), job, Map.of(), ShutdownOperation.POST);
    this.pushGateway = pushGateway;
    this.registry = registry;
    this.job = job;
  }

  public void pushMetrics(Map<String, String> groupingKey) {
    try {
      pushGateway.pushAdd(registry, job, groupingKey);
    } catch (IOException e) {
      logger.error(e.getMessage(), e);
    }
  }

}