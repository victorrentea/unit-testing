package ro.victor.unittest.db.search;

import java.util.List;

public interface JobRepositoryCustom {
    List<Job> search(JobSearchCriteria jobSearchCriteria);
}
