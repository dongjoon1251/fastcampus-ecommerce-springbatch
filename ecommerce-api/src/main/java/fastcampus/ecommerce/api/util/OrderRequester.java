package fastcampus.ecommerce.api.util;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import fastcampus.ecommerce.api.controller.order.OrderItemRequest;
import fastcampus.ecommerce.api.controller.order.OrderRequest;
import fastcampus.ecommerce.api.controller.order.OrderResponse;
import fastcampus.ecommerce.api.controller.order.PaymentRequest;
import fastcampus.ecommerce.api.domain.payment.PaymentMethod;
import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class OrderRequester {


  private static final String BASE_URL = "http://localhost:8080/v1";
  private static final String ORDERS_URL = BASE_URL + "/orders";
  private static final String PRODUCTS_URL = BASE_URL + "/products";
  private static final PaymentMethod[] PAYMENT_METHODS = PaymentMethod.values();
  private static final Random RANDOM = new Random();
  private static final HttpClient HTTP_CLIENT = HttpClient.newHttpClient();
  private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();

  public static void main(String[] args) {
    int maxWorkers = 20;
    ExecutorService executor = Executors.newFixedThreadPool(maxWorkers);
    List<CompletableFuture<Void>> futures = new ArrayList<>();

    try {
      int page = 0;
      int size = 1000;
      boolean hasNextPage = true;

      while (hasNextPage && page < 10000) {
        String productsJson = fetchProducts(page, size);
        JsonNode productsNode = OBJECT_MAPPER.readTree(productsJson);
        JsonNode contentNode = productsNode.get("content");

        for (JsonNode productNode : contentNode) {
          String productId = productNode.get("productId").asText();
          int stockQuantity = productNode.get("stockQuantity").asInt();
          CompletableFuture<Void> future = CompletableFuture.runAsync(
              () -> processProduct(productId, stockQuantity), executor);
          futures.add(future);
        }

        hasNextPage = !productsNode.get("last").asBoolean();
        page++;
      }
    } catch (Exception e) {
      System.out.println("ERROR!! " + e.getMessage());
    }

    CompletableFuture.allOf(futures.toArray(new CompletableFuture[0])).join();
    executor.shutdown();
  }

  private static String fetchProducts(int page, int size) throws IOException, InterruptedException {
    String url = String.format("%s?page=%d&size=%d&sort=productId,asc", PRODUCTS_URL, page, size);
    HttpRequest request = HttpRequest.newBuilder()
        .uri(URI.create(url))
        .header("Content-Type", "application/json")
        .GET()
        .build();
    HttpResponse<String> response = HTTP_CLIENT.send(request, HttpResponse.BodyHandlers.ofString());
    return response.body();
  }

  private static void processProduct(String productId, int stockQuantity) {
    int quantity = Math.max((int) Math.floor(stockQuantity / 10.0), 1);
    int randNum = RANDOM.nextInt(16) + 1;

    OrderResponse orderResponse = createOrder(productId, quantity);
    if (orderResponse != null) {
      if (randNum % 4 < 2) {
        completePayment(orderResponse.getOrderId(), randNum % 2 == 0);
      }
      if (randNum % 8 < 4) {
        completeOrder(orderResponse.getOrderId());
      }
      if (randNum % 16 < 8) {
        cancelOrder(orderResponse.getOrderId());
      }
    }
  }

  private static OrderResponse createOrder(String productId, int quantity) {
    OrderRequest orderRequest = OrderRequest.of((long) randomCustomerId(),
        List.of(OrderItemRequest.of(productId, quantity)),
        PAYMENT_METHODS[RANDOM.nextInt(PAYMENT_METHODS.length)]);

    System.out.println("상품 ID " + productId + "로 주문 생성 중...");
    try {
      String requestBody = OBJECT_MAPPER.writeValueAsString(orderRequest);
      HttpResponse<String> response = sendPostRequest(ORDERS_URL, requestBody);
      if (response.statusCode() == 200) {
        OrderResponse orderResponse = OBJECT_MAPPER.readValue(response.body(), OrderResponse.class);
        System.out.println("상품 ID " + productId + "로 주문이 성공적으로 생성되었습니다.");
        return orderResponse;
      } else {
        System.out.println("상품 ID " + productId + "로 주문 생성 실패: " + response.body());
        return null;
      }
    } catch (Exception e) {
      System.out.println("상품 ID " + productId + "로 주문 생성 중 예외 발생: " + e.getMessage());
      return null;
    }
  }

  private static void completePayment(Long orderId, boolean isSuccess) {
    PaymentRequest paymentRequest = PaymentRequest.of(isSuccess);
    System.out.println("주문 ID " + orderId + "에 대한 결제 완료 중...");
    try {
      String requestBody = OBJECT_MAPPER.writeValueAsString(paymentRequest);
      HttpResponse<String> response = sendPostRequest(ORDERS_URL + "/" + orderId + "/payment",
          requestBody);
      if (response.statusCode() == 200) {
        if (isSuccess) {
          System.out.println("주문 ID " + orderId + "에 대한 결제 처리가 성공적으로 완료되었습니다.");
        } else {
          System.out.println("주문 ID " + orderId + "에 대한 결제 처리가 실패되었습니다.");
        }
      } else {
        System.out.println("주문 ID " + orderId + "에 대한 결제 완료 실패: " + response.body());
      }
    } catch (Exception e) {
      System.out.println("주문 ID " + orderId + "에 대한 결제 완료 중 예외 발생: " + e.getMessage());
    }
  }

  private static void completeOrder(Long orderId) {
    System.out.println("주문 ID " + orderId + "를 완료 중...");
    try {
      HttpResponse<String> response = sendPostRequest(ORDERS_URL + "/" + orderId + "/complete", "");
      if (response.statusCode() == 200) {
        System.out.println("주문 ID " + orderId + "가 성공적으로 완료되었습니다.");
      } else {
        System.out.println("주문 ID " + orderId + " 완료 실패: " + response.body());
      }
    } catch (Exception e) {
      System.out.println("주문 ID " + orderId + " 완료 중 예외 발생: " + e.getMessage());
    }
  }

  private static void cancelOrder(Long orderId) {
    System.out.println("주문 ID " + orderId + "를 취소 중...");
    try {
      HttpResponse<String> response = sendPostRequest(ORDERS_URL + "/" + orderId + "/cancel", "");
      if (response.statusCode() == 200) {
        System.out.println("주문 ID " + orderId + "가 성공적으로 취소되었습니다.");
      } else {
        System.out.println("주문 ID " + orderId + " 취소 실패: " + response.body());
      }
    } catch (Exception e) {
      System.out.println("주문 ID " + orderId + " 취소 중 예외 발생: " + e.getMessage());
    }
  }

  private static int randomCustomerId() {
    return RANDOM.nextInt(1000) + 1;
  }

  private static HttpResponse<String> sendPostRequest(String url, String body)
      throws IOException, InterruptedException {
    HttpRequest request = HttpRequest.newBuilder()
        .uri(URI.create(url))
        .header("Content-Type", "application/json")
        .POST(HttpRequest.BodyPublishers.ofString(body))
        .build();
    return HTTP_CLIENT.send(request, HttpResponse.BodyHandlers.ofString());
  }
}
