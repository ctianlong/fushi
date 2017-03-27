package springboot.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import springboot.domain.SysUser;

public interface SysUserRepository extends JpaRepository<SysUser, Long> {
	
	SysUser findByUsername(String username);

}
