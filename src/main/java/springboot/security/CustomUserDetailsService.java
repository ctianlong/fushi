package springboot.security;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import springboot.domain.User;
import springboot.repository.UserRepository;

@Service("userDetailsService")
public class CustomUserDetailsService implements UserDetailsService {
	
	private final Logger logger = LoggerFactory.getLogger(CustomUserDetailsService.class);
	
	private final UserRepository userRepository;
	
	public CustomUserDetailsService(UserRepository userRepository) {
		this.userRepository = userRepository;
	}

	@Override
	@Transactional(readOnly = true)
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		logger.debug("开始认证用户：{}", username);
		Optional<User> userFromDatabase = userRepository.findOneWithRolesByUsername(username);
		return userFromDatabase.map(user -> {
			List<GrantedAuthority> grantedAuthorities = user.getRoles().stream()
					.map(role -> new SimpleGrantedAuthority(role.getName()))
					.collect(Collectors.toList());
			return new SecurityUser(username, user.getPassword(), user.isEnabled(), true, true, true, grantedAuthorities, user.getId(),
				user.getFullname(), user.getGroupNo(), user.getAgreed(), user.getSubmitted());
		}).orElseThrow(() -> new UsernameNotFoundException("用户" + username + "不存在"));
	}

}
