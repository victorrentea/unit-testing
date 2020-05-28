package ro.victor.unittest.spring.repo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;
import ro.victor.unittest.spring.domain.Product;
import ro.victor.unittest.spring.facade.ProductSearchCriteria;
import ro.victor.unittest.spring.facade.ProductSearchResult;

import javax.persistence.TypedQuery;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.apache.commons.lang3.StringUtils.isNotEmpty;

@Repository
public class ProductRepo {
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

    return jdbc.query(sql, paramMap, new RowMapper<ProductSearchResult>() {
      @Override
      public ProductSearchResult mapRow(ResultSet rs, int rowNum) throws SQLException {
        ProductSearchResult result = new ProductSearchResult();
        result.setId(rs.getLong(1));
        result.setName(rs.getString(2));
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
    jdbc.update("INSERT INTO PRODUCT(id, name) VALUES(:id, :name)",map);
  }

  public Integer count() {
    return jdbc.queryForObject("SELECT COUNT(*) FROM PRODUCT", Collections.emptyMap(), Integer.class);
  }
}
