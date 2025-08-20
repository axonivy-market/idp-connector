package com.axonivy.connector.idp.test;

import static com.axonivy.utils.e2etest.enums.E2EEnvironment.REAL_SERVER;

import java.util.UUID;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.ExtensionContext;

import com.axonivy.connector.idp.test.constants.IdpTestConstants;
import com.axonivy.utils.e2etest.utils.E2ETestUtils;

import ch.ivyteam.ivy.application.IApplication;
import ch.ivyteam.ivy.environment.AppFixture;
import ch.ivyteam.ivy.rest.client.RestClient;
import ch.ivyteam.ivy.rest.client.RestClients;
import ch.ivyteam.ivy.rest.client.RestClient.Builder;

public abstract class BaseSetup {
	public abstract String getUuid();

	protected boolean isRealTest;

	@BeforeEach
	void beforeEach(ExtensionContext context, AppFixture fixture, IApplication app) {
		isRealTest = context.getDisplayName().equals(REAL_SERVER.getDisplayName());
		E2ETestUtils.determineConfigForContext(context.getDisplayName(), runRealEnv(fixture), runMockEnv(fixture, app));
	}

	private Runnable runRealEnv(AppFixture fixture) {
		return () -> {
			String apiKeySecret = System.getProperty(IdpTestConstants.API_KEY_SECRET);
			fixture.var("idpConnector.apiKeySecret", apiKeySecret);
		};
	}

	private Runnable runMockEnv(AppFixture fixture, IApplication app) {
		return () -> {
			fixture.var("idpConnector.apiProxyUrl", "TESTHOSTURL");
			fixture.var("idpConnector.apiKeySecret", "TESTKEY");
			fixture.var("idpConnector.waitFor", "120");
			RestClient restClient = RestClients.of(app).find(UUID.fromString(getUuid()));

			Builder builder = RestClient.create(restClient.name()).uuid(restClient.uniqueId())
					.uri("http://{ivy.engine.host}:{ivy.engine.http.port}/{ivy.request.application}/api/idpMock")
					.description(restClient.description()).properties(restClient.properties());
			restClient = builder.toRestClient();
			RestClients.of(app).set(restClient);
		};
	}
}
