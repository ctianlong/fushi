package springboot.service;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import springboot.domain.SysUser;
import springboot.repository.SysUserRepository;

public class CustomUserService implements UserDetailsService {
	
	private static final Logger logger = Logger.getLogger(CustomUserService.class);
	
	@Autowired
	SysUserRepository sysUserRepository;

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		SysUser user = sysUserRepository.findByUsername(username);
		if(user == null){
			throw new UsernameNotFoundException("用户名不存在");
		}
		logger.info("用户" + username + "尝试登陆");
		return user;
	}

}
