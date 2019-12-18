package ro.victor.unittest.db.search;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class JobRepositoryImpl implements JobRepositoryCustom {

    @Autowired
    private EntityManager em;

    @Override
    public List<Job> search(JobSearchCriteria jobSearchCriteria) {
        String jpql = "SELECT j FROM Job j WHERE 1=1 ";
        Map<String, Object> paramMap = new HashMap<>();
        if (StringUtils.isNotBlank(jobSearchCriteria.jobName)) {
            jpql += "  AND UPPER(j.name) LIKE '%' || UPPER(:name) || '%' ";
            paramMap.put("name", jobSearchCriteria.jobName);
        }
        if (StringUtils.isNotBlank(jobSearchCriteria.skill)) {
            jpql += "  AND EXISTS (SELECT 1 FROM Skill s WHERE " +
                    " s.job.id = j.id AND UPPER(s.name) LIKE '%' || UPPER(:skill) || '%' )";
            paramMap.put("skill", jobSearchCriteria.skill);
        }
        TypedQuery<Job> query = em.createQuery(jpql, Job.class);
        for (String key : paramMap.keySet()) {
            query.setParameter(key, paramMap.get(key));
        }
        return query.getResultList();
    }
}
