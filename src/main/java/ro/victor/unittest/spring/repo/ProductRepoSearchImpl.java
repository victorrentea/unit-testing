package ro.victor.unittest.spring.repo;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import ro.victor.unittest.spring.facade.ProductSearchResult;
import ro.victor.unittest.spring.facade.ProductSearchCriteria;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.apache.commons.lang3.StringUtils.isNotEmpty;

@Repository
@RequiredArgsConstructor
public class ProductRepoSearchImpl implements ProductRepoSearch {
    private final EntityManager em;

    @Override
    public List<ProductSearchResult> search(ProductSearchCriteria criteria) {
        String jpql = "SELECT new ro.victor.unittest.spring.facade.ProductSearchResult(p.id, p.name)" +
                " FROM Product p " +
                " WHERE 1=1 ";

        Map<String, Object> paramMap = new HashMap<>();
        if (isNotEmpty(criteria.name)) {
            jpql += "  AND UPPER(p.name) LIKE UPPER(:name)   ";
            paramMap.put("name", "%" +criteria.name +"%"); // <---- bug
        }

        TypedQuery<ProductSearchResult> query = em.createQuery(jpql, ProductSearchResult.class);
        for (String paramName : paramMap.keySet()) {
            query.setParameter(paramName, paramMap.get(paramName));
        }
        return query.getResultList();
    }
}
