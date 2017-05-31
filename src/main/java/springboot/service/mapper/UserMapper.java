package springboot.service.mapper;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import springboot.domain.Role;
import springboot.domain.User;
import springboot.service.dto.UserDTO;

@Mapper(componentModel = "spring")
public interface UserMapper {

	UserDTO userToUserDTO(User user);
	
	List<UserDTO> usersToUserDTOs(List<User> users);
	
	@Mapping(target = "password", ignore = true)
	User userDTOToUser(UserDTO userDTO);
	
	List<User> userDTOsToUsers(List<UserDTO> userDTO);
	
	default User userFromId(Long id){
		if(id == null) return null;
		User user = new User();
		user.setId(id);
		return user;
	}
	
	default Set<String> stringsFromRoles(Set<Role> roles){
		return roles.stream().map(Role::getName).collect(Collectors.toSet());
	}
	
	default Set<Role> rolesFromsStrings(Set<String> strings){
		return strings.stream().map(s -> {
			Role r = new Role();
			r.setName(s);
			return r;
		}).collect(Collectors.toSet());
	}

}
