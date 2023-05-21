package education.web.rest;


import education.domain.Student;
import education.repository.StudentRepository;

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

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.sql.Connection;
import java.util.List;


import static org.assertj.core.api.Assertions.assertThat;


/**
 * Integration tests for the {@Link StudentResource} REST controller.
 */
@MicronautTest(transactional = false)
@Property(name = "micronaut.security.enabled", value = "false")
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class StudentResourceIT {

    private static final Integer DEFAULT_AGE = 10;
    private static final Integer UPDATED_AGE = 11;

    private static final Integer DEFAULT_GRADE = 1;
    private static final Integer UPDATED_GRADE = 2;

    private static final Instant DEFAULT_DATE_OF_BIRTH = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_DATE_OF_BIRTH = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final String DEFAULT_PARENT_EMAIL = "AAAAAAAAAA";
    private static final String UPDATED_PARENT_EMAIL = "BBBBBBBBBB";

    @Inject
    private StudentRepository studentRepository;

    @Inject
    private EntityManager em;

    @Inject
    SynchronousTransactionManager<Connection> transactionManager;

    @Inject @Client("/")
    RxHttpClient client;

    private Student student;

    @BeforeEach
    public void initTest() {
        student = createEntity(transactionManager, em);
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
    public static Student createEntity(TransactionOperations<Connection> transactionManager, EntityManager em) {
        Student student = new Student()
            .age(DEFAULT_AGE)
            .grade(DEFAULT_GRADE)
            .dateOfBirth(DEFAULT_DATE_OF_BIRTH)
            .parentEmail(DEFAULT_PARENT_EMAIL);
        return student;
    }

    /**
     * Delete all student entities.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static void deleteAll(TransactionOperations<Connection> transactionManager, EntityManager em) {
        TestUtil.removeAll(transactionManager, em, Student.class);
    }


    @Test
    public void createStudent() throws Exception {
        int databaseSizeBeforeCreate = studentRepository.findAll().size();


        // Create the Student
        HttpResponse<Student> response = client.exchange(HttpRequest.POST("/api/students", student), Student.class).blockingFirst();

        assertThat(response.status().getCode()).isEqualTo(HttpStatus.CREATED.getCode());

        // Validate the Student in the database
        List<Student> studentList = studentRepository.findAll();
        assertThat(studentList).hasSize(databaseSizeBeforeCreate + 1);
        Student testStudent = studentList.get(studentList.size() - 1);

        assertThat(testStudent.getAge()).isEqualTo(DEFAULT_AGE);
        assertThat(testStudent.getGrade()).isEqualTo(DEFAULT_GRADE);
        assertThat(testStudent.getDateOfBirth()).isEqualTo(DEFAULT_DATE_OF_BIRTH);
        assertThat(testStudent.getParentEmail()).isEqualTo(DEFAULT_PARENT_EMAIL);
    }

    @Test
    public void createStudentWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = studentRepository.findAll().size();

        // Create the Student with an existing ID
        student.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        @SuppressWarnings("unchecked")
        HttpResponse<Student> response = client.exchange(HttpRequest.POST("/api/students", student), Student.class)
            .onErrorReturn(t -> (HttpResponse<Student>) ((HttpClientResponseException) t).getResponse()).blockingFirst();

        assertThat(response.status().getCode()).isEqualTo(HttpStatus.BAD_REQUEST.getCode());

        // Validate the Student in the database
        List<Student> studentList = studentRepository.findAll();
        assertThat(studentList).hasSize(databaseSizeBeforeCreate);
    }


    @Test
    public void checkGradeIsRequired() throws Exception {
        int databaseSizeBeforeTest = studentRepository.findAll().size();
        // set the field null
        student.setGrade(null);

        // Create the Student, which fails.

        @SuppressWarnings("unchecked")
        HttpResponse<Student> response = client.exchange(HttpRequest.POST("/api/students", student), Student.class)
            .onErrorReturn(t -> (HttpResponse<Student>) ((HttpClientResponseException) t).getResponse()).blockingFirst();

        assertThat(response.status().getCode()).isEqualTo(HttpStatus.BAD_REQUEST.getCode());

        List<Student> studentList = studentRepository.findAll();
        assertThat(studentList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    public void checkDateOfBirthIsRequired() throws Exception {
        int databaseSizeBeforeTest = studentRepository.findAll().size();
        // set the field null
        student.setDateOfBirth(null);

        // Create the Student, which fails.

        @SuppressWarnings("unchecked")
        HttpResponse<Student> response = client.exchange(HttpRequest.POST("/api/students", student), Student.class)
            .onErrorReturn(t -> (HttpResponse<Student>) ((HttpClientResponseException) t).getResponse()).blockingFirst();

        assertThat(response.status().getCode()).isEqualTo(HttpStatus.BAD_REQUEST.getCode());

        List<Student> studentList = studentRepository.findAll();
        assertThat(studentList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    public void checkParentEmailIsRequired() throws Exception {
        int databaseSizeBeforeTest = studentRepository.findAll().size();
        // set the field null
        student.setParentEmail(null);

        // Create the Student, which fails.

        @SuppressWarnings("unchecked")
        HttpResponse<Student> response = client.exchange(HttpRequest.POST("/api/students", student), Student.class)
            .onErrorReturn(t -> (HttpResponse<Student>) ((HttpClientResponseException) t).getResponse()).blockingFirst();

        assertThat(response.status().getCode()).isEqualTo(HttpStatus.BAD_REQUEST.getCode());

        List<Student> studentList = studentRepository.findAll();
        assertThat(studentList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    public void getAllStudents() throws Exception {
        // Initialize the database
        studentRepository.saveAndFlush(student);

        // Get the studentList w/ all the students
        List<Student> students = client.retrieve(HttpRequest.GET("/api/students?eagerload=true"), Argument.listOf(Student.class)).blockingFirst();
        Student testStudent = students.get(0);


        assertThat(testStudent.getAge()).isEqualTo(DEFAULT_AGE);
        assertThat(testStudent.getGrade()).isEqualTo(DEFAULT_GRADE);
        assertThat(testStudent.getDateOfBirth()).isEqualTo(DEFAULT_DATE_OF_BIRTH);
        assertThat(testStudent.getParentEmail()).isEqualTo(DEFAULT_PARENT_EMAIL);
    }

    @Test
    public void getStudent() throws Exception {
        // Initialize the database
        studentRepository.saveAndFlush(student);

        // Get the student
        Student testStudent = client.retrieve(HttpRequest.GET("/api/students/" + student.getId()), Student.class).blockingFirst();


        assertThat(testStudent.getAge()).isEqualTo(DEFAULT_AGE);
        assertThat(testStudent.getGrade()).isEqualTo(DEFAULT_GRADE);
        assertThat(testStudent.getDateOfBirth()).isEqualTo(DEFAULT_DATE_OF_BIRTH);
        assertThat(testStudent.getParentEmail()).isEqualTo(DEFAULT_PARENT_EMAIL);
    }

    @Test
    public void getNonExistingStudent() throws Exception {
        // Get the student
        @SuppressWarnings("unchecked")
        HttpResponse<Student> response = client.exchange(HttpRequest.GET("/api/students/"+ Long.MAX_VALUE), Student.class)
            .onErrorReturn(t -> (HttpResponse<Student>) ((HttpClientResponseException) t).getResponse()).blockingFirst();

        assertThat(response.status().getCode()).isEqualTo(HttpStatus.NOT_FOUND.getCode());
    }

    @Test
    public void updateStudent() throws Exception {
        // Initialize the database
        studentRepository.saveAndFlush(student);

        int databaseSizeBeforeUpdate = studentRepository.findAll().size();

        // Update the student
        Student updatedStudent = studentRepository.findById(student.getId()).get();

        updatedStudent
            .age(UPDATED_AGE)
            .grade(UPDATED_GRADE)
            .dateOfBirth(UPDATED_DATE_OF_BIRTH)
            .parentEmail(UPDATED_PARENT_EMAIL);

        @SuppressWarnings("unchecked")
        HttpResponse<Student> response = client.exchange(HttpRequest.PUT("/api/students", updatedStudent), Student.class)
            .onErrorReturn(t -> (HttpResponse<Student>) ((HttpClientResponseException) t).getResponse()).blockingFirst();

        assertThat(response.status().getCode()).isEqualTo(HttpStatus.OK.getCode());

        // Validate the Student in the database
        List<Student> studentList = studentRepository.findAll();
        assertThat(studentList).hasSize(databaseSizeBeforeUpdate);
        Student testStudent = studentList.get(studentList.size() - 1);

        assertThat(testStudent.getAge()).isEqualTo(UPDATED_AGE);
        assertThat(testStudent.getGrade()).isEqualTo(UPDATED_GRADE);
        assertThat(testStudent.getDateOfBirth()).isEqualTo(UPDATED_DATE_OF_BIRTH);
        assertThat(testStudent.getParentEmail()).isEqualTo(UPDATED_PARENT_EMAIL);
    }

    @Test
    public void updateNonExistingStudent() throws Exception {
        int databaseSizeBeforeUpdate = studentRepository.findAll().size();

        // Create the Student

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        @SuppressWarnings("unchecked")
        HttpResponse<Student> response = client.exchange(HttpRequest.PUT("/api/students", student), Student.class)
            .onErrorReturn(t -> (HttpResponse<Student>) ((HttpClientResponseException) t).getResponse()).blockingFirst();

        assertThat(response.status().getCode()).isEqualTo(HttpStatus.BAD_REQUEST.getCode());

        // Validate the Student in the database
        List<Student> studentList = studentRepository.findAll();
        assertThat(studentList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    public void deleteStudent() throws Exception {
        // Initialize the database with one entity
        studentRepository.saveAndFlush(student);

        int databaseSizeBeforeDelete = studentRepository.findAll().size();

        // Delete the student
        @SuppressWarnings("unchecked")
        HttpResponse<Student> response = client.exchange(HttpRequest.DELETE("/api/students/"+ student.getId()), Student.class)
            .onErrorReturn(t -> (HttpResponse<Student>) ((HttpClientResponseException) t).getResponse()).blockingFirst();

        assertThat(response.status().getCode()).isEqualTo(HttpStatus.NO_CONTENT.getCode());

            // Validate the database is now empty
        List<Student> studentList = studentRepository.findAll();
        assertThat(studentList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Student.class);
        Student student1 = new Student();
        student1.setId(1L);
        Student student2 = new Student();
        student2.setId(student1.getId());
        assertThat(student1).isEqualTo(student2);
        student2.setId(2L);
        assertThat(student1).isNotEqualTo(student2);
        student1.setId(null);
        assertThat(student1).isNotEqualTo(student2);
    }
}
