package springboot.rest;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Optional;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.annotations.ApiParam;
import springboot.config.Constants;
import springboot.domain.User;
import springboot.repository.UserRepository;
import springboot.rest.util.HeaderUtil;
import springboot.rest.util.PaginationUtil;
import springboot.rest.util.ResponseUtil;
import springboot.rest.vm.UserVM;
import springboot.security.RoleConstants;
import springboot.service.UserService;
import springboot.service.dto.UserDTO;

//@RestController
//@RequestMapping("/api")
public class UserResource {
	
	private final Logger logger = LoggerFactory.getLogger(UserResource.class);
	
	private static final String ENTITY_NAME = "user";
	
	private final UserRepository userRepository;
	
	private final UserService userService;
	
	public UserResource(UserRepository userRepository, UserService userService){
		this.userRepository = userRepository;
		this.userService = userService;
	}
	
	/**
	 * GET  /users : get all users.
	 * 
	 * @param pageable the pagination information
	 * @return the ResponseEntity with status 200 (OK) and with body all users
	 */
	@GetMapping("/users")
	public ResponseEntity<List<UserDTO>> getAllUsers(@ApiParam Pageable pageable){
		final Page<UserDTO> page = userRepository.findAll(pageable).map(UserDTO::new);
		HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/users");
		return new ResponseEntity<List<UserDTO>>(page.getContent(), headers, HttpStatus.OK);
	}
	
	/**
	 * GET  /users/:username : get the "username" user.
	 * 
	 * @param username
	 * @return the ResponseEntity with status 200 (OK) and with body the "login" user, or with status 404 (Not Found)
	 */
	@GetMapping("/users/{username:" + Constants.LOGIN_REGEX +"}")
	public ResponseEntity<UserDTO> getUser(@PathVariable String username){
		logger.debug("REST request to get User : {}", username);
		return ResponseUtil.wrapOrNotFound(
				userRepository.findOneWithRolesByUsername(username).
				map(UserDTO::new));
	}
	
	/**
	 * POST  /users  : Creates a new user.
	 * 
	 * @param userVM
	 * @return
	 * @throws URISyntaxException
	 */
	@PostMapping("/users")
//	@Secured(RoleConstants.ADMIN)
	public ResponseEntity<Void> createUser(@RequestBody UserVM userVM) throws URISyntaxException{
		logger.debug("REST request to save User : {}", userVM);
		
		if(userVM.getId() != null){
			return ResponseEntity.badRequest()
					.headers(HeaderUtil.createFailureAlert(ENTITY_NAME, "idexists", "A new user cannot already have an ID"))
					.build();
		} else if(userRepository.findOneByUsername(userVM.getUsername()).isPresent()){
			return ResponseEntity.badRequest()
					.headers(HeaderUtil.createFailureAlert(ENTITY_NAME, "usernameexists", "Username already in use"))
					.build();
		} else {
			User newUser = userService.createUser(userVM);
			return ResponseEntity.created(new URI("/api/users/" + newUser.getUsername()))
					.headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, newUser.getUsername()))
					.build();
		}
	}
	
	
	/**
	 * PUT  /users : Updates an existing User.
	 * 
	 * @param userVM
	 * @return
	 */
	@PutMapping("/users")
//	@Secured(RoleConstants.ADMIN)
	public ResponseEntity<UserDTO> updateUser(@RequestBody UserVM userVM){
		logger.debug("REST request to update User : {}", userVM);
		Optional<User> existingUser = userRepository.findOneByUsername(userVM.getUsername());
		if(existingUser.isPresent() && !existingUser.get().getId().equals(userVM.getId())){
			return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert(ENTITY_NAME, "usernameexists", "Username already in use")).build();
		}
		Optional<UserDTO> updatedUser = userService.updateUser(userVM);
		
		return ResponseUtil.wrapOrNotFound(updatedUser, HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, userVM.getUsername()));
	}
	
	
	@DeleteMapping("/users/{username:" + Constants.LOGIN_REGEX + "}")
//	@Secured(RoleConstants.ADMIN)
	public ResponseEntity<Void> deleteUser(@PathVariable String username){
		logger.debug("REST request to delete User: {}", username);
		userService.deleteUser(username);
		return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, username))
				.build();
	}
	
	@PostMapping(path = "/users/change_password/{username:" + Constants.LOGIN_REGEX + "}",
			produces = MediaType.TEXT_PLAIN_VALUE)
//	@Secured(RoleConstants.ADMIN)
	public ResponseEntity<String> changePassword(@PathVariable String username, @RequestBody String password){
		if(!checkPasswordLength(password)){
			return new ResponseEntity<String>("Incorrect password", HttpStatus.BAD_REQUEST);
		}
		userService.changePassword(username, password);
		return new ResponseEntity<String>(HttpStatus.OK);
	}
	
	private boolean checkPasswordLength(String password) {
        return !StringUtils.isEmpty(password) &&
            password.length() >= UserVM.PASSWORD_MIN_LENGTH &&
            password.length() <= UserVM.PASSWORD_MAX_LENGTH;
    }
	
	

}
