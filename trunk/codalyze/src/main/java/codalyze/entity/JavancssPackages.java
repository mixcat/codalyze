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
@Table(name="javancss_packages")
public class JavancssPackages implements java.io.Serializable {

	private Long id;
	private String name;
	private Integer classes;
	private Integer functions;
	private Integer ncss;
	private Integer javadocs;
	private Integer javadocLines;
	private Integer singleCommentLines;
	private Integer multiCommentLines;
	private JavancssImports imports;

	public JavancssPackages() {
	}

	public JavancssPackages(JavancssImports imports, String name,
			int classes, int functions, int ncss, int javadocs, int javadocLines,
			int singleCommentLines, int multiCommentLines) {
			this.imports = imports;
			this.name = name;
			this.classes = classes;
			this.functions = functions;
			this.ncss = ncss;
			this.javadocs = javadocs;
			this.javadocLines = javadocLines;
			this.singleCommentLines = singleCommentLines;
			this.multiCommentLines = multiCommentLines;
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
	public Integer getJavadocLines() {
		return this.javadocLines;
	}

	@Column(nullable=false)
	public Integer getJavadocs() {
		return this.javadocs;
	}

	@Column(nullable=false)
	public Integer getMultiCommentLines() {
		return this.multiCommentLines;
	}

	@Column(nullable=false, length=766)
	public String getName() {
		return this.name;
	}

	@Column(nullable=false)
	public Integer getNcss() {
		return this.ncss;
	}

	@Column(nullable=false)
	public Integer getSingleCommentLines() {
		return this.singleCommentLines;
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

	public void setImports(JavancssImports imports) {
		this.imports = imports;
	}

	public void setJavadocLines(Integer javadocLines) {
		this.javadocLines = javadocLines;
	}

	public void setJavadocs(Integer javadocs) {
		this.javadocs = javadocs;
	}

	public void setMultiCommentLines(Integer multiCommentLines) {
		this.multiCommentLines = multiCommentLines;
	}

	public void setName(String name) {
		this.name = name;
	}

	public void setNcss(Integer ncss) {
		this.ncss = ncss;
	}

	public void setSingleCommentLines(Integer singleCommentLines) {
		this.singleCommentLines = singleCommentLines;
	}

}
