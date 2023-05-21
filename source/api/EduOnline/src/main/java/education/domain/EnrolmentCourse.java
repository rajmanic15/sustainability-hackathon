package education.domain;


import javax.persistence.*;
import javax.validation.constraints.*;

import java.io.Serializable;
import java.time.LocalDate;

/**
 * A EnrolmentCourse.
 */
@Entity
@Table(name = "enrolment_course")
public class EnrolmentCourse implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    private Long id;

    @Column(name = "status")
    private String status;

    @NotNull
    @Column(name = "enrolment_date", nullable = false)
    private LocalDate enrolmentDate;

    @OneToOne
    @JoinColumn(unique = true)
    private Student student;

    @OneToOne
    @JoinColumn(unique = true)
    private Course course;

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getStatus() {
        return status;
    }

    public EnrolmentCourse status(String status) {
        this.status = status;
        return this;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public LocalDate getEnrolmentDate() {
        return enrolmentDate;
    }

    public EnrolmentCourse enrolmentDate(LocalDate enrolmentDate) {
        this.enrolmentDate = enrolmentDate;
        return this;
    }

    public void setEnrolmentDate(LocalDate enrolmentDate) {
        this.enrolmentDate = enrolmentDate;
    }

    public Student getStudent() {
        return student;
    }

    public EnrolmentCourse student(Student student) {
        this.student = student;
        return this;
    }

    public void setStudent(Student student) {
        this.student = student;
    }

    public Course getCourse() {
        return course;
    }

    public EnrolmentCourse course(Course course) {
        this.course = course;
        return this;
    }

    public void setCourse(Course course) {
        this.course = course;
    }
    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here, do not remove

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof EnrolmentCourse)) {
            return false;
        }
        return id != null && id.equals(((EnrolmentCourse) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    @Override
    public String toString() {
        return "EnrolmentCourse{" +
            "id=" + getId() +
            ", status='" + getStatus() + "'" +
            ", enrolmentDate='" + getEnrolmentDate() + "'" +
            "}";
    }
}
