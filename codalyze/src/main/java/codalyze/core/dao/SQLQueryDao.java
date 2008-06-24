package codalyze.core.dao;

import java.util.List;

import org.hibernate.SessionFactory;
import org.hibernate.criterion.Restrictions;

import codalyze.core.entity.SQLQuery;

public class SQLQueryDao {
	
	private final SessionFactory sessionFactory;

	public SQLQueryDao(SessionFactory sessionFactory) {
		this.sessionFactory = sessionFactory;
	}

	public void save(SQLQuery query) {
		sessionFactory.getCurrentSession().save(query);
	}
	
	public void update(SQLQuery query) {
		sessionFactory.getCurrentSession().update(query);
		sessionFactory.getCurrentSession().flush();
	}

	public SQLQuery findByTitle(String title) {
		return (SQLQuery) sessionFactory.getCurrentSession().createCriteria(SQLQuery.class)
		.add(Restrictions.eq("title", title)).uniqueResult();
	}

	public List<SQLQuery> findAll() {
		return sessionFactory.getCurrentSession().createCriteria(SQLQuery.class).list();
	}

}
