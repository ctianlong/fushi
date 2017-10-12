package springboot.service.dto;

import java.util.Set;
import java.util.stream.Collectors;

import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

import springboot.config.Constants;
import springboot.domain.Role;
import springboot.domain.User;

public class UserDTO {

	private Long id;

	@Pattern(regexp = Constants.LOGIN_REGEX)
	@Size(min = 1, max = 50)
	private String username;

	private Set<String> roles;
	
	@Size(min = 1, max = 50)
	private String fullname;
	
	private Byte groupNo;

	public UserDTO() {
	}

	public UserDTO(User user) {
		this(user.getId(), user.getUsername(),user.getRoles().stream().map(Role::getName).collect(Collectors.toSet()),
				user.getFullname(), user.getGroupNo());
	}

	public UserDTO(Long id, String username, Set<String> roles, String fullname, Byte groupNo) {
		this.id = id;
		this.username = username;
		this.roles = roles;
		this.fullname = fullname;
		this.groupNo = groupNo;
	}

	public Long getId() {
		return id;
	}

	public String getUsername() {
		return username;
	}

	public Set<String> getRoles() {
		return roles;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public void setRoles(Set<String> roles) {
		this.roles = roles;
	}
	
	public String getFullname() {
		return fullname;
	}

	public void setFullname(String fullname) {
		this.fullname = fullname;
	}

	public Byte getGroupNo() {
		return groupNo;
	}

	public void setGroupNo(Byte groupNo) {
		this.groupNo = groupNo;
	}

	@Override
	public String toString() {
		return "UserDTO [id=" + id + ", username=" + username + ", roles=" + roles + ", fullname=" + fullname
				+ ", groupNo=" + groupNo + "]";
	}

}
