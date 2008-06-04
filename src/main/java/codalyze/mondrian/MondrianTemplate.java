package codalyze.mondrian;

import javax.servlet.ServletContext;

import mondrian.olap.Connection;
import mondrian.olap.DriverManager;
import mondrian.olap.Query;
import mondrian.olap.Result;
import mondrian.spi.CatalogLocator;
import mondrian.spi.impl.CatalogLocatorImpl;
import mondrian.spi.impl.ServletContextCatalogLocator;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Required;
import org.springframework.web.context.ServletContextAware;

import com.sun.org.apache.bcel.internal.generic.GETSTATIC;

public class MondrianTemplate implements ServletContextAware {

	Logger log = Logger.getLogger(MondrianTemplate.class);
	private String connectString;
	private ServletContext servletContext;

	public String getConnectString() {
		return connectString;
	}

	@Required
	public void setConnectString(String connectString) {
		this.connectString = connectString;
	}
	
	public Result execute(String queryString) {
		Connection mdxConnection = getMdxConnection();
		Query q = mdxConnection.parseQuery(queryString);
		Result result = mdxConnection.execute(q);
		mdxConnection.close();
		return result;
	}

	protected Connection getMdxConnection() {
		 CatalogLocatorImpl locator = new CatalogLocatorImpl();
		return DriverManager.getConnection(connectString,locator);
	}

	@Override
	public void setServletContext(ServletContext servletContext) {
		this.servletContext = servletContext;
	}

}