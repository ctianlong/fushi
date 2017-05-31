package springboot.domain;

import java.io.Serializable;
import java.util.Objects;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;

/**
 * A CompInteScoItem.
 */
@Entity
@Table(name = "comp_inte_sco_item")
public class CompInteScoItem implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Min(value = 0)
    @Max(value = 30)
    @Column(name = "comp_inte_sco_1")
    private Byte compInteSco1;

    @Min(value = 0)
    @Max(value = 30)
    @Column(name = "comp_inte_sco_2")
    private Byte compInteSco2;

    @Min(value = 0)
    @Max(value = 30)
    @Column(name = "comp_inte_sco_3")
    private Byte compInteSco3;

    @Min(value = 0)
    @Max(value = 30)
    @Column(name = "comp_inte_sco_4")
    private Byte compInteSco4;

    @Min(value = 0)
    @Max(value = 30)
    @Column(name = "comp_inte_sco_5")
    private Byte compInteSco5;
    
    @Min(value = 0)
    @Max(value = 150)
    @Column(name = "comp_inte_sco_sum")
    private Short compInteScoSum;

    @ManyToOne
    private User teacher;

    @ManyToOne
    private Student student;
    
    public CompInteScoItem() {
	}

    public CompInteScoItem(User teacher, Student student) {
		this.teacher = teacher;
		this.student = student;
	}

	public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
    
    public Short getCompInteScoSum() {
		return compInteScoSum;
	}
    
    public void setCompInteScoSum(Short compInteScoSum) {
		this.compInteScoSum = compInteScoSum;
	}
    public CompInteScoItem compInteScoSum(Short compInteScoSum) {
    	this.compInteScoSum = compInteScoSum;
    	return this;
    }

    public Byte getCompInteSco1() {
        return compInteSco1;
    }

    public CompInteScoItem compInteSco1(Byte compInteSco1) {
        this.compInteSco1 = compInteSco1;
        return this;
    }

    public void setCompInteSco1(Byte compInteSco1) {
        this.compInteSco1 = compInteSco1;
    }

    public Byte getCompInteSco2() {
        return compInteSco2;
    }

    public CompInteScoItem compInteSco2(Byte compInteSco2) {
        this.compInteSco2 = compInteSco2;
        return this;
    }

    public void setCompInteSco2(Byte compInteSco2) {
        this.compInteSco2 = compInteSco2;
    }

    public Byte getCompInteSco3() {
        return compInteSco3;
    }

    public CompInteScoItem compInteSco3(Byte compInteSco3) {
        this.compInteSco3 = compInteSco3;
        return this;
    }

    public void setCompInteSco3(Byte compInteSco3) {
        this.compInteSco3 = compInteSco3;
    }

    public Byte getCompInteSco4() {
        return compInteSco4;
    }

    public CompInteScoItem compInteSco4(Byte compInteSco4) {
        this.compInteSco4 = compInteSco4;
        return this;
    }

    public void setCompInteSco4(Byte compInteSco4) {
        this.compInteSco4 = compInteSco4;
    }

    public Byte getCompInteSco5() {
        return compInteSco5;
    }

    public CompInteScoItem compInteSco5(Byte compInteSco5) {
        this.compInteSco5 = compInteSco5;
        return this;
    }

    public void setCompInteSco5(Byte compInteSco5) {
        this.compInteSco5 = compInteSco5;
    }

    public User getTeacher() {
        return teacher;
    }

    public CompInteScoItem teacher(User user) {
        this.teacher = user;
        return this;
    }

    public void setTeacher(User user) {
        this.teacher = user;
    }

    public Student getStudent() {
        return student;
    }

    public CompInteScoItem student(Student student) {
        this.student = student;
        return this;
    }

    public void setStudent(Student student) {
        this.student = student;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        CompInteScoItem compInteScoItem = (CompInteScoItem) o;
        if (compInteScoItem.id == null || id == null) {
            return false;
        }
        return Objects.equals(id, compInteScoItem.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "CompInteScoItem{" +
            "id=" + id +
            ", compInteSco1='" + compInteSco1 + "'" +
            ", compInteSco2='" + compInteSco2 + "'" +
            ", compInteSco3='" + compInteSco3 + "'" +
            ", compInteSco4='" + compInteSco4 + "'" +
            ", compInteSco5='" + compInteSco5 + "'" +
            ", compInteScoSum='" + compInteScoSum + "'" +
            '}';
    }
}
