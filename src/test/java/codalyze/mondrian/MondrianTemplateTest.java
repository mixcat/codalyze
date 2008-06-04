package codalyze.mondrian;

import mondrian.olap.Connection;
import mondrian.olap.Query;
import mondrian.olap.Result;

import static org.easymock.classextension.EasyMock.*;

import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;

public class MondrianTemplateTest {

	private MondrianTemplate template;
	private String connectString;
	private String queryString;
	private Connection connection;

	@Test
	public void shouldCreateNewMdxConnection() {
		Query query = createMock(Query.class);
		Result result = createMock(Result.class);
		expect(connection.parseQuery(queryString)).andReturn(query);
		expect(connection.execute(query)).andReturn(result);
		connection.close();
		replay(connection);
		assertEquals(result, template.execute(queryString));
	}
	
	@Before
	public void setUp() {
		queryString = "query";
		connectString = "connect";
		connection = createMock(Connection.class);
		template = new MondrianTemplate() {
			@Override
			protected Connection getMdxConnection() {
				return connection;
			};
		};
		template.setConnectString(connectString);
	}
}
