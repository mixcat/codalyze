package com.factset.scud.client.consumer;

import java.net.URI;
import java.net.URISyntaxException;

import javax.ws.rs.core.UriBuilder;

/**
 * Generic helper to ease consuming Scud REST users API, version 1.
 * 
 * This class is supposed to be used as a singleton.
 * 
 * Sample usage:
 * 
 * URI uri = new ScudUsersServiceHelper("http", "localhost", 8080, "/scud").buildURI("someuser", "password"); 
 * // uri is "http://localhost:8080/scud/api/v1/users/someuser/password"
 * 
 * @author mcaprari
 */
public class ScudUsersServiceHelper {

	public static final String ERROR_HEADER = "X-SCUD-ERROR-CODE";
	private static final String API_V1_BASE = "api/v1/users";
	private final URI baseUri;
	
	public URI buildURI(String...components) {
		UriBuilder uriBuilder = UriBuilder.fromUri(baseUri).path(API_V1_BASE);
		for (String path : components) {
			uriBuilder.path(path);
		}
		return uriBuilder.build();
	}
	
	public ScudUsersServiceHelper(String scheme, String host, int port, String path) {
		try {
			this.baseUri = new URI(scheme, null, host, port, path, null, null);
		} catch (URISyntaxException e) {
			throw new RuntimeException(e);
		}		
	}
	
}
