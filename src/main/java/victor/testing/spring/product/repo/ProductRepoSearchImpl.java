package victor.testing.spring.product.repo;

import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Repository;
import victor.testing.spring.product.api.dto.ProductSearchResult;
import victor.testing.spring.product.api.dto.ProductSearchCriteria;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


@Repository
@RequiredArgsConstructor
public class ProductRepoSearchImpl implements ProductRepoSearch {
    private final EntityManager em;

    @Override
    public List<ProductSearchResult> search(ProductSearchCriteria criteria) {
        List<String> jpqlParts = new ArrayList<>();
        jpqlParts.add("SELECT new victor.testing.spring.web.dto.ProductSearchResult(p.id, p.name)" +
                " FROM Product p " +
                " WHERE 1=1");

        Map<String, Object> paramMap = new HashMap<>();

        if (StringUtils.isNotEmpty(criteria.name)) {
//            jpqlParts.add("AND p.name = :name");
            jpqlParts.add("AND UPPER(p.name) LIKE UPPER('%' || :name || '%')"); // = contains, ignoring case
            paramMap.put("name", criteria.name);
        }

        if (criteria.supplierId != null) {
            jpqlParts.add("AND p.supplier.id = :supplierId");
            paramMap.put("supplierId", criteria.supplierId);
        }

        if (criteria.category != null) {
            jpqlParts.add(" AND p.category = :category");
            paramMap.put("category", criteria.category);
        }

        String jqpl = String.join(" ", jpqlParts);
        TypedQuery<ProductSearchResult> query = em.createQuery(jqpl, ProductSearchResult.class);
        for (String paramName : paramMap.keySet()) {
            query.setParameter(paramName, paramMap.get(paramName));
        }
        return query.getResultList();
    }
}
