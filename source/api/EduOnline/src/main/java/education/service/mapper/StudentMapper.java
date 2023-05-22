package education.service.mapper;


import education.domain.*;
import education.service.dto.StudentDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity {@link Student} and its DTO {@link StudentDTO}.
 */
@Mapper(componentModel = "jsr330", uses = {UserMapper.class, CourseMapper.class})
public interface StudentMapper extends EntityMapper<StudentDTO, Student> {

    @Mapping(source = "internalUser.id", target = "internalUserId")
    StudentDTO toDto(Student student);

    @Mapping(source = "internalUserId", target = "internalUser")
    @Mapping(target = "removeCourses", ignore = true)
    Student toEntity(StudentDTO studentDTO);

    default Student fromId(Long id) {
        if (id == null) {
            return null;
        }
        Student student = new Student();
        student.setId(id);
        return student;
    }
}
