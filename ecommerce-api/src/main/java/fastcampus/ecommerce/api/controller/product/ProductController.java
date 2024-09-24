package fastcampus.ecommerce.api.controller.product;

import fastcampus.ecommerce.api.service.product.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping("/v1/products")
@RestController
@RequiredArgsConstructor
public class ProductController {

  private final ProductService productService;

  @GetMapping("/{productId}")
  public ProductResponse getProduct(@PathVariable String productId) {
    return ProductResponse.from(productService.findProduct(productId));
  }

  @GetMapping("")
  public Page<ProductResponse> getProducts(@RequestParam(defaultValue = "0") int page,
      @RequestParam(defaultValue = "10") int size,
      @RequestParam(defaultValue = "productId,asc") String[] sort) {
    Sort.Direction direction =
        sort[1].equalsIgnoreCase("desc") ? Sort.Direction.DESC : Sort.Direction.ASC;
    Pageable pageable = PageRequest.of(page, size, Sort.by(direction, sort[0]));
    return productService.getAllProducts(pageable).map(ProductResponse::from);
  }

}
