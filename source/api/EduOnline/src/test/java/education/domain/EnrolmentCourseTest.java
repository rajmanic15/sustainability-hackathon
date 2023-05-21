package education.domain;

import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;
import education.web.rest.TestUtil;

public class EnrolmentCourseTest {

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
