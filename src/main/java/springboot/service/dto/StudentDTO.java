package springboot.service.dto;


import javax.validation.constraints.*;
import java.io.Serializable;
import java.util.Objects;

/**
 * A DTO for the Student entity.
 */
public class StudentDTO implements Serializable {

	private static final long serialVersionUID = 1L;

	private Long id;

    @NotNull
    @Size(min = 2, max = 10)
    private String fullname;
    
    private Integer groupNo;

    @Min(value = 0)
    @Max(value = 100)
    private Integer proClaScore;

    @Min(value = 0)
    @Max(value = 50)
    private Integer engWirScore;

    @Min(value = 0)
    @Max(value = 50)
    private Integer engSpeScore;

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

    @Min(value = 0)
    @Max(value = 350)
    private Integer oriTotScore;

    @Min(value = 0)
    @Max(value = 500)
    private Integer lasTotScore;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
    public String getFullname() {
        return fullname;
    }

    public void setFullname(String fullname) {
        this.fullname = fullname;
    }
    
    public Integer getGroupNo() {
		return groupNo;
	}
    public void setGroupNo(Integer groupNo) {
		this.groupNo = groupNo;
	}
    public Integer getProClaScore() {
        return proClaScore;
    }

    public void setProClaScore(Integer proClaScore) {
        this.proClaScore = proClaScore;
    }
    public Integer getEngWirScore() {
        return engWirScore;
    }

    public void setEngWirScore(Integer engWirScore) {
        this.engWirScore = engWirScore;
    }
    public Integer getEngSpeScore() {
        return engSpeScore;
    }

    public void setEngSpeScore(Integer engSpeScore) {
        this.engSpeScore = engSpeScore;
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
    public Integer getOriTotScore() {
        return oriTotScore;
    }

    public void setOriTotScore(Integer oriTotScore) {
        this.oriTotScore = oriTotScore;
    }
    public Integer getLasTotScore() {
        return lasTotScore;
    }

    public void setLasTotScore(Integer lasTotScore) {
        this.lasTotScore = lasTotScore;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        StudentDTO studentDTO = (StudentDTO) o;

        if ( ! Objects.equals(id, studentDTO.id)) { return false; }

        return true;
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "StudentDTO{" +
            "id=" + id +
            ", fullname='" + fullname + "'" +
            ", groupNo='" + groupNo + "'" +
            ", proClaScore='" + proClaScore + "'" +
            ", engWirScore='" + engWirScore + "'" +
            ", engSpeScore='" + engSpeScore + "'" +
            ", compInteSco1='" + compInteSco1 + "'" +
            ", compInteSco2='" + compInteSco2 + "'" +
            ", compInteSco3='" + compInteSco3 + "'" +
            ", compInteSco4='" + compInteSco4 + "'" +
            ", compInteSco5='" + compInteSco5 + "'" +
            ", oriTotScore='" + oriTotScore + "'" +
            ", lasTotScore='" + lasTotScore + "'" +
            '}';
    }
}
