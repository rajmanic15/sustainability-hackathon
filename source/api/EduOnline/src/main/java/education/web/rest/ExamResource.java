package education.web.rest;

import education.domain.Exam;
import education.repository.ExamRepository;
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
 * REST controller for managing {@link education.domain.Exam}.
 */
@Controller("/api")
public class ExamResource {

    private final Logger log = LoggerFactory.getLogger(ExamResource.class);

    private static final String ENTITY_NAME = "exam";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final ExamRepository examRepository;

    public ExamResource(ExamRepository examRepository) {
        this.examRepository = examRepository;
    }

    /**
     * {@code POST  /exams} : Create a new exam.
     *
     * @param exam the exam to create.
     * @return the {@link HttpResponse} with status {@code 201 (Created)} and with body the new exam, or with status {@code 400 (Bad Request)} if the exam has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @Post("/exams")
    @ExecuteOn(TaskExecutors.IO)
    public HttpResponse<Exam> createExam(@Body Exam exam) throws URISyntaxException {
        log.debug("REST request to save Exam : {}", exam);
        if (exam.getId() != null) {
            throw new BadRequestAlertException("A new exam cannot already have an ID", ENTITY_NAME, "idexists");
        }
        Exam result = examRepository.save(exam);
        URI location = new URI("/api/exams/" + result.getId());
        return HttpResponse.created(result).headers(headers -> {
            headers.location(location);
            HeaderUtil.createEntityCreationAlert(headers, applicationName, true, ENTITY_NAME, result.getId().toString());
        });
    }

    /**
     * {@code PUT  /exams} : Updates an existing exam.
     *
     * @param exam the exam to update.
     * @return the {@link HttpResponse} with status {@code 200 (OK)} and with body the updated exam,
     * or with status {@code 400 (Bad Request)} if the exam is not valid,
     * or with status {@code 500 (Internal Server Error)} if the exam couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @Put("/exams")
    @ExecuteOn(TaskExecutors.IO)
    public HttpResponse<Exam> updateExam(@Body Exam exam) throws URISyntaxException {
        log.debug("REST request to update Exam : {}", exam);
        if (exam.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        Exam result = examRepository.update(exam);
        return HttpResponse.ok(result).headers(headers ->
            HeaderUtil.createEntityUpdateAlert(headers, applicationName, true, ENTITY_NAME, exam.getId().toString()));
    }

    /**
     * {@code GET  /exams} : get all the exams.
     *
     * @return the {@link HttpResponse} with status {@code 200 (OK)} and the list of exams in body.
     */
    @Get("/exams")
    @ExecuteOn(TaskExecutors.IO)
    public Iterable<Exam> getAllExams(HttpRequest request) {
        log.debug("REST request to get all Exams");
        return examRepository.findAll();
    }

    /**
     * {@code GET  /exams/:id} : get the "id" exam.
     *
     * @param id the id of the exam to retrieve.
     * @return the {@link HttpResponse} with status {@code 200 (OK)} and with body the exam, or with status {@code 404 (Not Found)}.
     */
    @Get("/exams/{id}")
    @ExecuteOn(TaskExecutors.IO)
    public Optional<Exam> getExam(@PathVariable Long id) {
        log.debug("REST request to get Exam : {}", id);
        
        return examRepository.findById(id);
    }

    /**
     * {@code DELETE  /exams/:id} : delete the "id" exam.
     *
     * @param id the id of the exam to delete.
     * @return the {@link HttpResponse} with status {@code 204 (NO_CONTENT)}.
     */
    @Delete("/exams/{id}")
    @ExecuteOn(TaskExecutors.IO)
    public HttpResponse deleteExam(@PathVariable Long id) {
        log.debug("REST request to delete Exam : {}", id);
        examRepository.deleteById(id);
        return HttpResponse.noContent().headers(headers -> HeaderUtil.createEntityDeletionAlert(headers, applicationName, true, ENTITY_NAME, id.toString()));
    }
}
