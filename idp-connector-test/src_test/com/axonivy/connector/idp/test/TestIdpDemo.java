package com.axonivy.connector.idp.test;

import static org.assertj.core.api.Assertions.assertThat;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.TestTemplate;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.api.extension.ExtensionContext;

import com.axonivy.connector.idp.test.context.MultiEnvironmentContextProvider;
import com.axonivy.connector.idp.test.utils.IdpTestUtils;

import ch.ivyteam.ivy.application.IApplication;
import ch.ivyteam.ivy.bpm.engine.client.BpmClient;
import ch.ivyteam.ivy.bpm.engine.client.ExecutionResult;
import ch.ivyteam.ivy.bpm.exec.client.IvyProcessTest;
import ch.ivyteam.ivy.environment.AppFixture;
import ch.ivyteam.ivy.security.ISession;

@IvyProcessTest(enableWebServer = true)
@ExtendWith(MultiEnvironmentContextProvider.class)
public class TestIdpDemo {
	private static final String REST_UUID = "c316f4d1-daa6-4ca2-b3e0-68133e54eb99";

	@BeforeEach
	void beforeEach(ExtensionContext context, AppFixture fixture, IApplication app) {
		IdpTestUtils.setUpConfigForContext(context.getDisplayName(), fixture, app, REST_UUID);
	}

	@TestTemplate
	public void testOrganizations(BpmClient bpmClient, ISession session, AppFixture fixture, IApplication app) {
		ExecutionResult result = bpmClient.start().process("IDPDemo/workflows.ivp").execute();
		com.axonivy.connector.idp.connector.demo.Data data = result.data().last();
		assertThat(data.getWorkflows()).isNotEmpty();
	}
}
