package springboot.repository;


import java.util.List;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import springboot.domain.Student;

/**
 * Spring Data JPA repository for the Student entity.
 */
public interface StudentRepository extends JpaRepository<Student,Long> {

	//注意添加distinct解决多对多或一对多查询中左外连接拼表造成的重复记录问题
	//也可以用返回 Set 去重
	@Query("select distinct s from Student s left join fetch s.items")
	List<Student> findAllWithItems();
	
	@EntityGraph(attributePaths = "items")
	Student findOneWithItemsById(Long id);

}
