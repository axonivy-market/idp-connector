# Axon Ivy IDP Connector

Axon Ivy IDP is an Intelligent Document Processing connector that automates document splitting, classification and data extraction. It reduces manual work in document-heavy processes (for example invoices or claims) by providing prebuilt workflows, a standalone UI component and built-in validation of results.

### Key features
- Seamless connector for Intelligent Document Processing: supports document splitting and data extraction with prebuilt workflows.
- Built-in validation of extraction results using configurable confidence thresholds to improve data quality.
- Embeddable standalone UI component with secure sharing links and token revocation for collaborative review.
- REST client integration with API-key authentication and multipart upload support for easy integration.
- Utilities to retrieve thumbnails, sub-PDFs and parsed extraction results for downstream processing.

## Demo

### Document Splitting

1. Start 'Document Splitting' from the demo start list.
   ![splitting-start](images/splitting-document-1.png)
2. Select a file and click 'Process' to run the document splitting service and preview detected split points.
   ![splitting-review](images/splitting-document-2.png)
3. Review the split result and download individual pages if required.
   ![splitting-result](images/splitting-document-3.png)
4. Optionally inspect the logs to verify the validation outcome for the split operation.
   ![validation-splitting](images/splitting-document-4.png)

### Extraction

1. Start 'Document Extraction' from the demo start list.
   ![extraction-start](images/extraction1.png)
2. Choose a document and click 'Process' to run extraction and preview parsed fields.
   ![extraction-review](images/extraction2.png)
3. View the detailed extraction results and download any artifacts you need.
   ![extraction-result](images/extraction3.png)
4. Optionally inspect the logs to verify the validation outcome for the extraction.
   ![validation-extraction](images/extraction4.png)

### Log Workflow names (utility)

- What it does: Fetches available workflows from the configured IDP instance and writes them to the log for troubleshooting or inspection.
- How to run: This utility is not shown in the public start list by default; run it from the Engine Cockpit or start it via the demo tooling when you need to inspect available workflows.

## Setup

Before the Axon Ivy Engine can interact with the IDP services, perform the following steps:

1. Obtain an Axon Ivy IDP API Key (contact support@axonivy.com). The API key is required to call the IDP REST services.
2. Configure the connector variables (API URL, API key) in the project variables as shown below.

```
@variables.yaml@
```

## Components

### Exposed CALLABLE_SUB processes

# Callable Sub Connector Starts

## ./idp-connector/processes/ProcessingService.p.json
- Signature: processing
   Input: workfowId: String, file: java.io.File
   Result: processingId: java.util.UUID, processingResult: com.fasterxml.jackson.databind.JsonNode, error: ch.ivyteam.ivy.bpm.error.BpmError
- Signature: getSubPdf
   Input: processingId: java.util.UUID, index: Integer, fileName: String
   Result: file: java.io.File, error: ch.ivyteam.ivy.bpm.error.BpmError
- Signature: shareToken
   Input: processing_id: java.util.UUID, expires_at: String
   Result: docShareTokenInfo: com.axonivy.connector.idp.connector.model.DocShareTokenInfo, error: ch.ivyteam.ivy.bpm.error.BpmError
- Signature: revokeToken
   Input: tokenUUID: java.util.UUID
   Result: error: ch.ivyteam.ivy.bpm.error.BpmError
- Signature: retrieveThumbnail
   Input: processingId: java.util.UUID
   Result: thumbnail: java.io.File, error: ch.ivyteam.ivy.bpm.error.BpmError
- Signature: retrieveResult
   Input: processingId: java.util.UUID
   Result: resultsNode: com.fasterxml.jackson.databind.JsonNode, error: ch.ivyteam.ivy.bpm.error.BpmError

## ./idp-connector/processes/ValidationService.p.json
- Signature: validate
   Input: processingId: java.util.UUID, confidenceMinValue: Double
   Result: passed: Boolean

### Form components

#### IDPStandaloneUI
- namespace: com.axonivy.connector.idp.connector.IDPStandaloneUI
- component type: HTML_DIALOG (process) + JSF composite component (cc:interface IvyComponent)
- start parameter: start(documentId: java.util.UUID, workflowType: com.axonivy.connector.idp.connector.WorkflowType) — workflowType defaults to WorkflowType.EXTRACTION
- parameter:
   - documentId: java.util.UUID — The document Id
   - workflowType: com.axonivy.connector.idp.connector.WorkflowType — The document's workflow type: 'document-splitting' OR 'extraction' (default: EXTRACTION)
- methods:
   - revokeShareToken(): revokes sharing token by calling ProcessingService:revokeToken(java.util.UUID) using `data.shareInfo.uuid`
- main feature/logic: Embeds the IDP editing UI in an iframe using a generated sharing URL (ProcessingService:shareToken). Exposes a PrimeFaces widget save API (`PF('{widgetVar}').save()`) that posts "save-document" to the iframe via the configured `apiProxyUrl`; provides token revocation on leave (remote command calling `revokeShareToken()`); displays a CMS error when no sharingUrl is available.

### Open API resources

- No public OpenAPI specs are available for this product

### Maven artifacts

1. idp-connector
    <dependency>
       <groupId>com.axonivy.connector.idp</groupId>
       <artifactId>idp-connector</artifactId>
       <type>iar</type>
    </dependency>

2. idp-connector-demo
    <dependency>
       <groupId>com.axonivy.connector.idp</groupId>
       <artifactId>idp-connector-demo</artifactId>
       <type>iar</type>
    </dependency>
