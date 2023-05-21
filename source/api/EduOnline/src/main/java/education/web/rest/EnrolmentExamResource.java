package education.web.rest;

import education.domain.EnrolmentExam;
import education.repository.EnrolmentExamRepository;
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
 * REST controller for managing {@link education.domain.EnrolmentExam}.
 */
@Controller("/api")
public class EnrolmentExamResource {

    private final Logger log = LoggerFactory.getLogger(EnrolmentExamResource.class);

    private static final String ENTITY_NAME = "enrolmentExam";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final EnrolmentExamRepository enrolmentExamRepository;

    public EnrolmentExamResource(EnrolmentExamRepository enrolmentExamRepository) {
        this.enrolmentExamRepository = enrolmentExamRepository;
    }

    /**
     * {@code POST  /enrolment-exams} : Create a new enrolmentExam.
     *
     * @param enrolmentExam the enrolmentExam to create.
     * @return the {@link HttpResponse} with status {@code 201 (Created)} and with body the new enrolmentExam, or with status {@code 400 (Bad Request)} if the enrolmentExam has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @Post("/enrolment-exams")
    @ExecuteOn(TaskExecutors.IO)
    public HttpResponse<EnrolmentExam> createEnrolmentExam(@Body EnrolmentExam enrolmentExam) throws URISyntaxException {
        log.debug("REST request to save EnrolmentExam : {}", enrolmentExam);
        if (enrolmentExam.getId() != null) {
            throw new BadRequestAlertException("A new enrolmentExam cannot already have an ID", ENTITY_NAME, "idexists");
        }
        EnrolmentExam result = enrolmentExamRepository.save(enrolmentExam);
        URI location = new URI("/api/enrolment-exams/" + result.getId());
        return HttpResponse.created(result).headers(headers -> {
            headers.location(location);
            HeaderUtil.createEntityCreationAlert(headers, applicationName, true, ENTITY_NAME, result.getId().toString());
        });
    }

    /**
     * {@code PUT  /enrolment-exams} : Updates an existing enrolmentExam.
     *
     * @param enrolmentExam the enrolmentExam to update.
     * @return the {@link HttpResponse} with status {@code 200 (OK)} and with body the updated enrolmentExam,
     * or with status {@code 400 (Bad Request)} if the enrolmentExam is not valid,
     * or with status {@code 500 (Internal Server Error)} if the enrolmentExam couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @Put("/enrolment-exams")
    @ExecuteOn(TaskExecutors.IO)
    public HttpResponse<EnrolmentExam> updateEnrolmentExam(@Body EnrolmentExam enrolmentExam) throws URISyntaxException {
        log.debug("REST request to update EnrolmentExam : {}", enrolmentExam);
        if (enrolmentExam.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        EnrolmentExam result = enrolmentExamRepository.update(enrolmentExam);
        return HttpResponse.ok(result).headers(headers ->
            HeaderUtil.createEntityUpdateAlert(headers, applicationName, true, ENTITY_NAME, enrolmentExam.getId().toString()));
    }

    /**
     * {@code GET  /enrolment-exams} : get all the enrolmentExams.
     *
     * @return the {@link HttpResponse} with status {@code 200 (OK)} and the list of enrolmentExams in body.
     */
    @Get("/enrolment-exams")
    @ExecuteOn(TaskExecutors.IO)
    public Iterable<EnrolmentExam> getAllEnrolmentExams(HttpRequest request) {
        log.debug("REST request to get all EnrolmentExams");
        return enrolmentExamRepository.findAll();
    }

    /**
     * {@code GET  /enrolment-exams/:id} : get the "id" enrolmentExam.
     *
     * @param id the id of the enrolmentExam to retrieve.
     * @return the {@link HttpResponse} with status {@code 200 (OK)} and with body the enrolmentExam, or with status {@code 404 (Not Found)}.
     */
    @Get("/enrolment-exams/{id}")
    @ExecuteOn(TaskExecutors.IO)
    public Optional<EnrolmentExam> getEnrolmentExam(@PathVariable Long id) {
        log.debug("REST request to get EnrolmentExam : {}", id);
        
        return enrolmentExamRepository.findById(id);
    }

    /**
     * {@code DELETE  /enrolment-exams/:id} : delete the "id" enrolmentExam.
     *
     * @param id the id of the enrolmentExam to delete.
     * @return the {@link HttpResponse} with status {@code 204 (NO_CONTENT)}.
     */
    @Delete("/enrolment-exams/{id}")
    @ExecuteOn(TaskExecutors.IO)
    public HttpResponse deleteEnrolmentExam(@PathVariable Long id) {
        log.debug("REST request to delete EnrolmentExam : {}", id);
        enrolmentExamRepository.deleteById(id);
        return HttpResponse.noContent().headers(headers -> HeaderUtil.createEntityDeletionAlert(headers, applicationName, true, ENTITY_NAME, id.toString()));
    }
}
