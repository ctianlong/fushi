package springboot.service.mapper;

import java.util.List;

import org.mapstruct.Mapper;

import springboot.domain.Student;
import springboot.service.dto.StudentDTO;

/**
 * Mapper for the entity Student and its DTO StudentDTO.
 */
@Mapper(componentModel = "spring", uses = {})
public interface StudentMapper {

    StudentDTO studentToStudentDTO(Student student);

    List<StudentDTO> studentsToStudentDTOs(List<Student> students);

    Student studentDTOToStudent(StudentDTO studentDTO);

    List<Student> studentDTOsToStudents(List<StudentDTO> studentDTOs);
    /**
     * generating the fromId for all mappers if the databaseType is sql, as the class has relationship to it might need it, instead of
     * creating a new attribute to know if the entity has any relationship from some other entity
     *
     * @param id id of the entity
     * @return the entity instance
     */
     
    default Student studentFromId(Long id) {
        if (id == null) {
            return null;
        }
        Student student = new Student();
        student.setId(id);
        return student;
    }
    

}
