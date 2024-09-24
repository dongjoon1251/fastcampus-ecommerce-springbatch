package fastcampus.ecommerce.api.service.product;


public class ProductNotFoundException extends RuntimeException {

  public ProductNotFoundException(String productId) {
    super("상품을 찾을 수 없습니다: " + productId);
  }
}
