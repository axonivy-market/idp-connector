# Axon Ivy IDP Connector

Axon Ivy IDP is an Intelligent Document Processing solution that automates extraction, classification and analysis of unstructured documents. It helps reduce manual effort in document-heavy processes (invoices, claims, onboarding) by providing reliable splitting and data extraction workflows.

### Key features
- Automates document splitting and data extraction to reduce manual work.
- Ready-to-use workflows for common scenarios (invoice extraction, generic splitting).
- Built-in validation highlights low-confidence extraction results to reduce errors.
- Embeddable standalone UI so users can preview, save and share documents.
- Secure access with API-key authentication and a configurable REST client.
- Flexible output formats: PDF, thumbnails, OCR text, page images and sub-PDFs.

## Demo

### Document Splitting

1. Start 'Document Splitting' from the demo start list.
   ![splitting-start](images/splitting-document-1.png)
2. Choose a document in the file-selection dialog and click "Process" to send the file to the IDP service.
   ![splitting-review](images/splitting-document-2.png)
3. Review the detected split points and click "Process" to finalize; download the individual pages if needed.
   ![splitting-result](images/splitting-document-3.png)
4. Check the engine log to see validation results and confidence warnings.
   ![validation-splitting](images/splitting-document-4.png)

### Extraction

1. Start 'Document Extraction' from the demo start list.
   ![extraction-start](images/extraction1.png)
2. Select a document and click "Process" to run the extraction workflow.
   ![extraction-review](images/extraction2.png)
3. Open the extraction result to review detected fields and export or copy results.
   ![extraction-result](images/extraction3.png)
4. Check the engine log to see validation results and confidence warnings.
   ![validation-extraction](images/extraction4.png)

### Log Workflow names

1. The demo includes a small helper called "Log Workflow names" which fetches available workflows from the configured IDP instance and writes them to the engine log.
2. This helper is intended for administrators and may be hidden from the public start list; enable or run it from the engine to inspect available workflows.

## Setup

Before any interaction between the Axon Ivy Engine and the IDP services can take place, do the following:

1. Obtain a working Axon Ivy IDP `API Key` (contact support@axonivy.com). This key is required to call the REST API.
2. Override the demo project's global variable for `apiKeySecret` as shown in the example below.

```
@variables.yaml@
```

## Components

### Exposed CALLABLE_SUB processes

#### idp-connector/processes/ProcessingService.p.json
- Signature: `processing(String,File)`
- Input:
  - `workfowId` : `String`
  - `file` : `java.io.File`
- Result:
  - `processingId` : `java.util.UUID`
  - `processingResult` : `com.fasterxml.jackson.databind.JsonNode`
  - `error` : `ch.ivyteam.ivy.bpm.error.BpmError`

- Signature: `getSubPdf(UUID,Integer,String)`
- Input:
  - `processingId` : `java.util.UUID`
  - `index` : `Integer`
  - `fileName` : `String`
- Result:
  - `file` : `java.io.File`
  - `error` : `ch.ivyteam.ivy.bpm.error.BpmError`

- Signature: `shareToken(UUID,String)`
- Input:
  - `processing_id` : `java.util.UUID`
  - `expires_at` : `String`
- Result:
  - `docShareTokenInfo` : `com.axonivy.connector.idp.connector.model.DocShareTokenInfo`
  - `error` : `ch.ivyteam.ivy.bpm.error.BpmError`

- Signature: `revokeToken(UUID)`
- Input:
  - `tokenUUID` : `java.util.UUID`
- Result:
  - `error` : `ch.ivyteam.ivy.bpm.error.BpmError`

- Signature: `retrieveThumbnail(UUID)`
- Input:
  - `processingId` : `java.util.UUID`
- Result:
  - `thumbnail` : `java.io.File`
  - `error` : `ch.ivyteam.ivy.bpm.error.BpmError`

- Signature: `retrieveResult(UUID)`
- Input:
  - `processingId` : `java.util.UUID`
- Result:
  - `resultsNode` : `com.fasterxml.jackson.databind.JsonNode`
  - `error` : `ch.ivyteam.ivy.bpm.error.BpmError`

#### idp-connector/processes/ValidationService.p.json
- Signature: `validate(UUID,Double)`
- Input:
  - `processingId` : `java.util.UUID`
  - `confidenceMinValue` : `Double`
- Result:
  - `passed` : `Boolean`

### Form components

UI dialog name: IDPStandaloneUI
- namespace: com.axonivy.connector.idp.connector.IDPStandaloneUI.IDPStandaloneUIData
- start parameter: `start(UUID,WorkflowType)`
- main feature/logic: Embeds the provider's standalone UI in an iframe so users can preview, save and share documents using a temporary link.

### Maven artifacts

1. idp-connector-demo
```xml
<dependency>
  <groupId>com.axonivy.connector.idp</groupId>
  <artifactId>idp-connector-demo</artifactId>
  <type>iar</type>
</dependency>
```

2. idp-connector
```xml
<dependency>
  <groupId>com.axonivy.connector.idp</groupId>
  <artifactId>idp-connector</artifactId>
  <type>iar</type>
</dependency>
```
