package victor.testing.spring.repo;

import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import victor.testing.spring.domain.Product;
import victor.testing.spring.web.dto.ProductSearchResult;
import victor.testing.spring.web.dto.ProductSearchCriteria;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


@Repository
@RequiredArgsConstructor
public class ProductRepoSearchImpl implements ProductRepoSearch {
    private final EntityManager em;

    @Override
    public List<ProductSearchResult> search(ProductSearchCriteria criteria) {
        String jpql = "SELECT new victor.testing.spring.web.dto.ProductSearchResult(p.id, p.name)" +
                " FROM Product p " +
                " WHERE 1=1 ";

        Map<String, Object> paramMap = new HashMap<>();

        if (StringUtils.isNotEmpty(criteria.name)) {
            jpql += "  AND p.name = :name   ";
            paramMap.put("name", criteria.name);
        }

        TypedQuery<ProductSearchResult> query = em.createQuery(jpql, ProductSearchResult.class);
        for (String paramName : paramMap.keySet()) {
            query.setParameter(paramName, paramMap.get(paramName));
        }
        return query.getResultList();
    }
}

@RequiredArgsConstructor
@Service
class SomeService {
    private final JdbcTemplate jdbc;
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void method() {
//        jdbc.update("INSERT INTO ")
    }
}