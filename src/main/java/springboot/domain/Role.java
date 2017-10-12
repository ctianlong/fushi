package springboot.domain;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Entity
@Table(name="role")
public class Role implements Serializable {

	private static final long serialVersionUID = 1L;
	
	@NotNull
	@Size(min = 1, max = 50)
	@Id
	@Column(length = 50)
	private String name;
	
	@NotNull
	@Size(min = 1, max = 10)
	@Column(name = "name_zh", length = 10, nullable = false)
	private String nameZh;
	
	@Size(max = 30)
	@Column(name = "description", length = 30)
	private String Description;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
	
	public String getNameZh() {
		return nameZh;
	}
	
	public void setNameZh(String nameZh) {
		this.nameZh = nameZh;
	}
	
	public String getDescription() {
		return Description;
	}
	
	public void setDescription(String description) {
		Description = description;
	}
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((name == null) ? 0 : name.hashCode());
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
		Role other = (Role) obj;
		if (name == null) {
			if (other.name != null)
				return false;
		} else if (!name.equals(other.name))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "Role [name=" + name + ", nameZh=" + nameZh + ", Description=" + Description + "]";
	}

}
