package codalyze.ui.web.controllers;

import static org.easymock.EasyMock.expect;
import static org.easymock.classextension.EasyMock.createMock;
import static org.easymock.classextension.EasyMock.replay;
import static org.easymock.classextension.EasyMock.verify;
import static org.junit.Assert.assertEquals;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.ws.rs.core.MultivaluedMap;

import org.junit.Before;
import org.junit.Test;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.validation.DataBinder;

import codalyze.core.entity.SQLQuery;

import com.sun.ws.rest.impl.MultivaluedMapImpl;

public class SQLQueryExecutionControllerTest {

	private SQLQueryExecutionController controller;
	private JdbcTemplate jdbcTemplate;
	private MultivaluedMap<String, String> form;
	private SQLQueryExecutionControllerValidator validator;
	private SQLQuery sqlQuery;

	@Test
	public void shouldBindAndValidate() {
		String query = "SELECT * FROM ALL";
		form.add("query", query );
		List<Map<String,Object>> result = new ArrayList<Map<String,Object>>();
		expect(jdbcTemplate.queryForList(query)).andReturn(result);
		replay(jdbcTemplate);
		assertEquals(result, controller.executeQuery(form));
		verify(jdbcTemplate);
	}
	
	@Before
	public void setUp() {
		sqlQuery = new SQLQuery();
		validator = createMock(SQLQueryExecutionControllerValidator.class);
		jdbcTemplate = createMock(JdbcTemplate.class);
		controller = new SQLQueryExecutionController() {
			@Override
			protected SQLQuery formBackingObject() {
				return sqlQuery;
			}
		};
		controller.setJdbcTemplate(jdbcTemplate);
		controller.setValidator(validator);
		form = new MultivaluedMapImpl();
	}
}
