package victor.testing.spring.repo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;
import victor.testing.spring.domain.Product;
import victor.testing.spring.facade.ProductSearchCriteria;
import victor.testing.spring.facade.ProductSearchResult;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.apache.commons.lang3.StringUtils.isNotBlank;

@Repository
public class ProductRepoJdbc {
  @Autowired
  private NamedParameterJdbcTemplate jdbc;

  //  @Query("SELECT p FROM Product p where p.name = ?1")
  public Product findByName(String name) {
    return null; // TODO
  }


  public List<ProductSearchResult> search(ProductSearchCriteria criteria) {
    // language=sql
    String sql = "SELECT p.id, p.name" +
        " FROM PRODUCT p " +
        " WHERE 1=1 ";

    Map<String, Object> paramMap = new HashMap<>();

    if (isNotBlank(criteria.name)) {
      sql += " AND UPPER(p.name) LIKE UPPER(:name) ";
      paramMap.put("name",  "%" + criteria.name + "%");
    }

    return jdbc.query(sql, paramMap, new RowMapper<ProductSearchResult>() {
      @Override
      public ProductSearchResult mapRow(ResultSet rs, int rowNum) throws SQLException {
        ProductSearchResult result = new ProductSearchResult();
        result.id = rs.getLong(1);
        result.name = rs.getString(2);
        return result;
      }
    });
  }

  public void save(Product product) {
    Long id = jdbc.queryForObject("SELECT hibernate_sequence.nextval from DUAL", Collections.emptyMap(), Long.class);
    product.setId(id);
    Map<String, Object> map = new HashMap<>();
    map.put("id", product.getId());
    map.put("name", product.getName());
    map.put("category", product.getCategory());
    jdbc.update("INSERT INTO PRODUCT(id, name, category) VALUES(:id, :name, :category)",map);
  }

  public Integer count() {
    return jdbc.queryForObject("SELECT COUNT(*) FROM PRODUCT", Collections.emptyMap(), Integer.class);
  }
}
