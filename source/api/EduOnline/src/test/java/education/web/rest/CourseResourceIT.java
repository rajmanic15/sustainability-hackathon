package education.web.rest;


import education.domain.Course;
import education.repository.CourseRepository;

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
 * Integration tests for the {@Link CourseResource} REST controller.
 */
@MicronautTest(transactional = false)
@Property(name = "micronaut.security.enabled", value = "false")
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class CourseResourceIT {

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_DESCRIPTION = "AAAAAAAAAA";
    private static final String UPDATED_DESCRIPTION = "BBBBBBBBBB";

    private static final LocalDate DEFAULT_START_DATE = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_START_DATE = LocalDate.now(ZoneId.systemDefault());

    private static final LocalDate DEFAULT_END_DATE = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_END_DATE = LocalDate.now(ZoneId.systemDefault());

    private static final Boolean DEFAULT_IS_ACTIVE = false;
    private static final Boolean UPDATED_IS_ACTIVE = true;

    @Inject
    private CourseRepository courseRepository;

    @Inject
    private EntityManager em;

    @Inject
    SynchronousTransactionManager<Connection> transactionManager;

    @Inject @Client("/")
    RxHttpClient client;

    private Course course;

    @BeforeEach
    public void initTest() {
        course = createEntity(transactionManager, em);
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
    public static Course createEntity(TransactionOperations<Connection> transactionManager, EntityManager em) {
        Course course = new Course()
            .name(DEFAULT_NAME)
            .description(DEFAULT_DESCRIPTION)
            .startDate(DEFAULT_START_DATE)
            .endDate(DEFAULT_END_DATE)
            .isActive(DEFAULT_IS_ACTIVE);
        return course;
    }

    /**
     * Delete all course entities.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static void deleteAll(TransactionOperations<Connection> transactionManager, EntityManager em) {
        TestUtil.removeAll(transactionManager, em, Course.class);
    }


    @Test
    public void createCourse() throws Exception {
        int databaseSizeBeforeCreate = courseRepository.findAll().size();


        // Create the Course
        HttpResponse<Course> response = client.exchange(HttpRequest.POST("/api/courses", course), Course.class).blockingFirst();

        assertThat(response.status().getCode()).isEqualTo(HttpStatus.CREATED.getCode());

        // Validate the Course in the database
        List<Course> courseList = courseRepository.findAll();
        assertThat(courseList).hasSize(databaseSizeBeforeCreate + 1);
        Course testCourse = courseList.get(courseList.size() - 1);

        assertThat(testCourse.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testCourse.getDescription()).isEqualTo(DEFAULT_DESCRIPTION);
        assertThat(testCourse.getStartDate()).isEqualTo(DEFAULT_START_DATE);
        assertThat(testCourse.getEndDate()).isEqualTo(DEFAULT_END_DATE);
        assertThat(testCourse.isIsActive()).isEqualTo(DEFAULT_IS_ACTIVE);
    }

    @Test
    public void createCourseWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = courseRepository.findAll().size();

        // Create the Course with an existing ID
        course.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        @SuppressWarnings("unchecked")
        HttpResponse<Course> response = client.exchange(HttpRequest.POST("/api/courses", course), Course.class)
            .onErrorReturn(t -> (HttpResponse<Course>) ((HttpClientResponseException) t).getResponse()).blockingFirst();

        assertThat(response.status().getCode()).isEqualTo(HttpStatus.BAD_REQUEST.getCode());

        // Validate the Course in the database
        List<Course> courseList = courseRepository.findAll();
        assertThat(courseList).hasSize(databaseSizeBeforeCreate);
    }


    @Test
    public void checkNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = courseRepository.findAll().size();
        // set the field null
        course.setName(null);

        // Create the Course, which fails.

        @SuppressWarnings("unchecked")
        HttpResponse<Course> response = client.exchange(HttpRequest.POST("/api/courses", course), Course.class)
            .onErrorReturn(t -> (HttpResponse<Course>) ((HttpClientResponseException) t).getResponse()).blockingFirst();

        assertThat(response.status().getCode()).isEqualTo(HttpStatus.BAD_REQUEST.getCode());

        List<Course> courseList = courseRepository.findAll();
        assertThat(courseList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    public void checkDescriptionIsRequired() throws Exception {
        int databaseSizeBeforeTest = courseRepository.findAll().size();
        // set the field null
        course.setDescription(null);

        // Create the Course, which fails.

        @SuppressWarnings("unchecked")
        HttpResponse<Course> response = client.exchange(HttpRequest.POST("/api/courses", course), Course.class)
            .onErrorReturn(t -> (HttpResponse<Course>) ((HttpClientResponseException) t).getResponse()).blockingFirst();

        assertThat(response.status().getCode()).isEqualTo(HttpStatus.BAD_REQUEST.getCode());

        List<Course> courseList = courseRepository.findAll();
        assertThat(courseList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    public void checkStartDateIsRequired() throws Exception {
        int databaseSizeBeforeTest = courseRepository.findAll().size();
        // set the field null
        course.setStartDate(null);

        // Create the Course, which fails.

        @SuppressWarnings("unchecked")
        HttpResponse<Course> response = client.exchange(HttpRequest.POST("/api/courses", course), Course.class)
            .onErrorReturn(t -> (HttpResponse<Course>) ((HttpClientResponseException) t).getResponse()).blockingFirst();

        assertThat(response.status().getCode()).isEqualTo(HttpStatus.BAD_REQUEST.getCode());

        List<Course> courseList = courseRepository.findAll();
        assertThat(courseList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    public void checkEndDateIsRequired() throws Exception {
        int databaseSizeBeforeTest = courseRepository.findAll().size();
        // set the field null
        course.setEndDate(null);

        // Create the Course, which fails.

        @SuppressWarnings("unchecked")
        HttpResponse<Course> response = client.exchange(HttpRequest.POST("/api/courses", course), Course.class)
            .onErrorReturn(t -> (HttpResponse<Course>) ((HttpClientResponseException) t).getResponse()).blockingFirst();

        assertThat(response.status().getCode()).isEqualTo(HttpStatus.BAD_REQUEST.getCode());

        List<Course> courseList = courseRepository.findAll();
        assertThat(courseList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    public void getAllCourses() throws Exception {
        // Initialize the database
        courseRepository.saveAndFlush(course);

        // Get the courseList w/ all the courses
        List<Course> courses = client.retrieve(HttpRequest.GET("/api/courses?eagerload=true"), Argument.listOf(Course.class)).blockingFirst();
        Course testCourse = courses.get(0);


        assertThat(testCourse.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testCourse.getDescription()).isEqualTo(DEFAULT_DESCRIPTION);
        assertThat(testCourse.getStartDate()).isEqualTo(DEFAULT_START_DATE);
        assertThat(testCourse.getEndDate()).isEqualTo(DEFAULT_END_DATE);
        assertThat(testCourse.isIsActive()).isEqualTo(DEFAULT_IS_ACTIVE);
    }

    @Test
    public void getCourse() throws Exception {
        // Initialize the database
        courseRepository.saveAndFlush(course);

        // Get the course
        Course testCourse = client.retrieve(HttpRequest.GET("/api/courses/" + course.getId()), Course.class).blockingFirst();


        assertThat(testCourse.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testCourse.getDescription()).isEqualTo(DEFAULT_DESCRIPTION);
        assertThat(testCourse.getStartDate()).isEqualTo(DEFAULT_START_DATE);
        assertThat(testCourse.getEndDate()).isEqualTo(DEFAULT_END_DATE);
        assertThat(testCourse.isIsActive()).isEqualTo(DEFAULT_IS_ACTIVE);
    }

    @Test
    public void getNonExistingCourse() throws Exception {
        // Get the course
        @SuppressWarnings("unchecked")
        HttpResponse<Course> response = client.exchange(HttpRequest.GET("/api/courses/"+ Long.MAX_VALUE), Course.class)
            .onErrorReturn(t -> (HttpResponse<Course>) ((HttpClientResponseException) t).getResponse()).blockingFirst();

        assertThat(response.status().getCode()).isEqualTo(HttpStatus.NOT_FOUND.getCode());
    }

    @Test
    public void updateCourse() throws Exception {
        // Initialize the database
        courseRepository.saveAndFlush(course);

        int databaseSizeBeforeUpdate = courseRepository.findAll().size();

        // Update the course
        Course updatedCourse = courseRepository.findById(course.getId()).get();

        updatedCourse
            .name(UPDATED_NAME)
            .description(UPDATED_DESCRIPTION)
            .startDate(UPDATED_START_DATE)
            .endDate(UPDATED_END_DATE)
            .isActive(UPDATED_IS_ACTIVE);

        @SuppressWarnings("unchecked")
        HttpResponse<Course> response = client.exchange(HttpRequest.PUT("/api/courses", updatedCourse), Course.class)
            .onErrorReturn(t -> (HttpResponse<Course>) ((HttpClientResponseException) t).getResponse()).blockingFirst();

        assertThat(response.status().getCode()).isEqualTo(HttpStatus.OK.getCode());

        // Validate the Course in the database
        List<Course> courseList = courseRepository.findAll();
        assertThat(courseList).hasSize(databaseSizeBeforeUpdate);
        Course testCourse = courseList.get(courseList.size() - 1);

        assertThat(testCourse.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testCourse.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
        assertThat(testCourse.getStartDate()).isEqualTo(UPDATED_START_DATE);
        assertThat(testCourse.getEndDate()).isEqualTo(UPDATED_END_DATE);
        assertThat(testCourse.isIsActive()).isEqualTo(UPDATED_IS_ACTIVE);
    }

    @Test
    public void updateNonExistingCourse() throws Exception {
        int databaseSizeBeforeUpdate = courseRepository.findAll().size();

        // Create the Course

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        @SuppressWarnings("unchecked")
        HttpResponse<Course> response = client.exchange(HttpRequest.PUT("/api/courses", course), Course.class)
            .onErrorReturn(t -> (HttpResponse<Course>) ((HttpClientResponseException) t).getResponse()).blockingFirst();

        assertThat(response.status().getCode()).isEqualTo(HttpStatus.BAD_REQUEST.getCode());

        // Validate the Course in the database
        List<Course> courseList = courseRepository.findAll();
        assertThat(courseList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    public void deleteCourse() throws Exception {
        // Initialize the database with one entity
        courseRepository.saveAndFlush(course);

        int databaseSizeBeforeDelete = courseRepository.findAll().size();

        // Delete the course
        @SuppressWarnings("unchecked")
        HttpResponse<Course> response = client.exchange(HttpRequest.DELETE("/api/courses/"+ course.getId()), Course.class)
            .onErrorReturn(t -> (HttpResponse<Course>) ((HttpClientResponseException) t).getResponse()).blockingFirst();

        assertThat(response.status().getCode()).isEqualTo(HttpStatus.NO_CONTENT.getCode());

            // Validate the database is now empty
        List<Course> courseList = courseRepository.findAll();
        assertThat(courseList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Course.class);
        Course course1 = new Course();
        course1.setId(1L);
        Course course2 = new Course();
        course2.setId(course1.getId());
        assertThat(course1).isEqualTo(course2);
        course2.setId(2L);
        assertThat(course1).isNotEqualTo(course2);
        course1.setId(null);
        assertThat(course1).isNotEqualTo(course2);
    }
}
