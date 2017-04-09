package springboot.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.data.rest.core.annotation.RestResource;

import springboot.domain.SysUser;

@RepositoryRestResource(path="users",itemResourceRel="user", collectionResourceRel="users")
public interface SysUserRepository extends JpaRepository<SysUser, Long> {
	
	@RestResource(path="username")
	SysUser findByUsername(@Param("username") String username);

}
