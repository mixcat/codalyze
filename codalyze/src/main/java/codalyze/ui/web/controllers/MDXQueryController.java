package codalyze.ui.web.controllers;

import javax.ws.rs.ConsumeMime;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.ProduceMime;

import mondrian.olap.Result;

import org.apache.log4j.Logger;

import codalyze.mondrian.MondrianTemplate;

import com.sun.jersey.spi.resource.Inject;
import com.sun.jersey.spi.resource.Singleton;

@Path("/query/mdx/{" + MDXQueryController.PARAM_QUERY + "}")
@Singleton
public class MDXQueryController {
	
	@Inject
	public MondrianTemplate mondrianTemplate;
	
	Logger log = Logger.getLogger(MDXQueryController.class);
	public static final String PARAM_QUERY = "query";

	@GET
	@ConsumeMime("text/plan")
	@ProduceMime("application/json")
	public Result query(@PathParam(PARAM_QUERY) String query) {
		Result result = mondrianTemplate.execute(query);
		return result; 
	}
	
}
