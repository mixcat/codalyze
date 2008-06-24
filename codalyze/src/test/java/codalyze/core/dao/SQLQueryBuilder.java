package codalyze.core.dao;

import codalyze.core.entity.SQLQuery;

public class SQLQueryBuilder {

	private String title;
	private String queryString;
	private Long id;

	public SQLQueryBuilder(String title, String query) {
		this.title = title;
		this.queryString = query;
	}
	
	public SQLQueryBuilder title(String title) {
		this.title = title;
		return this;
	}
	
	public SQLQueryBuilder query(String query) {
		this.queryString = query;
		return this;
	}
	
	public SQLQuery toQuery() {
		SQLQuery query = new SQLQuery();
		query.setTitle(title);
		query.setQuery(queryString);
		query.setId(id);
		return query;
	}

	public SQLQueryBuilder id(Long id) {
		this.id = id;
		return this;
	}
}
