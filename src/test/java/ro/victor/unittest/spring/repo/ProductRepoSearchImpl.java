package ro.victor.unittest.spring.repo;

import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Repository;
import ro.victor.unittest.spring.infra.ExternalServiceClient;
import ro.victor.unittest.spring.domain.Product;
import ro.victor.unittest.spring.facade.ProductSearchCriteria;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Repository
@RequiredArgsConstructor
public class ProductRepoSearchImpl implements ProductRepoSearch {
    private final EntityManager em;
//    private final ExternalServiceClient externalServiceClient;

    @Override
    public List<Product> search(ProductSearchCriteria criteria) {
//        String jpql = "SELECT new ro.victor.unittest.spring.search.ProductSearchResult(p.id, p.name) FROM Product p WHERE 1=1 ";
        String jpql = "SELECT p FROM Product p WHERE 1=1 ";

//        System.out.println(externalServiceClient.callService());

        Map<String, Object> paramMap = new HashMap<>();
        if (StringUtils.isNotBlank(criteria.name)) {
            jpql += "   AND UPPER(p.name) LIKE UPPER(:name)   ";
            paramMap.put("name", "%" + criteria.name + "%");
        }

        if (criteria.category != null) {
            jpql += "   AND p.category = :category ";
            paramMap.put("category", criteria.category);
        }

        TypedQuery<Product> query = em.createQuery(jpql, Product.class);

        for (String paramName : paramMap.keySet()) {
            query.setParameter(paramName, paramMap.get(paramName));
        }

        return query.getResultList();
    }
}
