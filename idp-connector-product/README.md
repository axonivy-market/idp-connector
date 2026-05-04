# Axon Ivy IDP Connector

Axon Ivy IDP is an Intelligent Document Processing solution that automates the extraction, classification, and analysis of unstructured data. It streamlines document-intensive processes such as invoice management, claims processing, and customer onboarding using AI-powered OCR, handwriting text recognition (HTR), and machine learning algorithms to increase the accuracy and efficiency in data management. This connector:

* gives you access to the Axon Ivy's capabilities: pre processing, classification and data extraction.
* has a built-in validation of processing results.
* minimizes your integration effort with a standalone UI component.
* is based on REST web service, Axon Ivy UI Component and SubCallable process technologies.

### Key features

- Pre-built workflows for document splitting and data extraction — ready to run out-of-the-box.
- Standalone UI component to embed IDP workflows into Axon Ivy pages with minimal integration effort.
- Secure REST integration with API key authentication and shareable result tokens for easy collaboration.
- Built-in validation with configurable confidence thresholds to reduce false positives.
- Utilities for thumbnails, sub-PDF retrieval and exporting results for downstream systems.

## Demo

### Document Splitting

1. Start 'DocumentSplitting'
   ![splitting-start](images/splitting-document-1.png)
2. Click 'Process' to review the result of the document splitting service
   ![splitting-review](images/splitting-document-2.png)
3. Click 'Process' to view the result of document splitting and to download the individual pages
   ![splitting-result](images/splitting-document-3.png)
4. Check the last log to see the result of the validation function
   ![validation-splitting](images/splitting-document-4.png)

### Extraction

1. Start 'Extraction'
   ![extraction-start](images/extraction1.png)
2. Select a document from the list, then click 'Process' to review the result of the extraction
   ![extraction-review](images/extraction2.png)
3. Click 'Process' to view the extraction result
   ![extraction-result](images/extraction3.png)
4. Check the last log to see the result of the validation function
   ![validation-extraction](images/extraction4.png)

## Setup

Before any interaction between the Axon Ivy Engine and the IDP services can take place, the following steps must be followed:

1. Get a working Axon Ivy IDP `API Key` by contacting support@axonivy.com - this is required to call the REST API services.
2. Override the global variable for `apiKeySecret` in the demo project as shown in the example below.

```
@variables.yaml@
```

## Components

### Callables

- `ProcessingService.processing(String, java.io.File)` — Starts document processing and returns a `processingId` (UUID) and the processing result (JSON).
- `ProcessingService.retrieveResult(java.util.UUID)` — Retrieves processing results (JSON) for a given processing id.
- `ProcessingService.getSubPdf(java.util.UUID, Integer, String)` — Downloads a sub-PDF (split page) as a file.
- `ProcessingService.retrieveThumbnail(java.util.UUID)` — Retrieves a preview thumbnail image for a processed document.
- `ProcessingService.shareToken(java.util.UUID, String)` — Generates a temporary share token to view a processed document.
- `ProcessingService.revokeToken(java.util.UUID)` — Revokes a previously issued share token.
- `ValidationService.validate(java.util.UUID, Double)` — Validates processing results against a configurable confidence threshold and returns a boolean pass/fail.

### Form components

- `IDPStandaloneUI` — Composite JSF component that embeds the IDP standalone UI in an iframe.
   - Attributes: `widgetVar` (required) — JSF widget variable used to trigger `save()`; `style` (optional) — CSS style for the iframe; `styleClass` (optional) — CSS class for the surrounding container.

### Maven artifacts

- Add the following artifacts to your Maven import (product IARs):

```xml
<dependency>
   <groupId>com.axonivy.connector.idp</groupId>
   <artifactId>idp-connector</artifactId>
   <version>${version}</version>
   <type>iar</type>
</dependency>

<dependency>
   <groupId>com.axonivy.connector.idp</groupId>
   <artifactId>idp-connector-demo</artifactId>
   <version>${version}</version>
   <type>iar</type>
</dependency>
```