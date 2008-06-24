package codalyze.core.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

@Entity
@Table(name="sql_query", uniqueConstraints = {@UniqueConstraint(columnNames={"title"})})
public class SQLQuery extends PersistentObject {

	private static final long serialVersionUID = 3459016577504861747L;
	private String query;
	private String title;
	private Long id;
	
	public SQLQuery() {
	}
	
	public SQLQuery(String query, String title) {
		this.query = query;
		this.title = title;
	}

	@Id @GeneratedValue(strategy = GenerationType.AUTO)
	public Long getId() {
		return id;
	}

	@Column(nullable=false)
	public String getQuery() {
		return query;
	}

	@Column(nullable=false)
	public String getTitle() {
		return title;
	}

	public void setId(Long id) {
		this.id = id;
	}
	
	public void setQuery(String query) {
		this.query = query;
	}
	
	public void setTitle(String title) {
		this.title = title;
	}

}
