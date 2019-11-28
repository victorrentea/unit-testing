package ro.victor.unittest.db.search;

import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ArrayBlockingQueue;

import static org.apache.commons.lang3.StringUtils.isNotEmpty;

@Repository
@RequiredArgsConstructor
public class ProductRepoSearchImpl implements ProductRepoSearch {
    private final EntityManager em;

    @Override
    public List<Product> search(ProductSearchCriteria criteria) {
//        String jpql = "SELECT new ro.victor.unittest.db.search.ProductSearchResult(p.id, p.name) FROM Product p WHERE 1=1 ";
        String jpql = "SELECT p FROM Product p WHERE 1=1 ";

        Map<String, Object> paramMap = new HashMap<>();
        if (isNotEmpty(criteria.name)) {
            jpql += "  AND UPPER(p.name) LIKE UPPER(:name)   ";
            paramMap.put("name", "%" + criteria.name + "%");
        }
        if (criteria.category != null) {
            jpql += "  AND p.category = :category ";
            paramMap.put("category", criteria.category);
        }

        TypedQuery<Product> query = em.createQuery(jpql, Product.class);
        for (String paramName : paramMap.keySet()) {
            query.setParameter(paramName, paramMap.get(paramName));
        }
        return query.getResultList();
    }
}
