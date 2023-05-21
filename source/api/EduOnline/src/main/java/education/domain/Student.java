package education.domain;


import javax.persistence.*;
import javax.validation.constraints.*;

import java.io.Serializable;
import java.time.Instant;

/**
 * A Student.
 */
@Entity
@Table(name = "student")
public class Student implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    private Long id;

    @Min(value = 10)
    @Max(value = 100)
    @Column(name = "age")
    private Integer age;

    @NotNull
    @Min(value = 1)
    @Max(value = 12)
    @Column(name = "grade", nullable = false)
    private Integer grade;

    @NotNull
    @Column(name = "date_of_birth", nullable = false)
    private Instant dateOfBirth;

    @NotNull
    @Size(min = 5, max = 254)
    @Column(name = "parent_email", length = 254, nullable = false)
    private String parentEmail;

    @OneToOne
    @JoinColumn(unique = true)
    private User internalUser;

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getAge() {
        return age;
    }

    public Student age(Integer age) {
        this.age = age;
        return this;
    }

    public void setAge(Integer age) {
        this.age = age;
    }

    public Integer getGrade() {
        return grade;
    }

    public Student grade(Integer grade) {
        this.grade = grade;
        return this;
    }

    public void setGrade(Integer grade) {
        this.grade = grade;
    }

    public Instant getDateOfBirth() {
        return dateOfBirth;
    }

    public Student dateOfBirth(Instant dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
        return this;
    }

    public void setDateOfBirth(Instant dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }

    public String getParentEmail() {
        return parentEmail;
    }

    public Student parentEmail(String parentEmail) {
        this.parentEmail = parentEmail;
        return this;
    }

    public void setParentEmail(String parentEmail) {
        this.parentEmail = parentEmail;
    }

    public User getInternalUser() {
        return internalUser;
    }

    public Student internalUser(User user) {
        this.internalUser = user;
        return this;
    }

    public void setInternalUser(User user) {
        this.internalUser = user;
    }
    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here, do not remove

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Student)) {
            return false;
        }
        return id != null && id.equals(((Student) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    @Override
    public String toString() {
        return "Student{" +
            "id=" + getId() +
            ", age=" + getAge() +
            ", grade=" + getGrade() +
            ", dateOfBirth='" + getDateOfBirth() + "'" +
            ", parentEmail='" + getParentEmail() + "'" +
            "}";
    }
}
