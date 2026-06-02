# Axon Ivy IDP Connector

Axon Ivy IDP is a powerful Intelligent Document Processing solution that automates the extraction, classification, and analysis of unstructured data, transforming document-intensive processes into streamlined, efficient workflows. The connector integrates with Axon Ivy to provide document splitting, automated extraction, validation, and easy retrieval of processing results. Read our [documentation](idp-connector-product/README.md).

**Key features**

- Automate document extraction and classification directly from your Axon Ivy processes.
- Split documents into sub-documents for targeted processing and downstream workflows.
- Validate extraction confidence using configurable thresholds to improve data quality.
- Retrieve processing results and thumbnails programmatically for downstream consumption.
- Securely share processed documents via short-lived share tokens for collaboration.
- Integrate functionality via callable subprocesses to keep business logic in your processes.

## Demo

Check the demo implementations provided in the included demo module. The demos showcase the Document Splitting and Document Extraction workflows and provide hands-on examples you can run locally.

### Demo Workflows

#### IDP Demo (idp-connector-demo)

##### Document Splitting

1. Launch the Document Splitting demo from the demo menu or process list.
2. You'll see a file selection dialog; upload or select the document to split.

![Document Splitting Dialog](images/splitting-document-1.png)

3. Configure splitting options (page ranges) and confirm.

![Document Splitting Preview](images/splitting-document-2.png)

4. Review the split results and download the resulting files.

![Document Splitting Result](images/splitting-document-3.png)

##### Document Extraction

1. Launch the Document Extraction demo from the demo menu or process list.
2. Select or upload a sample document to extract structured data.

![Extraction Example](images/extraction1.png)

3. Review extracted data and confirm results.

![Extraction Results](images/extraction2.png)

4. Optionally, use the standalone UI to view extraction details and thumbnails.

![Extraction Thumbnail](images/extraction3.png)

## Setup

- **Roles:** Everybody (configured in config/roles.xml)
- **OpenAPI:** No information was delivered for this section.

### Variables

```yaml
@variables.yaml@
```

## Components

### Callable Subprocesses

#### ProcessingService.p.json

- **Signature**: processing(String workfowId, java.io.File file) -> processingId: java.util.UUID
    - Input:
        - `workfowId` (String)
        - `file` (java.io.File)
    - Result:
        - `processingId` (java.util.UUID)
        - `processingResult` (com.fasterxml.jackson.databind.JsonNode)
        - `error` (ch.ivyteam.ivy.bpm.error.BpmError)

- **Signature**: getSubPdf(java.util.UUID processingId, Integer index, String fileName) -> file: java.io.File
    - Input:
        - `processingId` (java.util.UUID)
        - `index` (Integer)
        - `fileName` (String)
    - Result:
        - `file` (java.io.File)
        - `error` (ch.ivyteam.ivy.bpm.error.BpmError)

- **Signature**: shareToken(java.util.UUID processing_id, String expires_at) -> docShareTokenInfo: com.axonivy.connector.idp.connector.model.DocShareTokenInfo
    - Input:
        - `processing_id` (java.util.UUID)
        - `expires_at` (String)
    - Result:
        - `docShareTokenInfo` (com.axonivy.connector.idp.connector.model.DocShareTokenInfo)
        - `error` (ch.ivyteam.ivy.bpm.error.BpmError)

- **Signature**: revokeToken(java.util.UUID tokenUUID) -> error: ch.ivyteam.ivy.bpm.error.BpmError
    - Input:
        - `tokenUUID` (java.util.UUID)
    - Result:
        - `error` (ch.ivyteam.ivy.bpm.error.BpmError)

- **Signature**: retrieveThumbnail(java.util.UUID processingId) -> thumbnail: java.io.File
    - Input:
        - `processingId` (java.util.UUID)
    - Result:
        - `thumbnail` (java.io.File)
        - `error` (ch.ivyteam.ivy.bpm.error.BpmError)

- **Signature**: retrieveResult(java.util.UUID processingId) -> resultsNode: com.fasterxml.jackson.databind.JsonNode
    - Input:
        - `processingId` (java.util.UUID)
    - Result:
        - `resultsNode` (com.fasterxml.jackson.databind.JsonNode)
        - `error` (ch.ivyteam.ivy.bpm.error.BpmError)

#### ValidationService.p.json

- **Signature**: validate(java.util.UUID processingId, Double confidenceMinValue) -> passed: Boolean
    - Input:
        - `processingId` (java.util.UUID)
        - `confidenceMinValue` (Double)
    - Result:
        - `passed` (Boolean)
    - Description: validate all values have confident >= confidentThreshold or NOT
### Dialog Components

#### IDPStandaloneUI — Reusable form component

- **Namespace:** com.axonivy.connector.idp.connector.IDPStandaloneUI
- **Component type:** Component dialog
- **Fields:**
   - `documentId` (java.util.UUID) — The document Id
   - `workflowType` (com.axonivy.connector.idp.connector.WorkflowType) — The document's workflow type: 'document-splitting' OR 'extraction'. Default is 'extraction'.
- **Purpose:** Error when getting the sharing url
- **UI attributes:**
   - `widgetVar` (required) — widgetvar. Use to save the form data: PF('{widgetvar}').save();
   - `style` (java.lang.String) (default: width:100%;height:85vh;border:0;) — style for the component itself
   - `styleClass` — styleClasses for the div surrounding the iframe
### Web Services

- No information was delivered for this section.
### Maven Artifacts

1. idp-connector

```xml
<dependency>
  <groupId>com.axonivy.connector.idp</groupId>
  <artifactId>idp-connector</artifactId>
  <type>iar</type>
</dependency>
```

2. idp-connector-demo

```xml
<dependency>
  <groupId>com.axonivy.connector.idp</groupId>
  <artifactId>idp-connector-demo</artifactId>
  <type>iar</type>
</dependency>
```
