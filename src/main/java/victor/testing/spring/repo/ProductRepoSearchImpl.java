package victor.testing.spring.repo;

import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Repository;
import victor.testing.spring.domain.Product;
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
    public List<Product> search(ProductSearchCriteria criteria) {
        String jpql = "SELECT p " +
                " FROM Product p " +
                " WHERE 1=1 ";

        Map<String, Object> paramMap = new HashMap<>();

        if (StringUtils.isNotEmpty(criteria.name)) {
            jpql += "  AND UPPER(p.name) LIKE UPPER('%' || :name ||'%')   ";
            paramMap.put("name", criteria.name);
        }

        TypedQuery<Product> query = em.createQuery(jpql, Product.class);
        for (String paramName : paramMap.keySet()) {
            query.setParameter(paramName, paramMap.get(paramName));
        }
        return query.getResultList();
    }
}
