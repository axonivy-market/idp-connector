package com.axonivy.connector.idp.test;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.UUID;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.TestTemplate;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.api.extension.ExtensionContext;

import com.axonivy.connector.idp.connector.ProcessingServiceData;
import com.axonivy.connector.idp.connector.ValidationServiceData;
import com.axonivy.connector.idp.test.constants.IdpConstant;
import com.axonivy.connector.idp.test.context.MultiEnvironmentContextProvider;
import com.axonivy.connector.idp.test.utils.IdpUtils;

import ch.ivyteam.ivy.application.IApplication;
import ch.ivyteam.ivy.bpm.engine.client.BpmClient;
import ch.ivyteam.ivy.bpm.engine.client.ExecutionResult;
import ch.ivyteam.ivy.bpm.engine.client.element.BpmElement;
import ch.ivyteam.ivy.bpm.engine.client.element.BpmProcess;
import ch.ivyteam.ivy.bpm.exec.client.IvyProcessTest;
import ch.ivyteam.ivy.environment.AppFixture;
import ch.ivyteam.ivy.rest.client.RestClients;

@IvyProcessTest(enableWebServer = true)
@ExtendWith(MultiEnvironmentContextProvider.class)
public class TesValidationService {
	private static final BpmElement testeeValidation =
			BpmProcess.path("ValidationService").elementName("validate(UUID,Double)");
	private static final String REST_UUID = "c316f4d1-daa6-4ca2-b3e0-68133e54eb99";
	private static final BpmElement RETRIEVE_RESULT_ERROR = BpmElement.pid("191351D442CAD7E7-f12");

	@BeforeEach
	void beforeEach(ExtensionContext context, AppFixture fixture, IApplication app) {
		IdpUtils.setUpConfigForContext(context.getDisplayName(), fixture, app, REST_UUID);
	}

	@AfterEach
	void afterEach(AppFixture fixture, IApplication app) {
		RestClients clients = RestClients.of(app);
		clients.remove("IDP (Document Capturing API)");
	}

	@TestTemplate
	public void testValidation(BpmClient bpmClient, ExtensionContext context) throws NoSuchFieldException {
		ExecutionResult result = bpmClient.start().subProcess(testeeValidation)
				.withParam("processingId", UUID.fromString("11111111-1111-1111-1111-111111111111"))
				.withParam("confidenceMinValue", 0.3).execute();
		if (context.getDisplayName().equals(IdpConstant.REAL_CALL_CONTEXT_DISPLAY_NAME)) {
			ProcessingServiceData processingServiceData =
					(ProcessingServiceData) result.data().lastOnElement(RETRIEVE_RESULT_ERROR);
			assertThat(processingServiceData.getError().getAttribute("RestClientResponseStatusCode")).isEqualTo(404);
		} else {
			ValidationServiceData data = result.data().last();
			assertThat(data.getConfidencePassed()).isTrue();
		}
	}

	@TestTemplate
	public void testValidationWithConfidenceOne(BpmClient bpmClient, ExtensionContext context) {
		ExecutionResult result = bpmClient.start().subProcess(testeeValidation)
				.withParam("processingId", UUID.fromString("11111111-1111-1111-1111-111111111111"))
				.withParam("confidenceMinValue", 1.0).execute();
		if (context.getDisplayName().equals(IdpConstant.REAL_CALL_CONTEXT_DISPLAY_NAME)) {
			ProcessingServiceData processingServiceData =
					(ProcessingServiceData) result.data().lastOnElement(RETRIEVE_RESULT_ERROR);
			assertThat(processingServiceData.getError().getAttribute("RestClientResponseStatusCode")).isEqualTo(404);
		} else {
			ValidationServiceData data = result.data().last();
			assertThat(data.getConfidencePassed()).isFalse();
		}
	}

	@TestTemplate
	public void testValidationForSplitting(BpmClient bpmClient, ExtensionContext context) {
		ExecutionResult result = bpmClient.start().subProcess(testeeValidation)
				.withParam("processingId", UUID.fromString("22222222-2222-2222-2222-222222222222"))
				.withParam("confidenceMinValue", 0.5).execute();
		if (context.getDisplayName().equals(IdpConstant.REAL_CALL_CONTEXT_DISPLAY_NAME)) {
			ProcessingServiceData processingServiceData =
					(ProcessingServiceData) result.data().lastOnElement(RETRIEVE_RESULT_ERROR);
			assertThat(processingServiceData.getError().getAttribute("RestClientResponseStatusCode")).isEqualTo(404);
		} else {
			ValidationServiceData data = result.data().last();
			assertThat(data.getConfidencePassed()).isTrue();
			assertThat(data.getError()).isNull();
		}
	}

	@TestTemplate
	public void testValidationForSplittingWithConnfidenceOne(BpmClient bpmClient, ExtensionContext context) {
		ExecutionResult result = bpmClient.start().subProcess(testeeValidation)
				.withParam("processingId", UUID.fromString("22222222-2222-2222-2222-222222222222"))
				.withParam("confidenceMinValue", 1.0).execute();
		if (context.getDisplayName().equals(IdpConstant.REAL_CALL_CONTEXT_DISPLAY_NAME)) {
			ProcessingServiceData processingServiceData =
					(ProcessingServiceData) result.data().lastOnElement(RETRIEVE_RESULT_ERROR);
			assertThat(processingServiceData.getError().getAttribute("RestClientResponseStatusCode")).isEqualTo(404);
		} else {
			ValidationServiceData data = result.data().last();
			assertThat(data.getConfidencePassed()).isFalse();
		}
	}
}
