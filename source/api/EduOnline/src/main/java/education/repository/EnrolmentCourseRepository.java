package education.repository;

import education.domain.EnrolmentCourse;


import io.micronaut.data.annotation.Query;
import io.micronaut.data.annotation.Repository;
import io.micronaut.data.jpa.repository.JpaRepository;



/**
 * Micronaut Data  repository for the EnrolmentCourse entity.
 */
@SuppressWarnings("unused")
@Repository
public interface EnrolmentCourseRepository extends JpaRepository<EnrolmentCourse, Long> {

}
