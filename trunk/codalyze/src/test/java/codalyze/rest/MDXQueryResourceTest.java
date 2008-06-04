package codalyze.rest;

import static org.junit.Assert.*;

import static org.easymock.classextension.EasyMock.*;

import org.junit.Before;
import org.junit.Test;

import codalyze.mondrian.MondrianTemplate;
import codalyze.rest.MDXQueryResource;


public class MDXQueryResourceTest {

	private MDXQueryResource resource;
	private MondrianTemplate template;

	@Test
	public void testInvokesMondrianTemplate() {
		String queryString = "query";
		expect(template.execute(queryString)).andReturn(null);
		replay(template);
		resource.query(queryString);
		verify(template);
	}
	
	@Before
	public void setUp() {
		resource = new MDXQueryResource();
		template = createMock(MondrianTemplate.class);
		resource.mondrianTemplate = template;
	}
	
}
