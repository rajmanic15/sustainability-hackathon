package education.repository;

import education.domain.EnrolmentExam;


import io.micronaut.data.annotation.Query;
import io.micronaut.data.annotation.Repository;
import io.micronaut.data.jpa.repository.JpaRepository;



/**
 * Micronaut Data  repository for the EnrolmentExam entity.
 */
@SuppressWarnings("unused")
@Repository
public interface EnrolmentExamRepository extends JpaRepository<EnrolmentExam, Long> {

}
