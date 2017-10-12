package springboot.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import springboot.domain.Role;

//@RepositoryRestResource(path="roles",itemResourceRel="role", collectionResourceRel="roles")
public interface RoleRepository extends JpaRepository<Role, String> {

}
