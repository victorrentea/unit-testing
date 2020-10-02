package victor.testing.bank.repo;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import victor.testing.bank.vo.ClientSearchCriteria;
import victor.testing.bank.vo.ClientSearchResult;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Repository
public class ClientJpaRepository {
    @Autowired
    private EntityManager em;

    public List<ClientSearchResult> search(ClientSearchCriteria criteria) {
        String jpql = "SELECT new ro.victor.unittest.bank.vo.ClientSearchResult" +
                "(c.id, c.name) FROM Client c WHERE 1=1 ";
        Map<String, Object> params = new HashMap<>();

        if (StringUtils.isNotBlank(criteria.getName())) {
            jpql += " AND UPPER(c.name) LIKE UPPER(:clientName) ";
            params.put("clientName", "%" + criteria.getName() + "%");
        }

        if (!criteria.getNationalityIsoList().isEmpty()) {
            jpql += " AND c.nationalityIso IN :nationalityIsoList  ";
            params.put("nationalityIsoList", criteria.getNationalityIsoList());

        }


        TypedQuery<ClientSearchResult> q = em.createQuery(jpql, ClientSearchResult.class);
        for (String key : params.keySet()) {
            q.setParameter(key, params.get(key));
        }
        return q.getResultList();
    }

}
