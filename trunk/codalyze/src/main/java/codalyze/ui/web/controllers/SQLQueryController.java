package codalyze.ui.web.controllers;

import codalyze.core.dao.SQLQueryDao;
import codalyze.core.entity.SQLQuery;
import codalyze.ui.web.validators.CreateQueryValidator;

import java.util.List;

import javax.ws.rs.ConsumeMime;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.ProduceMime;
import javax.ws.rs.core.MultivaluedMap;

import org.apache.log4j.Logger;

import com.sun.jersey.spi.resource.Inject;
import com.sun.jersey.spi.resource.Singleton;


@Path("/query/sql/")
@Singleton
public class SQLQueryController extends FormController {

	private final static Logger log = Logger.getLogger(SQLQueryController.class); 

	@Inject
	private SQLQueryDao dao;
	
	public void setSQLQueryDao(SQLQueryDao dao) {
		this.dao = dao;
	}

	public SQLQueryController() {
		setCommandClass(SQLQuery.class);
		setValidator(new CreateQueryValidator());
	}
	
	@GET
	@ProduceMime("text/javascript")
	public List<SQLQuery> getQueries() {
		return dao.findAll();
	}
	
	@GET
	@Path("{title}")
	@ProduceMime("text/javascript")
	public SQLQuery getQuery(@PathParam("title") String title) {
		log.info("loading query by title [" + title + "]");
		 SQLQuery query = dao.findByTitle(title);
		 if (query == null) {
			 throw getNotFoundException(title);
		 }
		 return query;
	}
	
	@PUT
	@Path("{title}")
	@ProduceMime("text/javascript")
	@ConsumeMime("application/x-www-form-urlencoded")
	public SQLQuery updateQuery(@PathParam("title") String title, final MultivaluedMap<String, String> form) {
		SQLQuery queryObject  = (SQLQuery) formBackingObject();
		bindAndValidate(queryObject, form);
		dao.update(queryObject);
		return queryObject;	
	}
	
	@POST
	@ProduceMime("text/javascript")
	@ConsumeMime("application/x-www-form-urlencoded")
	public SQLQuery createQuery( final MultivaluedMap<String, String> form) {
		SQLQuery queryObject  = (SQLQuery) formBackingObject();
		bindAndValidate(queryObject, form);
		dao.save(queryObject);
		return queryObject;
	}
	
}
