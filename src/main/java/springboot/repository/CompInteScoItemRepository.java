package springboot.repository;


import java.util.List;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import springboot.domain.CompInteScoItem;

/**
 * Spring Data JPA repository for the CompInteScoItem entity.
 */
public interface CompInteScoItemRepository extends JpaRepository<CompInteScoItem,Long> {

    @Query("select c from CompInteScoItem c left join fetch c.student where c.teacher.id = ?#{principal.uid}")
    List<CompInteScoItem> findWithStudentByTeacherIsCurrentUser();
    
    @EntityGraph(attributePaths = "student")
    CompInteScoItem findOneWithStudentById(Long id);
    
    @EntityGraph(attributePaths = "student")
    List<CompInteScoItem> findAllWithStudentByTeacherId(Long id);

    @EntityGraph(attributePaths = "teacher")
	List<CompInteScoItem> findAllWithTeacherByStudentId(Long id);

    @EntityGraph(attributePaths = {"student", "teacher"})
	CompInteScoItem findOneWithStudentWithTeacherById(Long id);
    
    List<CompInteScoItem> findAllByTeacherId(Long id);

}
