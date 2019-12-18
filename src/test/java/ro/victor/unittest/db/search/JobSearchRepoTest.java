package ro.victor.unittest.db.search;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.junit.Assert.assertEquals;

@SpringBootTest
@RunWith(SpringRunner.class)
@Transactional
//@Rollback(false) - pt nopti triste cand faci debug si vrei sa vezi ce insera el de fapt in tranzactia de test
public class JobSearchRepoTest {
    @Autowired
    private JobRepository repo;
    private JobSearchCriteria criteria = new JobSearchCriteria();
    @Test
    public void byName() {
        Job job = new Job().setName("Jr Architect");
        repo.save(job);

        criteria.jobName = "ARCH";
        List<Job> results = repo.search(criteria);
        assertEquals(1, results.size());
        assertEquals(job.getId(), results.get(0).getId());

        criteria.jobName = "COBOL";
        assertEquals(0, repo.search(criteria).size());
    }
    @Test
    public void bySkill() {
        Job job = new Job().addSkill(new Skill("Pragmatic"));
        repo.save(job);

        criteria.skill = "ragmaT";
        List<Job> results = repo.search(criteria);
        assertEquals(1, results.size());
        assertEquals(job.getId(), results.get(0).getId());
    }
    @Test
    public void bySkill_noMatch() {
        Job job = new Job().addSkill(new Skill("Pragmatic"));
        repo.save(job);

        criteria.skill = "Bautor";
        assertEquals(0, repo.search(criteria).size());
    }
}
