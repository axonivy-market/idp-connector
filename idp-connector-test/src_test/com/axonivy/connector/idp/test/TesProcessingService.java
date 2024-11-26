package com.axonivy.connector.idp.test;

import static org.assertj.core.api.Assertions.assertThat;

import java.io.IOException;
import java.time.LocalDate;
import java.util.UUID;

import org.junit.jupiter.api.Test;

import com.axonivy.connector.idp.connector.ProcessingServiceData;
import com.axonivy.connector.idp.connector.utils.IDPUtils;

import ch.ivyteam.ivy.application.IApplication;
import ch.ivyteam.ivy.bpm.engine.client.BpmClient;
import ch.ivyteam.ivy.bpm.engine.client.ExecutionResult;
import ch.ivyteam.ivy.bpm.engine.client.element.BpmElement;
import ch.ivyteam.ivy.bpm.engine.client.element.BpmProcess;
import ch.ivyteam.ivy.bpm.exec.client.IvyProcessTest;
import ch.ivyteam.ivy.environment.AppFixture;
import ch.ivyteam.ivy.security.ISession;

@IvyProcessTest(enableWebServer = true)
public class TesProcessingService extends TestIdpConnector {
	private static final BpmElement testeeProcessing = BpmProcess.path("ProcessingService").elementName("processing(String,File)");
	private static final BpmElement testeeRetrieveResult = BpmProcess.path("ProcessingService").elementName("retrieveResult(UUID)");
	private static final BpmElement testeeGetSubPDF = BpmProcess.path("ProcessingService").elementName("getSubPdf(UUID,Integer,String)");
	private static final BpmElement testeeShareToken = BpmProcess.path("ProcessingService").elementName("shareToken(UUID,String)");
	private static final BpmElement testeeRevokeShareToken = BpmProcess.path("ProcessingService").elementName("revokeToken(UUID)");
	
	@Test
	public void testProcessing(BpmClient bpmClient, ISession session, AppFixture fixture, IApplication app) throws IOException {
		ExecutionResult result = bpmClient.start().subProcess(testeeProcessing)
				.execute("invoice_extraction", getFile("file/invoice01.pdf"));
		
		ProcessingServiceData data = result.data().last();
		assertThat(data.getProcessingId()).isNotNull();
		assertThat(data.getError()).isNull();
	}
	
	@Test
	public void testDocumentSplittingProcessing(BpmClient bpmClient, ISession session, AppFixture fixture, IApplication app) throws IOException {
		ExecutionResult result = bpmClient.start().subProcess(testeeProcessing)
				.execute("generic_splitting", getFile("file/invoice01.pdf"));
		
		ProcessingServiceData data = result.data().last();
		assertThat(data.getProcessingId()).isNotNull();
		assertThat(data.getError()).isNull();
	}
	
	@Test
	public void testRetreiveResult(BpmClient bpmClient, ISession session, AppFixture fixture, IApplication app)
			throws IOException {
		ExecutionResult result = bpmClient.start().subProcess(testeeRetrieveResult)
				.withParam("processingId", UUID.fromString("11111111-1111-1111-1111-111111111111")).execute();
		ProcessingServiceData data = result.data().last();
		
		assertThat(data.getJsonNode()).isNotNull();
		assertThat(data.getJsonNode().has("extractions")).isTrue();
	}
	
	@Test
	public void testRetreiveDocumentSplittingResult(BpmClient bpmClient, ISession session, AppFixture fixture, IApplication app)
			throws IOException {
		ExecutionResult result = bpmClient.start().subProcess(testeeRetrieveResult)
				.withParam("processingId", UUID.fromString("22222222-2222-2222-2222-222222222222")).execute();
		ProcessingServiceData data = result.data().last();
		assertThat(data.getJsonNode()).isNotNull();
		assertThat(data.getJsonNode().has("document_splitting")).isTrue();
	}
	
	@Test
	public void testGetSubPDF(BpmClient bpmClient, ISession session, AppFixture fixture, IApplication app)
			throws IOException {
		ExecutionResult result = bpmClient.start().subProcess(testeeGetSubPDF)
				.withParam("processingId", UUID.fromString("22222222-2222-2222-2222-222222222222"))
				.withParam("index", 0)
				.withParam("fileName", "yourSubPDF").execute();
		ProcessingServiceData data = result.data().last();
		assertThat(data.getFile()).isNotNull();
		assertThat(data.getFile().getName()).isEqualToIgnoringCase("yourSubPDF.pdf");
	}
	
	@Test
	public void testShareToken(BpmClient bpmClient, ISession session, AppFixture fixture, IApplication app)
			throws IOException {
		ExecutionResult result = bpmClient.start().subProcess(testeeShareToken)
				.withParam("processing_id", UUID.fromString("11111111-1111-1111-1111-111111111111"))
				.withParam("expires_at", IDPUtils.formatDate(LocalDate.now().plusDays(2)))
				.execute();
		ProcessingServiceData data = result.data().last();
		assertThat(data.getDocShareTokenInfo()).isNotNull();
		assertThat(data.getDocShareTokenInfo().getToken()).isNotEmpty();
	}
	
	@Test
	public void testRevokeShareToken(BpmClient bpmClient, ISession session, AppFixture fixture, IApplication app) {
		ExecutionResult result = bpmClient.start().subProcess(testeeRevokeShareToken)
				.withParam("tokenUUID", UUID.fromString("01c90b84-243e-4d39-abf1-bda890fc5129"))
				.execute();
		ProcessingServiceData data = result.data().last();
		assertThat(data.getError()).isNull();
	}
	
}
