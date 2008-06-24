package codalyze.ui.web.controllers;

import static org.easymock.classextension.EasyMock.*;
import org.junit.Before;
import org.junit.Test;

import com.sun.ws.rest.impl.MultivaluedMapImpl;

import codalyze.core.dao.SQLQueryBuilder;
import codalyze.core.dao.SQLQueryDao;
import codalyze.core.entity.SQLQuery;

import java.util.List;

import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.MultivaluedMap;

import static org.junit.Assert.*;

public class SQLQueryControllerTest {

	private SQLQueryController controller;
	private SQLQueryDao dao;
	private SQLQuery formBackingObject;

	@Test
	public void testGetQueryByTitle() {
		String title = "title";
		SQLQuery query = new SQLQueryBuilder(title, "query").id(1L).toQuery();
		expect(dao.findByTitle(title)).andReturn(query);
		replay(dao);
		assertEquals(query, controller.getQuery(title));
		verify(dao);
	}
	
	@Test
	public void shouldThrowWebApplicationExceptionIfQueryNotFound() {
		String title = "title";
		expect(dao.findByTitle(title)).andReturn(null);
		replay(dao);
		try {
			controller.getQuery(title);
			fail();
		} catch(WebApplicationException e) {}
	}
	
	@Test
	public void shouldBindFormAndSaveNewQuery() {
		MultivaluedMap<String, String> form = new MultivaluedMapImpl();
		form.add("title", "title");
		form.add("query", "query");
		formBackingObject.setId(1L);
		dao.save(formBackingObject);
		replay(dao);
		SQLQuery createQuery = controller.createQuery(form);
		assertSame(createQuery, formBackingObject);
		assertEquals("title", createQuery.getTitle());
		assertEquals("query", createQuery.getQuery());
	}
	
	@Test
	public void shouldUpdateExistingQuery() {
		MultivaluedMap<String, String> form = new MultivaluedMapImpl();
		form.add("id", "1");
		form.add("title", "newTitle");
		form.add("query", "newQuery");
		dao.update(formBackingObject);
		replay(dao);
		SQLQuery updateQuery = controller.updateQuery("title", form);
		assertSame(updateQuery, formBackingObject);
		assertEquals("newTitle", updateQuery.getTitle());
		assertEquals("newQuery", updateQuery.getQuery());
	}
	
	@Test
	public void shouldLoadAllQueries() {
		List<SQLQuery> list = createMock(List.class);
		expect(dao.findAll()).andReturn(list);
		replay(dao);
		assertEquals(list, controller.getQueries());
		verify(dao);
	}
	
	@Before
	public void setUp() {
		formBackingObject = new SQLQuery();
		dao = createMock(SQLQueryDao.class);
		controller = new SQLQueryController() {
			@Override
			protected Object formBackingObject() {
				return formBackingObject;
			}
		};
		controller.setSQLQueryDao(dao);
	}
}
