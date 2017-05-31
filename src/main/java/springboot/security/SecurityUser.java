package springboot.security;

import java.util.Collection;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;

public class SecurityUser extends User {

	private static final long serialVersionUID = 1L;
	
	private Long uid;
	
	private String fullname;
	
	private Byte groupNo;
	
	private Boolean agreed;
	
	private Boolean submitted;

	public SecurityUser(String username, String password, boolean enabled,
			boolean accountNonExpired, boolean credentialsNonExpired,
			boolean accountNonLocked, Collection<? extends GrantedAuthority> authorities, Long uid, String fullname,
			Byte groupNo, Boolean agreed, Boolean submitted) {
		super(username, password, enabled, accountNonExpired, credentialsNonExpired, accountNonLocked, authorities);
		this.uid = uid;
		this.fullname = fullname;
		this.groupNo = groupNo;
		this.agreed = agreed;
		this.submitted = submitted;
	}

	public Long getUid() {
		return uid;
	}
	
	public String getFullname() {
		return fullname;
	}
	
	public Byte getGroupNo() {
		return groupNo;
	}
	
	public Boolean getAgreed() {
		return agreed;
	}
	
	public Boolean getSubmitted() {
		return submitted;
	}
	
	public void setAgreed(Boolean agreed) {
		this.agreed = agreed;
	}
	
	public void setSubmitted(Boolean submitted) {
		this.submitted = submitted;
	}

}
