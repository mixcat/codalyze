package codalyze.entity;

import java.io.Serializable;

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
@Table(name="javancss_objects")
public class JavancssObjects implements Serializable {

	private Long id;
	private String name;
	private Integer ncss;
	private Integer functions;
	private Integer classes;
	private Integer javadocs;
	private JavancssImports imports;

	public JavancssObjects() {
	}
	
	public JavancssObjects(JavancssImports imports, String name,
			int ncss, int classes, int functions, int javadocs) {
				this.imports = imports;
				this.name = name;
				this.ncss = ncss;
				this.classes = classes;
				this.functions = functions;
				this.javadocs = javadocs;
	}

	@Column(nullable=false)
	public Integer getClasses() {
		return this.classes;
	}

	@Column(nullable=false)
	public Integer getFunctions() {
		return this.functions;
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

	public void setClasses(Integer classes) {
		this.classes = classes;
	}

	public void setFunctions(Integer functions) {
		this.functions = functions;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public void setImports(JavancssImports importId) {
		this.imports = importId;
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
