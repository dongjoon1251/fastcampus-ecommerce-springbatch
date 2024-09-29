package fastcampus.ecommerce.batch.service.product;

import fastcampus.ecommerce.batch.domain.product.Product;
import fastcampus.ecommerce.batch.domain.product.ProductRepository;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ProductService {

  private final ProductRepository productRepository;

  public Long countProducts() {
    return productRepository.count();
  }

  public void save(Product product) {
    productRepository.save(product);
  }

  public List<String> getProductIds() {
    return productRepository.findAllProjectIds();
  }
}
