package springboot.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import springboot.domain.User;

//@RepositoryRestResource(path="users",itemResourceRel="user", collectionResourceRel="users")
public interface UserRepository extends JpaRepository<User, Long>, JpaSpecificationExecutor<User> {
	
//	@RestResource(path="username")
	User findByUsername(@Param("username") String username);
	
	@Transactional
	@Modifying
	@Query("update User u set u.username = ?1 where u.id = ?2")
	int updateUsernameById(String username, Long id);

	Page<User> findAllByUsernameNot(Pageable pageable, String username);
	
	@EntityGraph(attributePaths = "roles")
	User findOneWithRolesById(Long id);
	
	@EntityGraph(attributePaths = "roles")
    Optional<User> findOneWithRolesByUsername(String username);
	
	Optional<User> findOneByUsername(String username);
	
	Optional<User> findOneById(Long id);

	//注意添加distinct解决多对多或一对多查询中左外连接拼表造成的重复记录问题
	//也可以用返回 Set 去重
	@Query("select distinct u from User u left join fetch u.roles")
	List<User> findAllWithRoles();

	//查询拥有某角色的用户
	@Query("select distinct u from User u left join u.roles r where r.name = ?1")
	List<User> findAllByRolesName(String roleName);
	
}
