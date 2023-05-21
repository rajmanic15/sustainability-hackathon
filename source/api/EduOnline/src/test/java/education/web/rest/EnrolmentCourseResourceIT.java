package education.web.rest;


import education.domain.EnrolmentCourse;
import education.repository.EnrolmentCourseRepository;

import io.micronaut.context.annotation.Property;
import io.micronaut.core.type.Argument;
import io.micronaut.http.HttpRequest;
import io.micronaut.http.HttpResponse;
import io.micronaut.http.HttpStatus;
import io.micronaut.http.client.RxHttpClient;
import io.micronaut.http.client.annotation.Client;
import io.micronaut.http.client.exceptions.HttpClientResponseException;
import io.micronaut.test.extensions.junit5.annotation.MicronautTest;
import io.micronaut.transaction.SynchronousTransactionManager;
import io.micronaut.transaction.TransactionOperations;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;

import javax.inject.Inject;
import javax.persistence.EntityManager;

import java.time.LocalDate;
import java.time.ZoneId;
import java.sql.Connection;
import java.util.List;


import static org.assertj.core.api.Assertions.assertThat;


/**
 * Integration tests for the {@Link EnrolmentCourseResource} REST controller.
 */
@MicronautTest(transactional = false)
@Property(name = "micronaut.security.enabled", value = "false")
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class EnrolmentCourseResourceIT {

    private static final String DEFAULT_STATUS = "AAAAAAAAAA";
    private static final String UPDATED_STATUS = "BBBBBBBBBB";

    private static final LocalDate DEFAULT_ENROLMENT_DATE = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_ENROLMENT_DATE = LocalDate.now(ZoneId.systemDefault());

    @Inject
    private EnrolmentCourseRepository enrolmentCourseRepository;

    @Inject
    private EntityManager em;

    @Inject
    SynchronousTransactionManager<Connection> transactionManager;

    @Inject @Client("/")
    RxHttpClient client;

    private EnrolmentCourse enrolmentCourse;

    @BeforeEach
    public void initTest() {
        enrolmentCourse = createEntity(transactionManager, em);
    }

    @AfterEach
    public void cleanUpTest() {
        deleteAll(transactionManager, em);
    }

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static EnrolmentCourse createEntity(TransactionOperations<Connection> transactionManager, EntityManager em) {
        EnrolmentCourse enrolmentCourse = new EnrolmentCourse()
            .status(DEFAULT_STATUS)
            .enrolmentDate(DEFAULT_ENROLMENT_DATE);
        return enrolmentCourse;
    }

    /**
     * Delete all enrolmentCourse entities.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static void deleteAll(TransactionOperations<Connection> transactionManager, EntityManager em) {
        TestUtil.removeAll(transactionManager, em, EnrolmentCourse.class);
    }


    @Test
    public void createEnrolmentCourse() throws Exception {
        int databaseSizeBeforeCreate = enrolmentCourseRepository.findAll().size();


        // Create the EnrolmentCourse
        HttpResponse<EnrolmentCourse> response = client.exchange(HttpRequest.POST("/api/enrolment-courses", enrolmentCourse), EnrolmentCourse.class).blockingFirst();

        assertThat(response.status().getCode()).isEqualTo(HttpStatus.CREATED.getCode());

        // Validate the EnrolmentCourse in the database
        List<EnrolmentCourse> enrolmentCourseList = enrolmentCourseRepository.findAll();
        assertThat(enrolmentCourseList).hasSize(databaseSizeBeforeCreate + 1);
        EnrolmentCourse testEnrolmentCourse = enrolmentCourseList.get(enrolmentCourseList.size() - 1);

        assertThat(testEnrolmentCourse.getStatus()).isEqualTo(DEFAULT_STATUS);
        assertThat(testEnrolmentCourse.getEnrolmentDate()).isEqualTo(DEFAULT_ENROLMENT_DATE);
    }

    @Test
    public void createEnrolmentCourseWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = enrolmentCourseRepository.findAll().size();

        // Create the EnrolmentCourse with an existing ID
        enrolmentCourse.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        @SuppressWarnings("unchecked")
        HttpResponse<EnrolmentCourse> response = client.exchange(HttpRequest.POST("/api/enrolment-courses", enrolmentCourse), EnrolmentCourse.class)
            .onErrorReturn(t -> (HttpResponse<EnrolmentCourse>) ((HttpClientResponseException) t).getResponse()).blockingFirst();

        assertThat(response.status().getCode()).isEqualTo(HttpStatus.BAD_REQUEST.getCode());

        // Validate the EnrolmentCourse in the database
        List<EnrolmentCourse> enrolmentCourseList = enrolmentCourseRepository.findAll();
        assertThat(enrolmentCourseList).hasSize(databaseSizeBeforeCreate);
    }


    @Test
    public void checkEnrolmentDateIsRequired() throws Exception {
        int databaseSizeBeforeTest = enrolmentCourseRepository.findAll().size();
        // set the field null
        enrolmentCourse.setEnrolmentDate(null);

        // Create the EnrolmentCourse, which fails.

        @SuppressWarnings("unchecked")
        HttpResponse<EnrolmentCourse> response = client.exchange(HttpRequest.POST("/api/enrolment-courses", enrolmentCourse), EnrolmentCourse.class)
            .onErrorReturn(t -> (HttpResponse<EnrolmentCourse>) ((HttpClientResponseException) t).getResponse()).blockingFirst();

        assertThat(response.status().getCode()).isEqualTo(HttpStatus.BAD_REQUEST.getCode());

        List<EnrolmentCourse> enrolmentCourseList = enrolmentCourseRepository.findAll();
        assertThat(enrolmentCourseList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    public void getAllEnrolmentCourses() throws Exception {
        // Initialize the database
        enrolmentCourseRepository.saveAndFlush(enrolmentCourse);

        // Get the enrolmentCourseList w/ all the enrolmentCourses
        List<EnrolmentCourse> enrolmentCourses = client.retrieve(HttpRequest.GET("/api/enrolment-courses?eagerload=true"), Argument.listOf(EnrolmentCourse.class)).blockingFirst();
        EnrolmentCourse testEnrolmentCourse = enrolmentCourses.get(0);


        assertThat(testEnrolmentCourse.getStatus()).isEqualTo(DEFAULT_STATUS);
        assertThat(testEnrolmentCourse.getEnrolmentDate()).isEqualTo(DEFAULT_ENROLMENT_DATE);
    }

    @Test
    public void getEnrolmentCourse() throws Exception {
        // Initialize the database
        enrolmentCourseRepository.saveAndFlush(enrolmentCourse);

        // Get the enrolmentCourse
        EnrolmentCourse testEnrolmentCourse = client.retrieve(HttpRequest.GET("/api/enrolment-courses/" + enrolmentCourse.getId()), EnrolmentCourse.class).blockingFirst();


        assertThat(testEnrolmentCourse.getStatus()).isEqualTo(DEFAULT_STATUS);
        assertThat(testEnrolmentCourse.getEnrolmentDate()).isEqualTo(DEFAULT_ENROLMENT_DATE);
    }

    @Test
    public void getNonExistingEnrolmentCourse() throws Exception {
        // Get the enrolmentCourse
        @SuppressWarnings("unchecked")
        HttpResponse<EnrolmentCourse> response = client.exchange(HttpRequest.GET("/api/enrolment-courses/"+ Long.MAX_VALUE), EnrolmentCourse.class)
            .onErrorReturn(t -> (HttpResponse<EnrolmentCourse>) ((HttpClientResponseException) t).getResponse()).blockingFirst();

        assertThat(response.status().getCode()).isEqualTo(HttpStatus.NOT_FOUND.getCode());
    }

    @Test
    public void updateEnrolmentCourse() throws Exception {
        // Initialize the database
        enrolmentCourseRepository.saveAndFlush(enrolmentCourse);

        int databaseSizeBeforeUpdate = enrolmentCourseRepository.findAll().size();

        // Update the enrolmentCourse
        EnrolmentCourse updatedEnrolmentCourse = enrolmentCourseRepository.findById(enrolmentCourse.getId()).get();

        updatedEnrolmentCourse
            .status(UPDATED_STATUS)
            .enrolmentDate(UPDATED_ENROLMENT_DATE);

        @SuppressWarnings("unchecked")
        HttpResponse<EnrolmentCourse> response = client.exchange(HttpRequest.PUT("/api/enrolment-courses", updatedEnrolmentCourse), EnrolmentCourse.class)
            .onErrorReturn(t -> (HttpResponse<EnrolmentCourse>) ((HttpClientResponseException) t).getResponse()).blockingFirst();

        assertThat(response.status().getCode()).isEqualTo(HttpStatus.OK.getCode());

        // Validate the EnrolmentCourse in the database
        List<EnrolmentCourse> enrolmentCourseList = enrolmentCourseRepository.findAll();
        assertThat(enrolmentCourseList).hasSize(databaseSizeBeforeUpdate);
        EnrolmentCourse testEnrolmentCourse = enrolmentCourseList.get(enrolmentCourseList.size() - 1);

        assertThat(testEnrolmentCourse.getStatus()).isEqualTo(UPDATED_STATUS);
        assertThat(testEnrolmentCourse.getEnrolmentDate()).isEqualTo(UPDATED_ENROLMENT_DATE);
    }

    @Test
    public void updateNonExistingEnrolmentCourse() throws Exception {
        int databaseSizeBeforeUpdate = enrolmentCourseRepository.findAll().size();

        // Create the EnrolmentCourse

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        @SuppressWarnings("unchecked")
        HttpResponse<EnrolmentCourse> response = client.exchange(HttpRequest.PUT("/api/enrolment-courses", enrolmentCourse), EnrolmentCourse.class)
            .onErrorReturn(t -> (HttpResponse<EnrolmentCourse>) ((HttpClientResponseException) t).getResponse()).blockingFirst();

        assertThat(response.status().getCode()).isEqualTo(HttpStatus.BAD_REQUEST.getCode());

        // Validate the EnrolmentCourse in the database
        List<EnrolmentCourse> enrolmentCourseList = enrolmentCourseRepository.findAll();
        assertThat(enrolmentCourseList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    public void deleteEnrolmentCourse() throws Exception {
        // Initialize the database with one entity
        enrolmentCourseRepository.saveAndFlush(enrolmentCourse);

        int databaseSizeBeforeDelete = enrolmentCourseRepository.findAll().size();

        // Delete the enrolmentCourse
        @SuppressWarnings("unchecked")
        HttpResponse<EnrolmentCourse> response = client.exchange(HttpRequest.DELETE("/api/enrolment-courses/"+ enrolmentCourse.getId()), EnrolmentCourse.class)
            .onErrorReturn(t -> (HttpResponse<EnrolmentCourse>) ((HttpClientResponseException) t).getResponse()).blockingFirst();

        assertThat(response.status().getCode()).isEqualTo(HttpStatus.NO_CONTENT.getCode());

            // Validate the database is now empty
        List<EnrolmentCourse> enrolmentCourseList = enrolmentCourseRepository.findAll();
        assertThat(enrolmentCourseList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(EnrolmentCourse.class);
        EnrolmentCourse enrolmentCourse1 = new EnrolmentCourse();
        enrolmentCourse1.setId(1L);
        EnrolmentCourse enrolmentCourse2 = new EnrolmentCourse();
        enrolmentCourse2.setId(enrolmentCourse1.getId());
        assertThat(enrolmentCourse1).isEqualTo(enrolmentCourse2);
        enrolmentCourse2.setId(2L);
        assertThat(enrolmentCourse1).isNotEqualTo(enrolmentCourse2);
        enrolmentCourse1.setId(null);
        assertThat(enrolmentCourse1).isNotEqualTo(enrolmentCourse2);
    }
}
