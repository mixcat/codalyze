package codalyze.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

@Entity
@Table(name="javancss_functions")
public class JavancssFunctions implements java.io.Serializable {

	private Long id;
	private String name;
	private Integer ncss;
	private Integer ccn;
	private Integer javadocs;
	private JavancssImports imports;

	public JavancssFunctions() {
	}
	
	public JavancssFunctions(JavancssImports imports, String name,
			int ncss, int ccn, int javadocs) {
			this.imports = imports;
			this.name = name;
			this.ncss = ncss;
			this.ccn = ccn;				this.javadocs = javadocs;
	}

	@Column(nullable=false)
	public Integer getCcn() {
		return this.ccn;
	}

	@Id @GeneratedValue(strategy = GenerationType.AUTO)
	public Long getId() {
		return this.id;
	}

	@ManyToOne
	@JoinColumn(nullable=false)
	public JavancssImports getImports() {
		return this.imports;
	}

	@Column(nullable=false)
	public Integer getJavadocs() {
		return this.javadocs;
	}

	@Column(nullable=false, length=766)
	public String getName() {
		return this.name;
	}

	@Column(nullable=false)
	public Integer getNcss() {
		return this.ncss;
	}

	public void setCcn(Integer ccn) {
		this.ccn = ccn;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public void setImports(JavancssImports imports) {
		this.imports = imports;
	}

	public void setJavadocs(Integer javadocs) {
		this.javadocs = javadocs;
	}

	public void setName(String name) {
		this.name = name;
	}

	public void setNcss(Integer ncss) {
		this.ncss = ncss;
	}

}
