package springboot.rest.vm;

import java.util.Set;

import javax.validation.constraints.Size;

import springboot.service.dto.UserDTO;

public class UserVM extends UserDTO {

	public static final int PASSWORD_MIN_LENGTH = 4;
	public static final int PASSWORD_MAX_LENGTH = 18;
	
	@Size(min = PASSWORD_MIN_LENGTH, max = PASSWORD_MAX_LENGTH)
	private String password;

	public UserVM() {
		// needed for Jackson
	}

	public UserVM(Long id, String username, String password, Set<String> roles, String fullname, Byte groupNo) {
		super(id, username, roles, fullname, groupNo);
		this.password = password;
	}

	public void setPassword(String password) {
		this.password = password;
	}
	
	public String getPassword() {
		return password;
	}

	@Override
	public String toString() {
		return "UserVM [password=" + password + "]" + super.toString();
	}

}
