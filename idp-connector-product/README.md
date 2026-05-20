# Axon Ivy IDP Connector

Axon Ivy IDP is a powerful Intelligent Document Processing solution that automates the extraction, classification, and analysis of unstructured data, transforming document-intensive processes into streamlined, efficient workflows.

Read our [documentation](idp-connector-product/README.md).

## Key features

- Automate document extraction and classification to reduce manual effort.
- Identify and split documents reliably to streamline downstream processing.
- Integrate processing results into Axon Ivy processes with ready-to-use callable subs.
- Review and validate extraction results through demo dialogs for quality control.
- Share processing results and thumbnails via secure, token-based links.
- Scalable REST API integration for high-throughput batch and interactive use.

## Demo

Check the demo implementations provided. Visit the demo module to try sample workflows.

### Demo workflows

#### idp-connector-demo (idp-connector-demo)

##### Document Splitting

1. Launch the "Document Splitting" demo from the demo menu.
2. Select a file to be split in the file selection dialog.
3. Review and confirm the suggested split points in the split review dialog.
4. Receive the split documents and optionally download or preview the results.

	![Document Splitting Step 1](images/splitting-document-1.png)

	![Document Splitting Step 2](images/splitting-document-2.png)

	![Document Splitting Step 3](images/splitting-document-3.png)

	![Document Splitting Step 4](images/splitting-document-4.png)

##### Document Extraction

1. Launch the "Document Extraction" demo from the demo menu.
2. Use the file selection dialog to choose a sample document.
3. The extraction result dialog shows detected fields and extracted data.
4. Review the extracted data and download results if desired.

	![Document Extraction Step 1](images/extraction1.png)

	![Document Extraction Step 2](images/extraction2.png)

	![Document Extraction Step 3](images/extraction3.png)

	![Document Extraction Step 4](images/extraction4.png)

## Setup

- **Roles:** Everybody (configured in config/roles.xml)
- **OpenAPI:** `${ivy.var.idpConnector.apiProxyUrl}/api` — Namespace: `com.axonivy.connector.idp.connector.model`

### Variables

Variables:
  idpConnector:
	# The proxy server URL
	apiProxyUrl: https://idp.api.axonivy.com
	# API Key for "IDP Document Capturing API"
	#[password]
	apiKeySecret: ${decrypt:}
	# Number of seconds to wait for the completion of document processing.
	# If processing doesn't finish in this time, a URL for retrieving the results will be returned.
	waitFor: "120"
	# The default value for validation processing result's confidence values  
	confidenceMinValue: "0.8"

- No information was detected for this section.

## Components

### Connector processes

#### ProcessingService.p.json

- **processing(String workfowId, java.io.File file) -> processingId: java.util.UUID, processingResult: com.fasterxml.jackson.databind.JsonNode, error: ch.ivyteam.ivy.bpm.error.BpmError**
	- Input:
		- `workfowId` (String) - 
		- `file` (java.io.File) - 
	- Result:
		- `processingId` (java.util.UUID) - 
		- `processingResult` (com.fasterxml.jackson.databind.JsonNode) - 
		- `error` (ch.ivyteam.ivy.bpm.error.BpmError) - 

- **getSubPdf(java.util.UUID processingId, Integer index, String fileName) -> file: java.io.File, error: ch.ivyteam.ivy.bpm.error.BpmError**
	- Input:
		- `processingId` (java.util.UUID) - 
		- `index` (Integer) - 
		- `fileName` (String) - 
	- Result:
		- `file` (java.io.File) - 
		- `error` (ch.ivyteam.ivy.bpm.error.BpmError) - 

- **shareToken(java.util.UUID processing_id, String expires_at) -> docShareTokenInfo: com.axonivy.connector.idp.connector.model.DocShareTokenInfo, error: ch.ivyteam.ivy.bpm.error.BpmError**
	- Input:
		- `processing_id` (java.util.UUID) - 
		- `expires_at` (String) - 
	- Result:
		- `docShareTokenInfo` (com.axonivy.connector.idp.connector.model.DocShareTokenInfo) - 
		- `error` (ch.ivyteam.ivy.bpm.error.BpmError) - 

- **revokeToken(java.util.UUID tokenUUID) -> error: ch.ivyteam.ivy.bpm.error.BpmError**
	- Input:
		- `tokenUUID` (java.util.UUID) - 
	- Result:
		- `error` (ch.ivyteam.ivy.bpm.error.BpmError) - 

- **retrieveThumbnail(java.util.UUID processingId) -> thumbnail: java.io.File, error: ch.ivyteam.ivy.bpm.error.BpmError**
	- Input:
		- `processingId` (java.util.UUID) - 
	- Result:
		- `thumbnail` (java.io.File) - 
		- `error` (ch.ivyteam.ivy.bpm.error.BpmError) - 

- **retrieveResult(java.util.UUID processingId) -> resultsNode: com.fasterxml.jackson.databind.JsonNode, error: ch.ivyteam.ivy.bpm.error.BpmError**
	- Input:
		- `processingId` (java.util.UUID) - 
	- Result:
		- `resultsNode` (com.fasterxml.jackson.databind.JsonNode) - 
		- `error` (ch.ivyteam.ivy.bpm.error.BpmError) - 

#### ValidationService.p.json

- **validate(java.util.UUID processingId, Double confidenceMinValue) -> passed: Boolean**
	- Input:
		- `processingId` (java.util.UUID) - 
		- `confidenceMinValue` (Double) - 
	- Result:
		- `passed` (Boolean) - 

### Form components

#### IDPStandaloneUIData — Standalone UI data for the IDP dialog
- **Namespace:** com.axonivy.connector.idp.connector.IDPStandaloneUI
- **Component type:** Data Class
- **Fields:**
   - `workflowType` (com.axonivy.connector.idp.connector.WorkflowType) — the document's workflow type: document-spliting OR extraction
   - `documentId` (java.util.UUID) — 
   - `shareInfo` (com.axonivy.connector.idp.connector.model.DocShareTokenInfo) — 
   - `sharingUrl` (String) — 
   - `apiProxyUrl` (String) — 

### Maven artifacts

1. idp-connector-demo

```xml
<dependency>
  <groupId>com.axonivy.connector.idp</groupId>
  <artifactId>idp-connector-demo</artifactId>
  <version>@version@</version>
  <type>iar</type>
</dependency>
```

2. idp-connector

```xml
<dependency>
  <groupId>com.axonivy.connector.idp</groupId>
  <artifactId>idp-connector</artifactId>
  <version>@version@</version>
  <type>iar</type>
</dependency>
```

