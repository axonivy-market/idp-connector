## Axon Ivy IDP Connector

Axon Ivy IDP ist eine leistungsstarke Lösung zur intelligenten Dokumentenverarbeitung, die die Extraktion, Klassifizierung und Analyse unstrukturierter Daten automatisiert und dokumentenintensive Prozesse in effiziente Workflows verwandelt.

Lies unsere [Dokumentation](idp-connector-product/README.md).

## Wichtigste Funktionen

- Automatisiere die Dokumentenextraktion und -klassifizierung, um manuellen Aufwand zu reduzieren.
- Erkenne und teile Dokumente zuverlässig, um die nachgelagerte Verarbeitung zu vereinfachen.
- Integriere Verarbeitungsergebnisse direkt in Axon Ivy-Prozesse über bereit nutzbare Aufrufe (callable subs).
- Überprüfe und validiere Extraktionsergebnisse in Demo-Dialogs zur Qualitätssicherung.
- Teile Verarbeitungsergebnisse und Thumbnails sicher über tokenbasierte Links.
- Skalierbare REST-API-Integration für Batch- und interaktive Szenarien.

## Demo

Probiere die bereitgestellten Demo-Implementierungen aus. Besuche das Demo-Modul, um Beispiel-Workflows zu testen.

### Demo-Workflows

#### idp-connector-demo (idp-connector-demo)

##### Document Splitting

1. Starte die Demo "Document Splitting" im Demo-Menü.
2. Wähle im Datei-Auswahl-Dialog die Datei aus, die getrennt werden soll.
3. Überprüfe und bestätige die vorgeschlagenen Trennpunkte im Überprüfungsdialog.
4. Erhalte die geteilten Dokumente und lade sie optional herunter oder zeige sie an.

    ![Dokumentaufteilung Schritt 1](images/splitting-document-1.png)

    ![Dokumentaufteilung Schritt 2](images/splitting-document-2.png)

    ![Dokumentaufteilung Schritt 3](images/splitting-document-3.png)

    ![Dokumentaufteilung Schritt 4](images/splitting-document-4.png)

##### Document Extraction

1. Starte die Demo "Document Extraction" im Demo-Menü.
2. Wähle eine Beispieldatei im Datei-Auswahl-Dialog.
3. Der Extraktionsergebnis-Dialog zeigt erkannte Felder und extrahierte Daten.
4. Überprüfe die extrahierten Daten und lade bei Bedarf die Ergebnisse herunter.

    ![Dokumentextraktion Schritt 1](images/extraction1.png)

    ![Dokumentextraktion Schritt 2](images/extraction2.png)

    ![Dokumentextraktion Schritt 3](images/extraction3.png)

    ![Dokumentextraktion Schritt 4](images/extraction4.png)

## Einrichtung

- **Rollen:** Everybody (konfiguriert in config/roles.xml)
- **OpenAPI:** `${ivy.var.idpConnector.apiProxyUrl}/api` — Namespace: `com.axonivy.connector.idp.connector.model`

### Variablen

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

- Für diesen Abschnitt wurden keine weiteren Setup-Details im Repository gefunden.

## Komponenten

### Connector-Prozesse

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

### Formular-Komponenten

#### IDPStandaloneUIData — Standalone-UI-Daten für den IDP-Dialog
- **Namespace:** com.axonivy.connector.idp.connector.IDPStandaloneUI
- **Komponententyp:** Data Class
- **Felder:**
   - `workflowType` (com.axonivy.connector.idp.connector.WorkflowType) — der Workflow-Typ des Dokuments: document-spliting ODER extraction
   - `documentId` (java.util.UUID) — 
   - `shareInfo` (com.axonivy.connector.idp.connector.model.DocShareTokenInfo) — 
   - `sharingUrl` (String) — 
   - `apiProxyUrl` (String) — 

### Maven-Artefakte

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
