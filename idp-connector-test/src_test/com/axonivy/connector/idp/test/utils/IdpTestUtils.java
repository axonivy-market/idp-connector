package com.axonivy.connector.idp.test.utils;

import java.util.UUID;

import com.axonivy.connector.idp.test.constants.IdpTestConstants;

import ch.ivyteam.ivy.application.IApplication;
import ch.ivyteam.ivy.environment.AppFixture;
import ch.ivyteam.ivy.rest.client.RestClient;
import ch.ivyteam.ivy.rest.client.RestClients;
import ch.ivyteam.ivy.rest.client.RestClient.Builder;

public class IdpTestUtils {

	public static void setUpConfigForContext(String contextName, AppFixture fixture, IApplication app, String uuid) {
		switch (contextName) {
			case IdpTestConstants.REAL_CALL_CONTEXT_DISPLAY_NAME:
				setUpConfigForApiTest(fixture);
				break;
			case IdpTestConstants.MOCK_SERVER_CONTEXT_DISPLAY_NAME:
				setUpConfigForMockServer(fixture, app, uuid);
				break;
			default:
				break;
		}
	}

	private static void setUpConfigForApiTest(AppFixture fixture) {
		String apiKeySecret = System.getProperty(IdpTestConstants.API_KEY_SECRET);
		fixture.var("idpConnector.apiKeySecret", apiKeySecret);
	}

	private static void setUpConfigForMockServer(AppFixture fixture, IApplication app, String uuid) {
		fixture.var("idpConnector.apiProxyUrl", "TESTHOSTURL");
		fixture.var("idpConnector.apiKeySecret", "TESTKEY");
		fixture.var("idpConnector.waitFor", "120");
		RestClient restClient = RestClients.of(app).find(UUID.fromString(uuid));

		Builder builder = RestClient.create(restClient.name()).uuid(restClient.uniqueId())
				.uri("http://{ivy.engine.host}:{ivy.engine.http.port}/{ivy.request.application}/api/idpMock")
				.description(restClient.description()).properties(restClient.properties());
		restClient = builder.toRestClient();
		RestClients.of(app).set(restClient);
	}
}
