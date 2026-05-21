# Axon Ivy IDP Connector

Axon Ivy IDP ist eine leistungsfähige Lösung zur Intelligent Document Processing, die die Extraktion, Klassifizierung und Analyse unstrukturierter Daten automatisiert und dokumentenintensive Prozesse in schlanke, effiziente Workflows verwandelt. Sie integriert sich in eine IDP Document Capturing API und enthält Demo-Workflows für Dokumentaufspaltung, Extraktion und Validierung.

### Wichtigste Funktionen

- Dokumente automatisch verarbeiten, um unstrukturierte Dateien in strukturierte Daten zu überführen.
- Mehrseitige Dokumente in Teildokumente aufteilen, um gezielte Verarbeitung und einfaches Auffinden zu ermöglichen.
- Ergebnisse der Extraktion in Demo‑Dialogen prüfen und validieren, um schnelle Korrekturen zu ermöglichen.
- Verarbeitete Dokumente sicher per zeitlich begrenztem Share‑Token teilen.
- Integration über einen konfigurierbaren REST‑Client mit einer IDP Document Capturing API.
- Automatisierte Validierungsprüfungen mit konfigurierbaren Konfidenzschwellen.

## Demo

Siehe die Demo‑Implementierungen im Modul `idp-connector-demo` für Beispiele zu Document Splitting und Document Extraction.

### Demo‑Abläufe

#### idp-connector-demo (idp-connector-demo)

##### Document Splitting
1. Starte die Demo "Document Splitting" im Demo‑Menü.

   ![Document Splitting - Schritt 1](images/splitting-document-1.png)

2. Wähle im File Selection‑Dialog ein Dokument zum Aufteilen aus.

   ![Document Splitting - Schritt 2](images/splitting-document-2.png)

3. Überprüfe die Aufteilungsergebnisse im Splitting Result‑Dialog und lade die Teildokumente herunter.

   ![Document Splitting - Schritt 3](images/splitting-document-3.png)

4. Optional: Teile das Ergebnis per Share‑Token.

   ![Document Splitting - Schritt 4](images/splitting-document-4.png)

##### Document Extraction
1. Starte die Demo "Document Extraction" im Demo‑Menü.

   ![Document Extraction - Schritt 1](images/extraction1.png)

2. Wähle eine Datei und starte die Extraktion.

   ![Document Extraction - Schritt 2](images/extraction2.png)

3. Prüfe die extrahierten Felder im Extraction Result‑Dialog.

   ![Document Extraction - Schritt 3](images/extraction3.png)

4. Optional: Passe die Validierungskonfidenz an und führe eine Nachprüfung durch.

   ![Document Extraction - Schritt 4](images/extraction4.png)

##### Log Workflow names (Hintergrundwerkzeug)
1. Führe die Demo „Log Workflow names“ aus, um verfügbare Workflows der konfigurierten IDP‑Instanz abzurufen.
2. Ergebnisse werden ins Log geschrieben.

## Einrichtung

- **Rollen:** Everybody (konfiguriert in config/roles.xml)
- **OpenAPI:** Namespace: `com.axonivy.connector.idp.connector.model` — Basis‑URL: `${ivy.var.idpConnector.apiProxyUrl}/api`

### Variablen

```yaml
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
```

- **Authentifizierung:** Verwendet einen API‑Schlüssel in `idpConnector.apiKeySecret` (siehe `config/variables.yaml`).

- Es wurden keine Informationen für diesen Abschnitt geliefert.

## Komponenten

### Connector‑Prozesse

#### ProcessingService.p.json

- **processing(String workfowId, java.io.File file) -> processingId: java.util.UUID, processingResult: com.fasterxml.jackson.databind.JsonNode, error: ch.ivyteam.ivy.bpm.error.BpmError**
    - Eingabe:
        - `workfowId` (String) - 
        - `file` (java.io.File) - 
    - Ergebnis:
        - `processingId` (java.util.UUID) - 
        - `processingResult` (com.fasterxml.jackson.databind.JsonNode) - 
        - `error` (ch.ivyteam.ivy.bpm.error.BpmError) - 

