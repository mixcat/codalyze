package codalyze.ui.web.controllers;

import java.util.List;
import java.util.Map;

import javax.ws.rs.ConsumeMime;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.ProduceMime;
import javax.ws.rs.core.MultivaluedMap;

import org.springframework.jdbc.core.JdbcTemplate;

import codalyze.core.entity.SQLQuery;

import com.sun.jersey.spi.resource.Inject;
import com.sun.jersey.spi.resource.Singleton;


@Path("/query/execute/sql/")
@Singleton
public class SQLQueryExecutionController extends FormController {
	
	@Inject
	private JdbcTemplate jdbcTemplate;
	
	public SQLQueryExecutionController() {
		setCommandClass(SQLQuery.class);
	}
	
	public void setJdbcTemplate(JdbcTemplate jdbcTemplate) {
		this.jdbcTemplate = jdbcTemplate;
	}
	
	@POST
	@ProduceMime("text/javascript")
	@ConsumeMime("application/x-www-form-urlencoded")
	public List<Map<String,Object>> executeQuery(MultivaluedMap<String, String> form) {
		SQLQuery sqlQuery = (SQLQuery) formBackingObject();
		bindAndValidate(sqlQuery, form);
		return jdbcTemplate.queryForList(sqlQuery.getQuery());
	}

}
