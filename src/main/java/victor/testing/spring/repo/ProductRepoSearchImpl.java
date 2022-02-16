package victor.testing.spring.repo;

import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import victor.testing.spring.web.dto.ProductSearchCriteria;
import victor.testing.spring.web.dto.ProductSearchResult;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


@RequiredArgsConstructor
public class ProductRepoSearchImpl implements ProductRepoSearch {
    private final EntityManager em;

    @Override
    public List<ProductSearchResult> search(ProductSearchCriteria criteria) {
        String jpql = "SELECT new victor.testing.spring.web.dto.ProductSearchResult(p.id, p.name)" +
                " FROM Product p " +
                " WHERE 1=1  ";

//        String superHighlyTunedNativeSQLOf2A4pagesLong /*+hint */   select NLS_LANG.UPPER(s), max over partition by ()
        // flyway / liquibase / dbmaintain : db migration tools. CREATE TABLE, ALTER TABLE < testin the incremental scripts.

        Map<String, Object> paramMap = new HashMap<>();

        if (StringUtils.isNotEmpty(criteria.name)) {
            jpql += "  AND p.name = :name   ";
            paramMap.put("name", criteria.name);
        }

        if (criteria.supplierId != null) {
            jpql += "  AND p.supplier.id = :supplierId   ";
            paramMap.put("supplierId", criteria.supplierId);
        }

        TypedQuery<ProductSearchResult> query = em.createQuery(jpql, ProductSearchResult.class);
        for (String paramName : paramMap.keySet()) {
            query.setParameter(paramName, paramMap.get(paramName));
        }
        return query.getResultList();
    }
}
