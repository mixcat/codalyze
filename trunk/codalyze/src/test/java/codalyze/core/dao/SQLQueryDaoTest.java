package codalyze.core.dao;

import static org.junit.Assert.*;

import java.util.List;

import org.hibernate.SessionFactory;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.AbstractTransactionalJUnit4SpringContextTests;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import codalyze.core.entity.SQLQuery;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations={"classpath:test-context.xml"})
public class SQLQueryDaoTest extends AbstractTransactionalJUnit4SpringContextTests {

	@Autowired
	protected SQLQueryDao queryDao;
	
	@Autowired
	protected SessionFactory sessionFactory;

	private SQLQuery query;
	private String title;
	private String queryString;
	
	@Before
	public void setUp() {
		title = "title";
		queryString = "query";
		query = new SQLQuery();
		query.setTitle(title);
		query.setQuery(queryString);
	}

	@Test
	public void testShouldSaveAndLoadNewQuery() {
		queryDao.save(query);
		Long id = query.getId();
		reset();
		SQLQuery reloadedQuery = (SQLQuery) sessionFactory.getCurrentSession().load(SQLQuery.class, id);
		assertEquals(title,reloadedQuery.getTitle());
		assertEquals(queryString,reloadedQuery.getQuery());
		assertEquals(id, reloadedQuery.getId());
	}
	
	@Test
	public void testShouldUpdateAnExistingQuery() {
		queryDao.save(query);
		Long id = query.getId();
		reset();
		SQLQuery reloadedQuery = (SQLQuery) sessionFactory.getCurrentSession().load(SQLQuery.class, id);
		reloadedQuery.setTitle("newTitle");
		reloadedQuery.setQuery("newQuery");
		queryDao.update(reloadedQuery);
		reset();
		reloadedQuery = (SQLQuery) sessionFactory.getCurrentSession().load(SQLQuery.class, id);
		assertEquals("newTitle",reloadedQuery.getTitle());
		assertEquals("newQuery",reloadedQuery.getQuery());
		assertEquals(id, reloadedQuery.getId());
	}
	
	@Test
	public void testShouldFindBytitle() {
		queryDao.save(query);
		reset();
		assertEquals(query, queryDao.findByTitle(title));
	}
	
	@Test
	public void testShouldfindAll() {
		queryDao.save(query);
		SQLQuery queryTwo = new SQLQueryBuilder("newQuery","newTitle").toQuery();
		queryDao.save(queryTwo);
		List<SQLQuery> all = queryDao.findAll();
		assertEquals(2, all.size());
		assertTrue(all.contains(query));
		assertTrue(all.contains(queryTwo));
	}
	
	@Test
	public void testShouldRejectQueryWithNullTitle() {
		query.setTitle(null);
		try {
			queryDao.save(query);
			fail();
		} catch (Exception e) {}
	}
	
	@Test
	public void testShouldRejectQueryWithNullQuery() {
		query.setQuery(null);
		try {
			queryDao.save(query);
			fail();
		} catch (Exception e) {}
	}
	
	@Test
	public void testShouldNotSaveDuplicatetitle() {
		queryDao.save(query);
		SQLQuery clone = new SQLQuery();
		clone.setQuery(query.getQuery() + "-----");
		clone.setTitle(query.getTitle());
		try {
			queryDao.save(clone);
			fail();
		} catch(Exception e) {}
	}
	
	@Test
	public void testShouldNotUpdateDuplicatetitle() {
		queryDao.save(query);
		SQLQuery clone = new SQLQueryBuilder("newQuery","newTitle").toQuery();

		queryDao.save(clone);
		Long id = clone.getId();
		reset();
		SQLQuery reloadedClone = (SQLQuery) sessionFactory.getCurrentSession().load(SQLQuery.class, id);
		reloadedClone.setTitle(query.getTitle());
		try {
			queryDao.update(reloadedClone);
			fail();
		} catch(Exception e) {}
	}

	public void reset() {
		sessionFactory.getCurrentSession().flush();
		sessionFactory.getCurrentSession().clear();
	}
	
}
