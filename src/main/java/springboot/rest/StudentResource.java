package springboot.rest;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Optional;

import javax.validation.Valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import springboot.domain.Student;
import springboot.repository.StudentRepository;
import springboot.rest.util.HeaderUtil;
import springboot.rest.util.ResponseUtil;
import springboot.service.dto.StudentDTO;
import springboot.service.mapper.StudentMapper;

/**
 * REST controller for managing Student.
 */
//@RestController
//@RequestMapping("/api")
public class StudentResource {

    private final Logger log = LoggerFactory.getLogger(StudentResource.class);

    private static final String ENTITY_NAME = "student";
        
    private final StudentRepository studentRepository;

    private final StudentMapper studentMapper;

    public StudentResource(StudentRepository studentRepository, StudentMapper studentMapper) {
        this.studentRepository = studentRepository;
        this.studentMapper = studentMapper;
    }

    /**
     * POST  /students : Create a new student.
     *
     * @param studentDTO the studentDTO to create
     * @return the ResponseEntity with status 201 (Created) and with body the new studentDTO, or with status 400 (Bad Request) if the student has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/students")
    public ResponseEntity<StudentDTO> createStudent(@Valid @RequestBody StudentDTO studentDTO) throws URISyntaxException {
        log.debug("REST request to save Student : {}", studentDTO);
        if (studentDTO.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert(ENTITY_NAME, "idexists", "A new student cannot already have an ID")).body(null);
        }
        Student student = studentMapper.studentDTOToStudent(studentDTO);
        student = studentRepository.save(student);
        StudentDTO result = studentMapper.studentToStudentDTO(student);
        return ResponseEntity.created(new URI("/api/students/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /students : Updates an existing student.
     *
     * @param studentDTO the studentDTO to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated studentDTO,
     * or with status 400 (Bad Request) if the studentDTO is not valid,
     * or with status 500 (Internal Server Error) if the studentDTO couldnt be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/students")
    public ResponseEntity<StudentDTO> updateStudent(@Valid @RequestBody StudentDTO studentDTO) throws URISyntaxException {
        log.debug("REST request to update Student : {}", studentDTO);
        if (studentDTO.getId() == null) {
            return createStudent(studentDTO);
        }
        Student student = studentMapper.studentDTOToStudent(studentDTO);
        student = studentRepository.save(student);
        StudentDTO result = studentMapper.studentToStudentDTO(student);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, studentDTO.getId().toString()))
            .body(result);
    }

    /**
     * GET  /students : get all the students.
     *
     * @return the ResponseEntity with status 200 (OK) and the list of students in body
     */
    @GetMapping("/students")
    public List<StudentDTO> getAllStudents() {
        log.debug("REST request to get all Students");
        List<Student> students = studentRepository.findAll();
        return studentMapper.studentsToStudentDTOs(students);
    }

    /**
     * GET  /students/:id : get the "id" student.
     *
     * @param id the id of the studentDTO to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the studentDTO, or with status 404 (Not Found)
     */
    @GetMapping("/students/{id}")
    public ResponseEntity<StudentDTO> getStudent(@PathVariable Long id) {
        log.debug("REST request to get Student : {}", id);
        Student student = studentRepository.findOne(id);
        StudentDTO studentDTO = studentMapper.studentToStudentDTO(student);
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(studentDTO));
    }

    /**
     * DELETE  /students/:id : delete the "id" student.
     *
     * @param id the id of the studentDTO to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/students/{id}")
    public ResponseEntity<Void> deleteStudent(@PathVariable Long id) {
        log.debug("REST request to delete Student : {}", id);
        studentRepository.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }

}
