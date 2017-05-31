package springboot.service.dto;


import java.io.Serializable;
import java.util.Objects;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;

/**
 * A DTO for the CompInteScoItem entity.
 */
public class CompInteScoItemDTO implements Serializable {

	private static final long serialVersionUID = 1L;

	private Long id;

    @Min(value = 0)
    @Max(value = 30)
    private Integer compInteSco1;

    @Min(value = 0)
    @Max(value = 30)
    private Integer compInteSco2;

    @Min(value = 0)
    @Max(value = 30)
    private Integer compInteSco3;

    @Min(value = 0)
    @Max(value = 30)
    private Integer compInteSco4;

    @Min(value = 0)
    @Max(value = 30)
    private Integer compInteSco5;

    private Long teacherId;
    
    private String teacherName;

    private Long studentId;
    
    private String studentName;

    public String getTeacherName() {
		return teacherName;
	}

	public void setTeacherName(String teacherName) {
		this.teacherName = teacherName;
	}

	public String getStudentName() {
		return studentName;
	}

	public void setStudentName(String studentName) {
		this.studentName = studentName;
	}

	public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
    public Integer getCompInteSco1() {
        return compInteSco1;
    }

    public void setCompInteSco1(Integer compInteSco1) {
        this.compInteSco1 = compInteSco1;
    }
    public Integer getCompInteSco2() {
        return compInteSco2;
    }

    public void setCompInteSco2(Integer compInteSco2) {
        this.compInteSco2 = compInteSco2;
    }
    public Integer getCompInteSco3() {
        return compInteSco3;
    }

    public void setCompInteSco3(Integer compInteSco3) {
        this.compInteSco3 = compInteSco3;
    }
    public Integer getCompInteSco4() {
        return compInteSco4;
    }

    public void setCompInteSco4(Integer compInteSco4) {
        this.compInteSco4 = compInteSco4;
    }
    public Integer getCompInteSco5() {
        return compInteSco5;
    }

    public void setCompInteSco5(Integer compInteSco5) {
        this.compInteSco5 = compInteSco5;
    }

    public Long getTeacherId() {
        return teacherId;
    }

    public void setTeacherId(Long userId) {
        this.teacherId = userId;
    }

    public Long getStudentId() {
        return studentId;
    }

    public void setStudentId(Long studentId) {
        this.studentId = studentId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        CompInteScoItemDTO compInteScoItemDTO = (CompInteScoItemDTO) o;

        if ( ! Objects.equals(id, compInteScoItemDTO.id)) { return false; }

        return true;
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

	@Override
	public String toString() {
		return "CompInteScoItemDTO [id=" + id + ", compInteSco1=" + compInteSco1 + ", compInteSco2=" + compInteSco2
				+ ", compInteSco3=" + compInteSco3 + ", compInteSco4=" + compInteSco4 + ", compInteSco5=" + compInteSco5
				+ ", teacherId=" + teacherId + ", teacherName=" + teacherName + ", studentId=" + studentId
				+ ", studentName=" + studentName + "]";
	}

}
