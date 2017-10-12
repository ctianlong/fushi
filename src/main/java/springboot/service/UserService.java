package springboot.service;

import java.util.Optional;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import springboot.domain.Role;
import springboot.domain.User;
import springboot.repository.RoleRepository;
import springboot.repository.UserRepository;
import springboot.security.RoleConstants;
import springboot.service.dto.UserDTO;
import springboot.service.mapper.UserMapper;

/**
 * manage users
 * @author Tianlong
 *
 */
@Service
@Transactional
public class UserService {

	private final Logger logger = LoggerFactory.getLogger(UserService.class);
	
	private final UserRepository userRepository;
	
	private final RoleRepository roleRepository;
	
	private final PasswordEncoder passwordEncoder;
	
	private final UserMapper userMapper;
	
	public UserService(UserRepository userRepository, RoleRepository roleRepository, UserMapper userMapper,
			 PasswordEncoder passwordEncoder){
		this.userRepository = userRepository;
		this.roleRepository = roleRepository;
		this.userMapper = userMapper;
		this.passwordEncoder = passwordEncoder;
	}
	
	/**
	 * Create user with the fixed role ROLE_USER
	 * 
	 * @param username
	 * @param password
	 * @return
	 */
	public User createUser(String username, String password){
		User newUser = new User();
		newUser.setUsername(username);
		newUser.setPassword(passwordEncoder.encode(password));
		newUser.getRoles().add(roleRepository.findOne(RoleConstants.USER));
		userRepository.save(newUser);
		logger.debug("Created Information for User: {}", username);
		return newUser;
	}
	
	/**
	 * Create user with given roles
	 * 
	 * @param userDTO
	 * @return
	 */
	public User createUser(UserDTO userDTO){
		User user = userMapper.userDTOToUser(userDTO);
		//创建用户时默认密码：000000
		user.setPassword(passwordEncoder.encode("000000"));
		userRepository.save(user);
		logger.debug("Created Information for User: {}", user);
		return user;
	}
	
	/**
	 * Update basic information (first name, last name, email, language) for the current user.
	 * 
	 * @param username
	 */
	public void updateUser(String otherInformation){
	}
	
	/**
	 * Update all information for a specific user, and return the modified user.
	 * 
	 * @param userDTO
	 * @return
	 */
	public Optional<UserDTO> updateUser(UserDTO userDTO){
		return Optional.ofNullable(userRepository.findOne(userDTO.getId()))
				.map(user -> {
					user.setUsername(userDTO.getUsername());
					user.setFullname(userDTO.getFullname());
					user.setGroupNo(userDTO.getGroupNo());
					
					//使用Optional判断null
//					Optional.ofNullable(userDTO.getRoles()).ifPresent(roles -> {
//						user.setRoles(userMapper.rolesFromsStrings(roles));
//					});
					
					//查询操作少，修改操作多
//					if(userDTO.getRoles() != null){
//						user.setRoles(userMapper.rolesFromsStrings(userDTO.getRoles()));
//					}
					
					//该方式更好，通过相对多的查询来减少对数据库的修改或插入操作
					//以查询操作增多来换取修改操作减少
					if(userDTO.getRoles() != null){
						Set<Role> roles = user.getRoles();
						roles.clear();
						userDTO.getRoles().stream()
							.map(roleRepository::findOne)
							.forEach(roles::add);
					}
					logger.debug("Changed Information for User: {}", user);
					return userDTO;
				});
	}

	public void deleteUser(String username) {
		userRepository.findOneByUsername(username).ifPresent(user -> {
			userRepository.delete(user);
			logger.debug("Deleted User: {}",username);
		});
	}

	public void changePassword(String username, String password) {
		userRepository.findOneByUsername(username).ifPresent(user -> {
			user.setPassword(passwordEncoder.encode(password));
			logger.debug("Changed password for User: {}", username);
		});
	}
	
}
