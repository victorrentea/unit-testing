package victor.testing.spring.repo;

import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Repository;
import victor.testing.spring.rest.dto.ProductSearchResult;
import victor.testing.spring.rest.dto.ProductSearchCriteria;

import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
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
        jpqlParts.add("SELECT new victor.testing.spring.rest.dto.ProductSearchResult(product.id, product.name)" +
                " FROM Product product " +
                " WHERE 1=1");

        Map<String, Object> paramMap = new HashMap<>();

        if (StringUtils.isNotEmpty(criteria.getName())) {
//            jpqlParts.add("AND product.name = :name");
            jpqlParts.add("AND UPPER(product.name) LIKE UPPER('%' || :name || '%')"); // = contains, ignoring case
            paramMap.put("name", criteria.getName());
        }

        if (criteria.getSupplierId() != null) {
            jpqlParts.add("AND product.supplier.id = :supplierId");
            paramMap.put("supplierId", criteria.getSupplierId());
        }

        if (criteria.getCategory() != null) {
            jpqlParts.add(" AND product.category = :category");
            paramMap.put("category", criteria.getCategory());
        }

        String jqpl = String.join(" ", jpqlParts);
        TypedQuery<ProductSearchResult> query = em.createQuery(jqpl, ProductSearchResult.class);
        for (String paramName : paramMap.keySet()) {
            query.setParameter(paramName, paramMap.get(paramName));
        }
        return query.getResultList();
    }
}
