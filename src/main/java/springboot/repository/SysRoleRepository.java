package springboot.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import springboot.domain.SysRole;

@RepositoryRestResource(path="roles",itemResourceRel="role", collectionResourceRel="roles")
public interface SysRoleRepository extends JpaRepository<SysRole, Long> {

}
