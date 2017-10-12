package springboot.domain;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

import com.fasterxml.jackson.annotation.JsonIgnore;

import springboot.config.Constants;

@Entity
@Table(name = "user")
public class User implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	@NotNull
    @Pattern(regexp = Constants.LOGIN_REGEX)
    @Size(min = 1, max = 50)
    @Column(length = 50, unique = true, nullable = false)
	private String username;

	//控制password字段只能反序列化，不允许序列化
//	@JsonProperty(access = Access.WRITE_ONLY)
	@JsonIgnore
	@NotNull
    @Size(min = 60, max = 60)
    @Column(name = "password",length = 60,nullable = false)
	private String password;

//	@ManyToMany(cascade = { CascadeType.REFRESH })
//	@ManyToMany(cascade = { CascadeType.REFRESH }, fetch = FetchType.EAGER)
	@JsonIgnore
	@ManyToMany
	@JoinTable(
			name = "user_role",
			inverseJoinColumns = {@JoinColumn(name = "role_name", referencedColumnName="name")},
			joinColumns = {@JoinColumn(name = "user_id", referencedColumnName="id")})
	private Set<Role> roles = new HashSet<>();
	
	@NotNull
    @Column(name = "enabled", nullable = false)
    private boolean enabled = true;
	
	@Column(name = "agreed")
	private Boolean agreed;
	
	@Column(name = "submitted")
	private Boolean submitted;
	
	@NotNull
	@Size(min = 1, max = 50)
	@Column(length = 50, nullable = false)
	private String fullname;
	
	@Column(name = "group_no")
	private Byte groupNo;
	
	public Boolean getAgreed() {
		return agreed;
	}

	public void setAgreed(Boolean agreed) {
		this.agreed = agreed;
	}

	public Boolean getSubmitted() {
		return submitted;
	}

	public void setSubmitted(Boolean submitted) {
		this.submitted = submitted;
	}

	public boolean isEnabled() {
		return enabled;
	}
	
	public void setEnabled(boolean enabled) {
		this.enabled = enabled;
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

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Set<Role> getRoles() {
		return roles;
	}

	public void setRoles(Set<Role> roles) {
		this.roles = roles;
	}
	
	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}
	
	public void setPassword(String password) {
		this.password = password;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((username == null) ? 0 : username.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		User other = (User) obj;
		if (username == null) {
			if (other.username != null)
				return false;
		} else if (!username.equals(other.username))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "User [id=" + id + ", username=" + username + ", password=" + password + ", roles=" + roles
				+ ", enabled=" + enabled + ", agreed=" + agreed + ", submitted=" + submitted + ", fullname=" + fullname
				+ ", groupNo=" + groupNo + "]";
	}

}
