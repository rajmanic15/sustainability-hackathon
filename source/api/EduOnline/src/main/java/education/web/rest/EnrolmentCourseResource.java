package education.web.rest;

import education.domain.EnrolmentCourse;
import education.repository.EnrolmentCourseRepository;
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
 * REST controller for managing {@link education.domain.EnrolmentCourse}.
 */
@Controller("/api")
public class EnrolmentCourseResource {

    private final Logger log = LoggerFactory.getLogger(EnrolmentCourseResource.class);

    private static final String ENTITY_NAME = "enrolmentCourse";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final EnrolmentCourseRepository enrolmentCourseRepository;

    public EnrolmentCourseResource(EnrolmentCourseRepository enrolmentCourseRepository) {
        this.enrolmentCourseRepository = enrolmentCourseRepository;
    }

    /**
     * {@code POST  /enrolment-courses} : Create a new enrolmentCourse.
     *
     * @param enrolmentCourse the enrolmentCourse to create.
     * @return the {@link HttpResponse} with status {@code 201 (Created)} and with body the new enrolmentCourse, or with status {@code 400 (Bad Request)} if the enrolmentCourse has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @Post("/enrolment-courses")
    @ExecuteOn(TaskExecutors.IO)
    public HttpResponse<EnrolmentCourse> createEnrolmentCourse(@Body EnrolmentCourse enrolmentCourse) throws URISyntaxException {
        log.debug("REST request to save EnrolmentCourse : {}", enrolmentCourse);
        if (enrolmentCourse.getId() != null) {
            throw new BadRequestAlertException("A new enrolmentCourse cannot already have an ID", ENTITY_NAME, "idexists");
        }
        EnrolmentCourse result = enrolmentCourseRepository.save(enrolmentCourse);
        URI location = new URI("/api/enrolment-courses/" + result.getId());
        return HttpResponse.created(result).headers(headers -> {
            headers.location(location);
            HeaderUtil.createEntityCreationAlert(headers, applicationName, true, ENTITY_NAME, result.getId().toString());
        });
    }

    /**
     * {@code PUT  /enrolment-courses} : Updates an existing enrolmentCourse.
     *
     * @param enrolmentCourse the enrolmentCourse to update.
     * @return the {@link HttpResponse} with status {@code 200 (OK)} and with body the updated enrolmentCourse,
     * or with status {@code 400 (Bad Request)} if the enrolmentCourse is not valid,
     * or with status {@code 500 (Internal Server Error)} if the enrolmentCourse couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @Put("/enrolment-courses")
    @ExecuteOn(TaskExecutors.IO)
    public HttpResponse<EnrolmentCourse> updateEnrolmentCourse(@Body EnrolmentCourse enrolmentCourse) throws URISyntaxException {
        log.debug("REST request to update EnrolmentCourse : {}", enrolmentCourse);
        if (enrolmentCourse.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        EnrolmentCourse result = enrolmentCourseRepository.update(enrolmentCourse);
        return HttpResponse.ok(result).headers(headers ->
            HeaderUtil.createEntityUpdateAlert(headers, applicationName, true, ENTITY_NAME, enrolmentCourse.getId().toString()));
    }

    /**
     * {@code GET  /enrolment-courses} : get all the enrolmentCourses.
     *
     * @return the {@link HttpResponse} with status {@code 200 (OK)} and the list of enrolmentCourses in body.
     */
    @Get("/enrolment-courses")
    @ExecuteOn(TaskExecutors.IO)
    public Iterable<EnrolmentCourse> getAllEnrolmentCourses(HttpRequest request) {
        log.debug("REST request to get all EnrolmentCourses");
        return enrolmentCourseRepository.findAll();
    }

    /**
     * {@code GET  /enrolment-courses/:id} : get the "id" enrolmentCourse.
     *
     * @param id the id of the enrolmentCourse to retrieve.
     * @return the {@link HttpResponse} with status {@code 200 (OK)} and with body the enrolmentCourse, or with status {@code 404 (Not Found)}.
     */
    @Get("/enrolment-courses/{id}")
    @ExecuteOn(TaskExecutors.IO)
    public Optional<EnrolmentCourse> getEnrolmentCourse(@PathVariable Long id) {
        log.debug("REST request to get EnrolmentCourse : {}", id);
        
        return enrolmentCourseRepository.findById(id);
    }

    /**
     * {@code DELETE  /enrolment-courses/:id} : delete the "id" enrolmentCourse.
     *
     * @param id the id of the enrolmentCourse to delete.
     * @return the {@link HttpResponse} with status {@code 204 (NO_CONTENT)}.
     */
    @Delete("/enrolment-courses/{id}")
    @ExecuteOn(TaskExecutors.IO)
    public HttpResponse deleteEnrolmentCourse(@PathVariable Long id) {
        log.debug("REST request to delete EnrolmentCourse : {}", id);
        enrolmentCourseRepository.deleteById(id);
        return HttpResponse.noContent().headers(headers -> HeaderUtil.createEntityDeletionAlert(headers, applicationName, true, ENTITY_NAME, id.toString()));
    }
}
