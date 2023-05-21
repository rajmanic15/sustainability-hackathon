package education.web.rest;

import education.domain.Student;
import education.repository.StudentRepository;
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
 * REST controller for managing {@link education.domain.Student}.
 */
@Controller("/api")
public class StudentResource {

    private final Logger log = LoggerFactory.getLogger(StudentResource.class);

    private static final String ENTITY_NAME = "student";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final StudentRepository studentRepository;

    public StudentResource(StudentRepository studentRepository) {
        this.studentRepository = studentRepository;
    }

    /**
     * {@code POST  /students} : Create a new student.
     *
     * @param student the student to create.
     * @return the {@link HttpResponse} with status {@code 201 (Created)} and with body the new student, or with status {@code 400 (Bad Request)} if the student has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @Post("/students")
    @ExecuteOn(TaskExecutors.IO)
    public HttpResponse<Student> createStudent(@Body Student student) throws URISyntaxException {
        log.debug("REST request to save Student : {}", student);
        if (student.getId() != null) {
            throw new BadRequestAlertException("A new student cannot already have an ID", ENTITY_NAME, "idexists");
        }
        Student result = studentRepository.save(student);
        URI location = new URI("/api/students/" + result.getId());
        return HttpResponse.created(result).headers(headers -> {
            headers.location(location);
            HeaderUtil.createEntityCreationAlert(headers, applicationName, true, ENTITY_NAME, result.getId().toString());
        });
    }

    /**
     * {@code PUT  /students} : Updates an existing student.
     *
     * @param student the student to update.
     * @return the {@link HttpResponse} with status {@code 200 (OK)} and with body the updated student,
     * or with status {@code 400 (Bad Request)} if the student is not valid,
     * or with status {@code 500 (Internal Server Error)} if the student couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @Put("/students")
    @ExecuteOn(TaskExecutors.IO)
    public HttpResponse<Student> updateStudent(@Body Student student) throws URISyntaxException {
        log.debug("REST request to update Student : {}", student);
        if (student.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        Student result = studentRepository.update(student);
        return HttpResponse.ok(result).headers(headers ->
            HeaderUtil.createEntityUpdateAlert(headers, applicationName, true, ENTITY_NAME, student.getId().toString()));
    }

    /**
     * {@code GET  /students} : get all the students.
     *
     * @return the {@link HttpResponse} with status {@code 200 (OK)} and the list of students in body.
     */
    @Get("/students")
    @ExecuteOn(TaskExecutors.IO)
    public Iterable<Student> getAllStudents(HttpRequest request) {
        log.debug("REST request to get all Students");
        return studentRepository.findAll();
    }

    /**
     * {@code GET  /students/:id} : get the "id" student.
     *
     * @param id the id of the student to retrieve.
     * @return the {@link HttpResponse} with status {@code 200 (OK)} and with body the student, or with status {@code 404 (Not Found)}.
     */
    @Get("/students/{id}")
    @ExecuteOn(TaskExecutors.IO)
    public Optional<Student> getStudent(@PathVariable Long id) {
        log.debug("REST request to get Student : {}", id);
        
        return studentRepository.findById(id);
    }

    /**
     * {@code DELETE  /students/:id} : delete the "id" student.
     *
     * @param id the id of the student to delete.
     * @return the {@link HttpResponse} with status {@code 204 (NO_CONTENT)}.
     */
    @Delete("/students/{id}")
    @ExecuteOn(TaskExecutors.IO)
    public HttpResponse deleteStudent(@PathVariable Long id) {
        log.debug("REST request to delete Student : {}", id);
        studentRepository.deleteById(id);
        return HttpResponse.noContent().headers(headers -> HeaderUtil.createEntityDeletionAlert(headers, applicationName, true, ENTITY_NAME, id.toString()));
    }
}
