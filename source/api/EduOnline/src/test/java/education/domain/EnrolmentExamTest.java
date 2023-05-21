package education.domain;

import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;
import education.web.rest.TestUtil;

public class EnrolmentExamTest {

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
