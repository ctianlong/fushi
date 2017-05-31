package springboot.domain;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

/**
 * A Student.
 */
@Entity
@Table(name = "student")
public class Student implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Size(min = 2, max = 10)
    @Column(name = "fullname", length = 10, nullable = false)
    private String fullname;
    
    @Column(name = "group_no")
    private Byte groupNo;
    
    @Size(min = 1, max = 20)
    @Column(name = "graduated_college", length = 20)
    private String graduatedCollege;
    
    @Size(min = 1, max = 20)
    @Column(name = "telephone", length = 20)
    private String telephone;
    
    @Size(min = 1, max = 10)
    @Column(name = "first_tutor", length = 10)
    private String firstTutor;
    
    @Size(min = 1, max = 10)
    @Column(name = "second_tutor", length = 10)
    private String secondTutor;
    
    @Size(min = 1, max = 50)
    @Column(name = "home_address", length = 50)
    private String homeAddress;

    @Min(value = 0)
    @Max(value = 100)
    @Column(name = "pro_cla_score")
    private Byte proClaScore;

    @Min(value = 0)
    @Max(value = 50)
    @Column(name = "eng_wir_score")
    private Byte engWirScore;

    @Min(value = 0)
    @Max(value = 50)
    @Column(name = "eng_spe_score")
    private Byte engSpeScore;

    @Column(name = "comp_inte_sco_1", precision = 4, scale = 2)
    private BigDecimal compInteSco1;

    @Column(name = "comp_inte_sco_2", precision = 4, scale = 2)
    private BigDecimal compInteSco2;

    @Min(value = 0)
    @Max(value = 30)
    @Column(name = "comp_inte_sco_3", precision = 4, scale = 2)
    private BigDecimal compInteSco3;

    @Min(value = 0)
    @Max(value = 30)
    @Column(name = "comp_inte_sco_4", precision = 4, scale = 2)
    private BigDecimal compInteSco4;

    @Min(value = 0)
    @Max(value = 30)
    @Column(name = "comp_inte_sco_5", precision = 4, scale = 2)
    private BigDecimal compInteSco5;
    
    @Min(value = 0)
    @Max(value = 150)
    @Column(name = "comp_inte_tot_score", precision = 3, scale = 0)
    private BigDecimal compInteTotScore;

    @Min(value = 0)
    @Max(value = 350)
    @Column(name = "ori_tot_score", precision = 3, scale = 0)
    private BigDecimal oriTotScore;

    @Min(value = 0)
    @Max(value = 500)
    @Column(name = "las_tot_score", precision = 3, scale = 0)
    private BigDecimal lasTotScore;
    
    @OneToMany(mappedBy = "student")
    private Set<CompInteScoItem> items = new HashSet<>();
    
    public void setItems(Set<CompInteScoItem> items) {
		this.items = items;
	}
    
    public Set<CompInteScoItem> getItems() {
		return items;
	}

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getFullname() {
        return fullname;
    }

    public Student fullname(String fullname) {
        this.fullname = fullname;
        return this;
    }

    public void setFullname(String fullname) {
        this.fullname = fullname;
    }
    
    public Byte getGroupNo() {
		return groupNo;
	}
    
    public Student groupNo(Byte groupNo) {
    	this.groupNo = groupNo;
    	return this;
    }
    
    public void setGroupNo(Byte groupNo) {
		this.groupNo = groupNo;
	}

    public Byte getProClaScore() {
        return proClaScore;
    }

    public Student proClaScore(Byte proClaScore) {
        this.proClaScore = proClaScore;
        return this;
    }

    public void setProClaScore(Byte proClaScore) {
        this.proClaScore = proClaScore;
    }

    public Byte getEngWirScore() {
        return engWirScore;
    }

    public Student engWirScore(Byte engWirScore) {
        this.engWirScore = engWirScore;
        return this;
    }

    public void setEngWirScore(Byte engWirScore) {
        this.engWirScore = engWirScore;
    }

    public Byte getEngSpeScore() {
        return engSpeScore;
    }

    public Student engSpeScore(Byte engSpeScore) {
        this.engSpeScore = engSpeScore;
        return this;
    }

    public void setEngSpeScore(Byte engSpeScore) {
        this.engSpeScore = engSpeScore;
    }

    public BigDecimal getCompInteSco1() {
        return compInteSco1;
    }

    public Student compInteSco1(BigDecimal compInteSco1) {
        this.compInteSco1 = compInteSco1;
        return this;
    }

    public void setCompInteSco1(BigDecimal compInteSco1) {
        this.compInteSco1 = compInteSco1;
    }

    public BigDecimal getCompInteSco2() {
        return compInteSco2;
    }

    public Student compInteSco2(BigDecimal compInteSco2) {
        this.compInteSco2 = compInteSco2;
        return this;
    }

    public void setCompInteSco2(BigDecimal compInteSco2) {
        this.compInteSco2 = compInteSco2;
    }

    public BigDecimal getCompInteSco3() {
        return compInteSco3;
    }

    public Student compInteSco3(BigDecimal compInteSco3) {
        this.compInteSco3 = compInteSco3;
        return this;
    }

    public void setCompInteSco3(BigDecimal compInteSco3) {
        this.compInteSco3 = compInteSco3;
    }

    public BigDecimal getCompInteSco4() {
        return compInteSco4;
    }

    public Student compInteSco4(BigDecimal compInteSco4) {
        this.compInteSco4 = compInteSco4;
        return this;
    }

    public void setCompInteSco4(BigDecimal compInteSco4) {
        this.compInteSco4 = compInteSco4;
    }

    public BigDecimal getCompInteSco5() {
        return compInteSco5;
    }

    public Student compInteSco5(BigDecimal compInteSco5) {
        this.compInteSco5 = compInteSco5;
        return this;
    }

    public void setCompInteSco5(BigDecimal compInteSco5) {
        this.compInteSco5 = compInteSco5;
    }

    public BigDecimal getOriTotScore() {
        return oriTotScore;
    }

    public Student oriTotScore(BigDecimal oriTotScore) {
        this.oriTotScore = oriTotScore;
        return this;
    }

    public void setOriTotScore(BigDecimal oriTotScore) {
        this.oriTotScore = oriTotScore;
    }

    public BigDecimal getLasTotScore() {
        return lasTotScore;
    }

    public Student lasTotScore(BigDecimal lasTotScore) {
        this.lasTotScore = lasTotScore;
        return this;
    }

    public void setLasTotScore(BigDecimal lasTotScore) {
        this.lasTotScore = lasTotScore;
    }

    public String getGraduatedCollege() {
		return graduatedCollege;
	}

    public Student graduatedCollege(String graduatedCollege) {
    	this.graduatedCollege = graduatedCollege;
    	return this;
    }
	public void setGraduatedCollege(String graduatedCollege) {
		this.graduatedCollege = graduatedCollege;
	}

	public String getTelephone() {
		return telephone;
	}

	public Student telephone(String telephone) {
		this.telephone = telephone;
		return this;
	}
	public void setTelephone(String telephone) {
		this.telephone = telephone;
	}

	public String getFirstTutor() {
		return firstTutor;
	}

	public Student firstTutor(String firstTutor) {
		this.firstTutor = firstTutor;
		return this;
	}
	public void setFirstTutor(String firstTutor) {
		this.firstTutor = firstTutor;
	}

	public String getSecondTutor() {
		return secondTutor;
	}

	public Student secondTutor(String secondTutor) {
		this.secondTutor = secondTutor;
		return this;
	}
	public void setSecondTutor(String secondTutor) {
		this.secondTutor = secondTutor;
	}

	public String getHomeAddress() {
		return homeAddress;
	}

	public Student homeAddress(String homeAddress) {
		this.homeAddress = homeAddress;
		return this;
	}
	public void setHomeAddress(String homeAddress) {
		this.homeAddress = homeAddress;
	}
	
	public BigDecimal getCompInteTotScore() {
		return compInteTotScore;
	}
	
	public Student compInteTotScore(BigDecimal compInteTotScore) {
		this.compInteTotScore = compInteTotScore;
		return this;
	}

	public void setCompInteTotScore(BigDecimal compInteTotScore) {
		this.compInteTotScore = compInteTotScore;
	}

	@Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Student student = (Student) o;
        if (student.id == null || id == null) {
            return false;
        }
        return Objects.equals(id, student.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

	@Override
	public String toString() {
		return "Student [id=" + id + ", fullname=" + fullname + ", groupNo=" + groupNo + ", graduatedCollege="
				+ graduatedCollege + ", telephone=" + telephone + ", firstTutor=" + firstTutor + ", secondTutor="
				+ secondTutor + ", homeAddress=" + homeAddress + ", proClaScore=" + proClaScore + ", engWirScore="
				+ engWirScore + ", engSpeScore=" + engSpeScore + ", compInteSco1=" + compInteSco1 + ", compInteSco2="
				+ compInteSco2 + ", compInteSco3=" + compInteSco3 + ", compInteSco4=" + compInteSco4 + ", compInteSco5="
				+ compInteSco5 + ", compInteTotScore=" + compInteTotScore + ", oriTotScore=" + oriTotScore
				+ ", lasTotScore=" + lasTotScore + ", items=" + items + "]";
	}

	

}
