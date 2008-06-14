package codalyze.entity;


import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

@Entity
@Table(name="javancss_imports", uniqueConstraints = {@UniqueConstraint(columnNames={"date"})} )
public class JavancssImports implements java.io.Serializable {

	private Long id;
	private Date date;
	private String metadata;

	
	public JavancssImports() {
	}
	
	public JavancssImports(Date date, String metadata) {
		this.date = date;
		this.metadata = metadata;
	}
	
	@Column(nullable=false)
	public Date getDate() {
		return this.date;
	}

	@Id @GeneratedValue(strategy = GenerationType.AUTO)
	public Long getId() {
		return this.id;
	}

	@Column(nullable=false)
	public String getMetadata() {
		return this.metadata;
	}

	public void setDate(Date date) {
		this.date = date;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public void setMetadata(String metadata) {
		this.metadata = metadata;
	}

}