- **getSubPdf(java.util.UUID processingId, Integer index, String fileName) -> file: java.io.File, error: ch.ivyteam.ivy.bpm.error.BpmError**
    - Eingabe:
        - `processingId` (java.util.UUID) - 
        - `index` (Integer) - 
        - `fileName` (String) - 
    - Ergebnis:
        - `file` (java.io.File) - 
        - `error` (ch.ivyteam.ivy.bpm.error.BpmError) - 

- **shareToken(java.util.UUID processing_id, String expires_at) -> docShareTokenInfo: com.axonivy.connector.idp.connector.model.DocShareTokenInfo, error: ch.ivyteam.ivy.bpm.error.BpmError**
    - Eingabe:
        - `processing_id` (java.util.UUID) - 
        - `expires_at` (String) - 
    - Ergebnis:
        - `docShareTokenInfo` (com.axonivy.connector.idp.connector.model.DocShareTokenInfo) - 
        - `error` (ch.ivyteam.ivy.bpm.error.BpmError) - 

- **revokeToken(java.util.UUID tokenUUID) -> error: ch.ivyteam.ivy.bpm.error.BpmError**
    - Eingabe:
        - `tokenUUID` (java.util.UUID) - 
    - Ergebnis:
        - `error` (ch.ivyteam.ivy.bpm.error.BpmError) - 

- **retrieveThumbnail(java.util.UUID processingId) -> thumbnail: java.io.File, error: ch.ivyteam.ivy.bpm.error.BpmError**
    - Eingabe:
        - `processingId` (java.util.UUID) - 
    - Ergebnis:
        - `thumbnail` (java.io.File) - 
        - `error` (ch.ivyteam.ivy.bpm.error.BpmError) - 

- **retrieveResult(java.util.UUID processingId) -> resultsNode: com.fasterxml.jackson.databind.JsonNode, error: ch.ivyteam.ivy.bpm.error.BpmError**
    - Eingabe:
        - `processingId` (java.util.UUID) - 
    - Ergebnis:
        - `resultsNode` (com.fasterxml.jackson.databind.JsonNode) - 
        - `error` (ch.ivyteam.ivy.bpm.error.BpmError) - 

#### ValidationService.p.json

- **validate(java.util.UUID processingId, Double confidenceMinValue) -> passed: Boolean**
    - Eingabe:
        - `processingId` (java.util.UUID) - 
        - `confidenceMinValue` (Double) - 
    - Ergebnis:
        - `passed` (Boolean) - 

### Formular‑Komponenten

#### IDPStandaloneUI — Laufzeitfelder für das Standalone‑UI in Demos
- **Namespace:** com.axonivy.connector.idp.connector.IDPStandaloneUI
- **Komponententyp:** Data Class
- **Felder:**
   - `workflowType` (com.axonivy.connector.idp.connector.WorkflowType) — the document's workflow type: document-spliting OR extraction
   - `documentId` (java.util.UUID)
   - `shareInfo` (com.axonivy.connector.idp.connector.model.DocShareTokenInfo)
   - `sharingUrl` (String)
   - `apiProxyUrl` (String)

#### SplittingResultData — Daten des Document Splitting Demos
- **Namespace:** com.axonivy.connector.idp.connector.demo.SplittingResult
- **Komponententyp:** Data Class
- **Felder:**
   - `processingId` (java.util.UUID)
   - `subDocuments` (java.util.List<com.axonivy.connector.idp.demo.dto.SubDocument>)
   - `file` (java.io.File)
   - `subDocument` (com.axonivy.connector.idp.connector.model.SubDocument)
   - `streamedContent` (org.primefaces.model.StreamedContent)
   - `prettyJson` (String)
   - `processingResult` (com.fasterxml.jackson.databind.JsonNode)
   - `genericSplitting` (com.axonivy.connector.idp.connector.model.GenericSplittingProcessingCompleted)
   - `selectedSubDocumentIndex` (Integer)

### Maven‑Artefakte

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
