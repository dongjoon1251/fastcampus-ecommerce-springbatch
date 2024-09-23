package fastcampus.ecommerce.batch.service.product;

import fastcampus.ecommerce.batch.domain.product.Product;
import java.sql.Timestamp;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ProductService {

  private final JdbcTemplate jdbcTemplate;

  public Long countProducts() {
    return jdbcTemplate.queryForObject("select count(*) from products", Long.class);
  }

  public void save(Product product) {
    String sql = "INSERT INTO products (product_id, seller_id, category, product_name, " +
        "sales_start_date, sales_end_date, product_status, brand, manufacturer, " +
        "sales_price, stock_quantity, created_at, updated_at) " +
        "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

    jdbcTemplate.update(sql,
        product.getProductId(),
        product.getSellerId(),
        product.getCategory(),
        product.getProductName(),
        product.getSalesStartDate(),
        product.getSalesEndDate(),
        product.getProductStatus(),
        product.getBrand(),
        product.getManufacturer(),
        product.getSalesPrice(),
        product.getStockQuantity(),
        Timestamp.valueOf(product.getCreatedAt()),
        Timestamp.valueOf(product.getUpdatedAt())
    );
  }

  public List<String> getProductIds() {
    return jdbcTemplate.queryForList("select product_id from products", String.class);
  }
}
