package com.axonivy.connector.idp.test;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.UUID;

import org.junit.jupiter.api.Test;

import com.axonivy.connector.idp.connector.ValidationServiceData;

import ch.ivyteam.ivy.application.IApplication;
import ch.ivyteam.ivy.bpm.engine.client.BpmClient;
import ch.ivyteam.ivy.bpm.engine.client.ExecutionResult;
import ch.ivyteam.ivy.bpm.engine.client.element.BpmElement;
import ch.ivyteam.ivy.bpm.engine.client.element.BpmProcess;
import ch.ivyteam.ivy.bpm.exec.client.IvyProcessTest;
import ch.ivyteam.ivy.environment.AppFixture;
import ch.ivyteam.ivy.security.ISession;

@IvyProcessTest(enableWebServer = true)
public class TesValidationService extends TestIdpConnector {
	private static final BpmElement testeeValidation = BpmProcess.path("ValidationService").elementName("validate(UUID,Double)");
	
	@Test
	public void testValidation(BpmClient bpmClient, ISession session, AppFixture fixture, IApplication app) {
		ExecutionResult result = bpmClient.start().subProcess(testeeValidation)
				.withParam("processingId", UUID.fromString("11111111-1111-1111-1111-111111111111"))
				.withParam("confidenceMinValue", 0.3).execute();
		ValidationServiceData data = result.data().last();
		assertThat(data.getConfidencePassed()).isTrue();
	}

	@Test
	public void testValidationWithConfidenceOne(BpmClient bpmClient, ISession session, AppFixture fixture, IApplication app) {
		ExecutionResult result = bpmClient.start().subProcess(testeeValidation)
				.withParam("processingId", UUID.fromString("11111111-1111-1111-1111-111111111111"))
				.withParam("confidenceMinValue", 1.0).execute();
		ValidationServiceData data = result.data().last();
		assertThat(data.getConfidencePassed()).isFalse();
	}

	@Test
	public void testValidationForSplitting(BpmClient bpmClient, ISession session, AppFixture fixture, IApplication app) {
		ExecutionResult result = bpmClient.start().subProcess(testeeValidation)
				.withParam("processingId", UUID.fromString("22222222-2222-2222-2222-222222222222"))
				.withParam("confidenceMinValue", 0.5).execute();
		ValidationServiceData data = result.data().last();
		assertThat(data.getConfidencePassed()).isTrue();
		assertThat(data.getError()).isNull();
	}

	@Test
	public void testValidationForSplittingWithConnfidenceOne(BpmClient bpmClient, ISession session, AppFixture fixture, IApplication app) {
		ExecutionResult result = bpmClient.start().subProcess(testeeValidation)
				.withParam("processingId", UUID.fromString("22222222-2222-2222-2222-222222222222"))
				.withParam("confidenceMinValue", 1.0).execute();
		ValidationServiceData data = result.data().last();
		assertThat(data.getConfidencePassed()).isFalse();
	}
}
