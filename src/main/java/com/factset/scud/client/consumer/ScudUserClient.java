package com.factset.scud.client.consumer;

import java.net.URI;

import com.sun.jersey.api.client.ClientHandlerException;
import com.sun.jersey.api.client.ClientResponse;

public class ScudUserClient {

	
	private void setUsersServiceHelper(ScudUsersServiceHelper usersServiceHelper) {
		usersServiceHlper = usersServiceHelper;
	}

	private ScudUsersServiceHelper usersServiceHlper;
	
	public void updatePassword(String username, String newPassword) {
		URI uri = usersServiceHlper.buildURI(username,"password");

		ClientResponse response = null;
		try {
			response = RestServiceConsumer.put(uri, "text/plain", newPassword);
		}
		catch (ClientHandlerException e) {
			System.out.println("Generic Error");
			e.printStackTrace();
		}

		if (!RestServiceConsumer.isOK(response)) {
			System.out.println("operation failed with http status " + response.getStatus() + " and message " + response.getEntity(String.class));
			if (RestServiceConsumer.hasCustomHeader(response, ScudUsersServiceHelper.ERROR_HEADER)) { 
				String error = RestServiceConsumer.getCustomHeader(response, ScudUsersServiceHelper.ERROR_HEADER);
				System.out.println("scud specific error code is: " + error);
			}
		}
		else {
			System.out.println("operation completed succesfully");
		}
	}
	
	public static void main(String[] args) {
		ScudUserClient client = new ScudUserClient();
		ScudUsersServiceHelper usersServiceHelper = new ScudUsersServiceHelper("http", "localhost", 8080, "/scud");
		client.setUsersServiceHelper(usersServiceHelper);
		client.updatePassword("mcaprari", "NewPassword:0");
	}
	
}
