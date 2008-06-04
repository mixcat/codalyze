package codalyze.rest;

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

@Path("/query/{" + MDXQueryResource.PARAM_QUERY + "}")
@Singleton
public class MDXQueryResource {
	
	public static final String SCUD_CUSTOM_ERROR_HEADER = "X-SCUD-ERROR-CODE";
	public static final String ERROR_CODE_INVALID_PASSWORD_CODE = "scud-invalid-password";
	public static final String ERROR_CODE_INVALID_PASSWORD_MSG = "Password Refused";
	
	@Inject
	protected MondrianTemplate mondrianTemplate;
	
	
	Logger log = Logger.getLogger(MDXQueryResource.class);
	public static final String PARAM_QUERY = "query";

	@GET
	@ConsumeMime("text/plan")
	@ProduceMime("application/json")
	public Result query(@PathParam(PARAM_QUERY) String query) {
		
		Result result = mondrianTemplate.execute(query);
		return result; 
		/*
		if (false) {
			log.info("[FAILURE] Refusing new password for username: " );
			throw new WebApplicationException(new ResponseBuilderImpl()
				.status(500)
				.entity(ERROR_CODE_INVALID_PASSWORD_MSG)
				.header(SCUD_CUSTOM_ERROR_HEADER, ERROR_CODE_INVALID_PASSWORD_CODE)
				.build()
			);
		}
		
		if (!false) {
			log.warn("[FAILURE] Failed update password for username: " );
			throw new WebApplicationException(new ResponseBuilderImpl()
				.status(404)
				.entity("Password update failed")
				.build()
			);
		}
		log.info("[SUCCESS] updating password for username: ");
		*/
	}
	
}
