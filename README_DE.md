# Axon Ivy IDP Connector

Axon Ivy IDP ist ein Connector für Intelligente Dokumentenverarbeitung (IDP), der die Dokumentaufteilung, Klassifikation und Datenauslese automatisiert. Er reduziert manuelle Arbeiten in dokumentintensiven Prozessen (z. B. Rechnungen oder Schadensfälle), indem er vorgefertigte Workflows, eine eigenständige UI-Komponente und eingebaute Validierung der Ergebnisse bereitstellt.

### Wichtigste Funktionen
- Automatisiert Dokumentaufteilung und Datenauslese mittels vorgefertigter Workflows.
- Validiert Extraktionsergebnisse mit konfigurierbaren Konfidenz-Schwellenwerten zur Verbesserung der Datenqualität.
- Ermöglicht kollaborative Prüfungen durch eine einbettbare, eigenständige UI-Komponente mit sicheren Freigabe-Links und Token-Widerruf.
- Bietet REST-Client-Integration mit API-Key-Authentifizierung und Multipart-Upload-Unterstützung für einfache Anbindung.
- Stellt Hilfsfunktionen zum Abruf von Thumbnails, Teil-PDFs und geparsten Extraktionsresultaten für die Weiterverarbeitung bereit.

## Demo

### Dokumentaufteilung

1. Starte 'Document Splitting' aus der Demo-Startliste.
   ![Dokumentaufteilung – Start](images/splitting-document-1.png)
2. Wähle eine Datei und klicke auf 'Process', um den Dienst zur Dokumentaufteilung auszuführen und die erkannten Trennpunkte in der Vorschau anzusehen.
   ![Dokumentaufteilung – Vorschau](images/splitting-document-2.png)
3. Überprüfe das Aufteilungsergebnis und lade bei Bedarf einzelne Seiten herunter.
   ![Dokumentaufteilung – Ergebnis](images/splitting-document-3.png)
4. Optional kannst du die Logs prüfen, um das Validierungsergebnis der Aufteilung zu verifizieren.
   ![Validierung – Aufteilung](images/splitting-document-4.png)

### Extraktion

1. Starte 'Document Extraction' aus der Demo-Startliste.
   ![Extraktion – Start](images/extraction1.png)
2. Wähle ein Dokument und klicke auf 'Process', um die Extraktion auszuführen und die geparsten Felder in der Vorschau zu prüfen.
   ![Extraktion – Vorschau](images/extraction2.png)
3. Sieh dir die detaillierten Extraktionsergebnisse an und lade benötigte Artefakte herunter.
   ![Extraktion – Ergebnis](images/extraction3.png)
4. Optional kannst du die Logs prüfen, um das Validierungsergebnis der Extraktion zu verifizieren.
   ![Validierung – Extraktion](images/extraction4.png)

### Workflow-Namen protokollieren (Hilfsprogramm)

- Funktion: Ruft verfügbare Workflows von der konfigurierten IDP-Instanz ab und schreibt sie ins Log zur Fehlersuche oder Inspektion.
- Ausführung: Dieses Hilfsprogramm wird standardmäßig nicht in der öffentlichen Startliste angezeigt; starte es im Engine Cockpit oder über die Demo-Tools, wenn du verfügbare Workflows prüfen möchtest.

## Einrichtung

Bevor die Axon Ivy Engine mit den IDP-Services interagieren kann, führe folgende Schritte aus:

1. Beschaffe dir einen Axon Ivy IDP API-Key (kontakt: support@axonivy.com). Der API-Key wird benötigt, um die IDP-REST-Services aufzurufen.
2. Konfiguriere die Connector-Variablen (API-URL, API-Key) in den Projektvariablen wie unten gezeigt.

```
@variables.yaml@
```

## Komponenten

### Exponierte CALLABLE_SUB-Prozesse

# Callable Sub Connector Starts

## ./idp-connector/processes/ProcessingService.p.json
- Signatur: processing
   Eingabe: workfowId: String, file: java.io.File
   Ergebnis: processingId: java.util.UUID, processingResult: com.fasterxml.jackson.databind.JsonNode, error: ch.ivyteam.ivy.bpm.error.BpmError
- Signatur: getSubPdf
   Eingabe: processingId: java.util.UUID, index: Integer, fileName: String
   Ergebnis: file: java.io.File, error: ch.ivyteam.ivy.bpm.error.BpmError
- Signatur: shareToken
   Eingabe: processing_id: java.util.UUID, expires_at: String
   Ergebnis: docShareTokenInfo: com.axonivy.connector.idp.connector.model.DocShareTokenInfo, error: ch.ivyteam.ivy.bpm.error.BpmError
- Signatur: revokeToken
   Eingabe: tokenUUID: java.util.UUID
   Ergebnis: error: ch.ivyteam.ivy.bpm.error.BpmError
- Signatur: retrieveThumbnail
   Eingabe: processingId: java.util.UUID
   Ergebnis: thumbnail: java.io.File, error: ch.ivyteam.ivy.bpm.error.BpmError
- Signatur: retrieveResult
   Eingabe: processingId: java.util.UUID
   Ergebnis: resultsNode: com.fasterxml.jackson.databind.JsonNode, error: ch.ivyteam.ivy.bpm.error.BpmError

## ./idp-connector/processes/ValidationService.p.json
- Signatur: validate
   Eingabe: processingId: java.util.UUID, confidenceMinValue: Double
   Ergebnis: passed: Boolean

### Formularkomponenten

#### IDPStandaloneUI
- namespace: com.axonivy.connector.idp.connector.IDPStandaloneUI
- Komponententyp: HTML_DIALOG (process) + JSF composite component (cc:interface IvyComponent)
- Startparameter: start(documentId: java.util.UUID, workflowType: com.axonivy.connector.idp.connector.WorkflowType) — workflowType standardmäßig WorkflowType.EXTRACTION
- Parameter:
   - documentId: java.util.UUID — Die Dokument-ID
   - workflowType: com.axonivy.connector.idp.connector.WorkflowType — Der Workflow-Typ des Dokuments: 'document-splitting' ODER 'extraction' (Standard: EXTRACTION)
- Methoden:
   - revokeShareToken(): Hebt das Sharing-Token auf, indem ProcessingService:revokeToken(java.util.UUID) mit `data.shareInfo.uuid` aufgerufen wird.
- Hauptfunktion/Logik: Betten die IDP-Bearbeitungs-UI in ein iframe ein, das über eine generierte Sharing-URL (ProcessingService:shareToken) geladen wird. Bietet eine PrimeFaces-Widget-Save-API (`PF('{widgetVar}').save()`), die "save-document" an das iframe via `apiProxyUrl` postet; führt Token-Widerruf beim Verlassen durch (Remote-Command, das `revokeShareToken()` aufruft); zeigt eine CMS-Fehlermeldung an, wenn keine sharingUrl verfügbar ist.

### Open API-Ressourcen

- Für dieses Produkt sind keine öffentlichen OpenAPI-Spezifikationen verfügbar

### Maven-Artefakte

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
