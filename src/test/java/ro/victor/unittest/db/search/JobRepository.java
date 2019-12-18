package ro.victor.unittest.db.search;

import org.springframework.data.jpa.repository.JpaRepository;

public interface JobRepository extends JpaRepository<Job, Long>, JobRepositoryCustom {

}
