package education.web.rest;


import education.domain.EnrolmentExam;
import education.repository.EnrolmentExamRepository;

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
 * Integration tests for the {@Link EnrolmentExamResource} REST controller.
 */
@MicronautTest(transactional = false)
@Property(name = "micronaut.security.enabled", value = "false")
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class EnrolmentExamResourceIT {

    private static final String DEFAULT_STATUS = "AAAAAAAAAA";
    private static final String UPDATED_STATUS = "BBBBBBBBBB";

    private static final LocalDate DEFAULT_ENROLMENT_DATE = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_ENROLMENT_DATE = LocalDate.now(ZoneId.systemDefault());

    @Inject
    private EnrolmentExamRepository enrolmentExamRepository;

    @Inject
    private EntityManager em;

    @Inject
    SynchronousTransactionManager<Connection> transactionManager;

    @Inject @Client("/")
    RxHttpClient client;

    private EnrolmentExam enrolmentExam;

    @BeforeEach
    public void initTest() {
        enrolmentExam = createEntity(transactionManager, em);
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
    public static EnrolmentExam createEntity(TransactionOperations<Connection> transactionManager, EntityManager em) {
        EnrolmentExam enrolmentExam = new EnrolmentExam()
            .status(DEFAULT_STATUS)
            .enrolmentDate(DEFAULT_ENROLMENT_DATE);
        return enrolmentExam;
    }

    /**
     * Delete all enrolmentExam entities.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static void deleteAll(TransactionOperations<Connection> transactionManager, EntityManager em) {
        TestUtil.removeAll(transactionManager, em, EnrolmentExam.class);
    }


    @Test
    public void createEnrolmentExam() throws Exception {
        int databaseSizeBeforeCreate = enrolmentExamRepository.findAll().size();


        // Create the EnrolmentExam
        HttpResponse<EnrolmentExam> response = client.exchange(HttpRequest.POST("/api/enrolment-exams", enrolmentExam), EnrolmentExam.class).blockingFirst();

        assertThat(response.status().getCode()).isEqualTo(HttpStatus.CREATED.getCode());

        // Validate the EnrolmentExam in the database
        List<EnrolmentExam> enrolmentExamList = enrolmentExamRepository.findAll();
        assertThat(enrolmentExamList).hasSize(databaseSizeBeforeCreate + 1);
        EnrolmentExam testEnrolmentExam = enrolmentExamList.get(enrolmentExamList.size() - 1);

        assertThat(testEnrolmentExam.getStatus()).isEqualTo(DEFAULT_STATUS);
        assertThat(testEnrolmentExam.getEnrolmentDate()).isEqualTo(DEFAULT_ENROLMENT_DATE);
    }

    @Test
    public void createEnrolmentExamWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = enrolmentExamRepository.findAll().size();

        // Create the EnrolmentExam with an existing ID
        enrolmentExam.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        @SuppressWarnings("unchecked")
        HttpResponse<EnrolmentExam> response = client.exchange(HttpRequest.POST("/api/enrolment-exams", enrolmentExam), EnrolmentExam.class)
            .onErrorReturn(t -> (HttpResponse<EnrolmentExam>) ((HttpClientResponseException) t).getResponse()).blockingFirst();

        assertThat(response.status().getCode()).isEqualTo(HttpStatus.BAD_REQUEST.getCode());

        // Validate the EnrolmentExam in the database
        List<EnrolmentExam> enrolmentExamList = enrolmentExamRepository.findAll();
        assertThat(enrolmentExamList).hasSize(databaseSizeBeforeCreate);
    }


    @Test
    public void checkEnrolmentDateIsRequired() throws Exception {
        int databaseSizeBeforeTest = enrolmentExamRepository.findAll().size();
        // set the field null
        enrolmentExam.setEnrolmentDate(null);

        // Create the EnrolmentExam, which fails.

        @SuppressWarnings("unchecked")
        HttpResponse<EnrolmentExam> response = client.exchange(HttpRequest.POST("/api/enrolment-exams", enrolmentExam), EnrolmentExam.class)
            .onErrorReturn(t -> (HttpResponse<EnrolmentExam>) ((HttpClientResponseException) t).getResponse()).blockingFirst();

        assertThat(response.status().getCode()).isEqualTo(HttpStatus.BAD_REQUEST.getCode());

        List<EnrolmentExam> enrolmentExamList = enrolmentExamRepository.findAll();
        assertThat(enrolmentExamList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    public void getAllEnrolmentExams() throws Exception {
        // Initialize the database
        enrolmentExamRepository.saveAndFlush(enrolmentExam);

        // Get the enrolmentExamList w/ all the enrolmentExams
        List<EnrolmentExam> enrolmentExams = client.retrieve(HttpRequest.GET("/api/enrolment-exams?eagerload=true"), Argument.listOf(EnrolmentExam.class)).blockingFirst();
        EnrolmentExam testEnrolmentExam = enrolmentExams.get(0);


        assertThat(testEnrolmentExam.getStatus()).isEqualTo(DEFAULT_STATUS);
        assertThat(testEnrolmentExam.getEnrolmentDate()).isEqualTo(DEFAULT_ENROLMENT_DATE);
    }

    @Test
    public void getEnrolmentExam() throws Exception {
        // Initialize the database
        enrolmentExamRepository.saveAndFlush(enrolmentExam);

        // Get the enrolmentExam
        EnrolmentExam testEnrolmentExam = client.retrieve(HttpRequest.GET("/api/enrolment-exams/" + enrolmentExam.getId()), EnrolmentExam.class).blockingFirst();


        assertThat(testEnrolmentExam.getStatus()).isEqualTo(DEFAULT_STATUS);
        assertThat(testEnrolmentExam.getEnrolmentDate()).isEqualTo(DEFAULT_ENROLMENT_DATE);
    }

    @Test
    public void getNonExistingEnrolmentExam() throws Exception {
        // Get the enrolmentExam
        @SuppressWarnings("unchecked")
        HttpResponse<EnrolmentExam> response = client.exchange(HttpRequest.GET("/api/enrolment-exams/"+ Long.MAX_VALUE), EnrolmentExam.class)
            .onErrorReturn(t -> (HttpResponse<EnrolmentExam>) ((HttpClientResponseException) t).getResponse()).blockingFirst();

        assertThat(response.status().getCode()).isEqualTo(HttpStatus.NOT_FOUND.getCode());
    }

    @Test
    public void updateEnrolmentExam() throws Exception {
        // Initialize the database
        enrolmentExamRepository.saveAndFlush(enrolmentExam);

        int databaseSizeBeforeUpdate = enrolmentExamRepository.findAll().size();

        // Update the enrolmentExam
        EnrolmentExam updatedEnrolmentExam = enrolmentExamRepository.findById(enrolmentExam.getId()).get();

        updatedEnrolmentExam
            .status(UPDATED_STATUS)
            .enrolmentDate(UPDATED_ENROLMENT_DATE);

        @SuppressWarnings("unchecked")
        HttpResponse<EnrolmentExam> response = client.exchange(HttpRequest.PUT("/api/enrolment-exams", updatedEnrolmentExam), EnrolmentExam.class)
            .onErrorReturn(t -> (HttpResponse<EnrolmentExam>) ((HttpClientResponseException) t).getResponse()).blockingFirst();

        assertThat(response.status().getCode()).isEqualTo(HttpStatus.OK.getCode());

        // Validate the EnrolmentExam in the database
        List<EnrolmentExam> enrolmentExamList = enrolmentExamRepository.findAll();
        assertThat(enrolmentExamList).hasSize(databaseSizeBeforeUpdate);
        EnrolmentExam testEnrolmentExam = enrolmentExamList.get(enrolmentExamList.size() - 1);

        assertThat(testEnrolmentExam.getStatus()).isEqualTo(UPDATED_STATUS);
        assertThat(testEnrolmentExam.getEnrolmentDate()).isEqualTo(UPDATED_ENROLMENT_DATE);
    }

    @Test
    public void updateNonExistingEnrolmentExam() throws Exception {
        int databaseSizeBeforeUpdate = enrolmentExamRepository.findAll().size();

        // Create the EnrolmentExam

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        @SuppressWarnings("unchecked")
        HttpResponse<EnrolmentExam> response = client.exchange(HttpRequest.PUT("/api/enrolment-exams", enrolmentExam), EnrolmentExam.class)
            .onErrorReturn(t -> (HttpResponse<EnrolmentExam>) ((HttpClientResponseException) t).getResponse()).blockingFirst();

        assertThat(response.status().getCode()).isEqualTo(HttpStatus.BAD_REQUEST.getCode());

        // Validate the EnrolmentExam in the database
        List<EnrolmentExam> enrolmentExamList = enrolmentExamRepository.findAll();
        assertThat(enrolmentExamList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    public void deleteEnrolmentExam() throws Exception {
        // Initialize the database with one entity
        enrolmentExamRepository.saveAndFlush(enrolmentExam);

        int databaseSizeBeforeDelete = enrolmentExamRepository.findAll().size();

        // Delete the enrolmentExam
        @SuppressWarnings("unchecked")
        HttpResponse<EnrolmentExam> response = client.exchange(HttpRequest.DELETE("/api/enrolment-exams/"+ enrolmentExam.getId()), EnrolmentExam.class)
            .onErrorReturn(t -> (HttpResponse<EnrolmentExam>) ((HttpClientResponseException) t).getResponse()).blockingFirst();

        assertThat(response.status().getCode()).isEqualTo(HttpStatus.NO_CONTENT.getCode());

            // Validate the database is now empty
        List<EnrolmentExam> enrolmentExamList = enrolmentExamRepository.findAll();
        assertThat(enrolmentExamList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(EnrolmentExam.class);
        EnrolmentExam enrolmentExam1 = new EnrolmentExam();
        enrolmentExam1.setId(1L);
        EnrolmentExam enrolmentExam2 = new EnrolmentExam();
        enrolmentExam2.setId(enrolmentExam1.getId());
        assertThat(enrolmentExam1).isEqualTo(enrolmentExam2);
        enrolmentExam2.setId(2L);
        assertThat(enrolmentExam1).isNotEqualTo(enrolmentExam2);
        enrolmentExam1.setId(null);
        assertThat(enrolmentExam1).isNotEqualTo(enrolmentExam2);
    }
}
