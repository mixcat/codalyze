package com.factset.scud.client.consumer;

import java.net.URI;
import java.util.List;

import javax.ws.rs.core.MultivaluedMap;

import org.apache.log4j.Logger;

import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientFilter;
import com.sun.jersey.api.client.ClientHandlerException;
import com.sun.jersey.api.client.ClientRequest;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.UniformInterfaceException;
import com.sun.jersey.api.client.WebResource;

/**
 *	Generic consumer for a RESTful web service 
 *	@author mcaprari
 */
public class RestServiceConsumer {
	
	private static final Logger log = Logger.getLogger(RestServiceConsumer.class);
	
	protected static WebResource createResource(URI uri) {
		return Client.create().resource(uri);
	}
	
	/**
	 * UPDATE resource addressed by URI with value entity.
	 * 
	 * Can throw ClientHandlerException for network-related errors.
	 * 
	 * If you expect a respone value, use response.getEntity(...)
	 * 
	 * @param uri			resource address
	 * @param contentType	mime type of the entity being sent (ie text/plain)	
	 * @param entity		the entity to send
	 * @return
	 */
	static ClientResponse put(URI uri, String contentType, Object entity) {
		log.debug("PUT [" + uri.toString() + "]");
		ResultFilter resultFilter = new RestServiceConsumer().new ResultFilter();
		WebResource resource = createResource(uri);
		resource.addFilter(resultFilter);
		resource.accept("text/plain", "text/xml+foobar");
		try {
			resource.put(entity);
		}
		catch (UniformInterfaceException e) {
			log.debug("PUT [" + uri.toString() + "] EXCEPTION", e);
			return e.getResponse();
		}

		return resultFilter.getResponse();
	}
	
	public static boolean isOK(ClientResponse response) {
		return (200 <= response.getStatus() && response.getStatus() < 300);
	}

	public static boolean hasCustomHeader(ClientResponse response, String customHeader) {
		MultivaluedMap<String,String> metadata = response.getMetadata();
		List<String> list = metadata.get(customHeader);
		return (list != null && list.size() > 0);
	}

	public static String getCustomHeader(ClientResponse response, String customHeader) {
		MultivaluedMap<String,String> metadata = response.getMetadata();
		List<String> list = metadata.get(customHeader);
		if (list != null && list.size() > 0) {
			return list.get(0);
		}
		return null;
	}
	
	public class ResultFilter extends ClientFilter {

		protected ClientResponse response;
		
		@Override
		public ClientResponse handle(ClientRequest request) throws ClientHandlerException {
			response = getNext().handle(request);
			return response;
		}

		public ClientResponse getResponse() {
			return response;
		}
		
	}
}
