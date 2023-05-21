package education.web.rest;

import education.domain.Course;
import education.repository.CourseRepository;
import education.web.rest.errors.BadRequestAlertException;

import education.util.HeaderUtil;
import io.micronaut.context.annotation.Value;
import io.micronaut.core.annotation.Nullable;
import io.micronaut.http.HttpRequest;
import io.micronaut.http.HttpResponse;
import io.micronaut.http.annotation.*;
import io.micronaut.http.uri.UriBuilder;
import io.micronaut.scheduling.TaskExecutors;
import io.micronaut.scheduling.annotation.ExecuteOn;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;



import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Optional;

/**
 * REST controller for managing {@link education.domain.Course}.
 */
@Controller("/api")
public class CourseResource {

    private final Logger log = LoggerFactory.getLogger(CourseResource.class);

    private static final String ENTITY_NAME = "course";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final CourseRepository courseRepository;

    public CourseResource(CourseRepository courseRepository) {
        this.courseRepository = courseRepository;
    }

    /**
     * {@code POST  /courses} : Create a new course.
     *
     * @param course the course to create.
     * @return the {@link HttpResponse} with status {@code 201 (Created)} and with body the new course, or with status {@code 400 (Bad Request)} if the course has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @Post("/courses")
    @ExecuteOn(TaskExecutors.IO)
    public HttpResponse<Course> createCourse(@Body Course course) throws URISyntaxException {
        log.debug("REST request to save Course : {}", course);
        if (course.getId() != null) {
            throw new BadRequestAlertException("A new course cannot already have an ID", ENTITY_NAME, "idexists");
        }
        Course result = courseRepository.save(course);
        URI location = new URI("/api/courses/" + result.getId());
        return HttpResponse.created(result).headers(headers -> {
            headers.location(location);
            HeaderUtil.createEntityCreationAlert(headers, applicationName, true, ENTITY_NAME, result.getId().toString());
        });
    }

    /**
     * {@code PUT  /courses} : Updates an existing course.
     *
     * @param course the course to update.
     * @return the {@link HttpResponse} with status {@code 200 (OK)} and with body the updated course,
     * or with status {@code 400 (Bad Request)} if the course is not valid,
     * or with status {@code 500 (Internal Server Error)} if the course couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @Put("/courses")
    @ExecuteOn(TaskExecutors.IO)
    public HttpResponse<Course> updateCourse(@Body Course course) throws URISyntaxException {
        log.debug("REST request to update Course : {}", course);
        if (course.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        Course result = courseRepository.update(course);
        return HttpResponse.ok(result).headers(headers ->
            HeaderUtil.createEntityUpdateAlert(headers, applicationName, true, ENTITY_NAME, course.getId().toString()));
    }

    /**
     * {@code GET  /courses} : get all the courses.
     *
     * @return the {@link HttpResponse} with status {@code 200 (OK)} and the list of courses in body.
     */
    @Get("/courses")
    @ExecuteOn(TaskExecutors.IO)
    public Iterable<Course> getAllCourses(HttpRequest request) {
        log.debug("REST request to get all Courses");
        return courseRepository.findAll();
    }

    /**
     * {@code GET  /courses/:id} : get the "id" course.
     *
     * @param id the id of the course to retrieve.
     * @return the {@link HttpResponse} with status {@code 200 (OK)} and with body the course, or with status {@code 404 (Not Found)}.
     */
    @Get("/courses/{id}")
    @ExecuteOn(TaskExecutors.IO)
    public Optional<Course> getCourse(@PathVariable Long id) {
        log.debug("REST request to get Course : {}", id);
        
        return courseRepository.findById(id);
    }

    /**
     * {@code DELETE  /courses/:id} : delete the "id" course.
     *
     * @param id the id of the course to delete.
     * @return the {@link HttpResponse} with status {@code 204 (NO_CONTENT)}.
     */
    @Delete("/courses/{id}")
    @ExecuteOn(TaskExecutors.IO)
    public HttpResponse deleteCourse(@PathVariable Long id) {
        log.debug("REST request to delete Course : {}", id);
        courseRepository.deleteById(id);
        return HttpResponse.noContent().headers(headers -> HeaderUtil.createEntityDeletionAlert(headers, applicationName, true, ENTITY_NAME, id.toString()));
    }
}
