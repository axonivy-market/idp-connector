# Axon Ivy IDP-Konnektor

Axon Ivy IDP ist eine intelligente Dokumentenverarbeitungslösung, die die
Extraktion, Klassifizierung und Analyse unstrukturierter Daten automatisiert.
Sie optimiert dokumentenintensive Prozesse wie Rechnungsmanagement,
Schadenbearbeitung und Kundenonboarding mithilfe von KI-gestützter OCR,
Handschrifterkennung (HTR) und Algorithmen für maschinelles Lernen, um die
Genauigkeit und Effizienz der Datenverwaltung zu steigern. Dieser Konnektor:

* Sie haben Zugriff auf die Funktionen von Axon Ivy: Vorverarbeitung,
  Klassifizierung und Datenextraktion.
* verfügt über eine integrierte Validierung der Verarbeitungsergebnisse.
* Minimiert Ihren Integrationsaufwand mit einer eigenständigen UI-Komponente.
* basiert auf REST-Webdienst, Axon Ivy UI Component und
  SubCallable-Prozesstechnologien.

## Demo

### Dokumentaufteilung

1. Starten Sie „DocumentSplitting“!
   [splitting-start](images/splitting-document-1.png)
2. Klicken Sie auf „Verarbeiten“, um das Ergebnis des
   Dokumentaufteilungsdienstes zu überprüfen!
   [splitting-review](images/splitting-document-2.png)
3. Klicken Sie auf „Verarbeiten“, um das Ergebnis der Dokumentaufteilung
   anzuzeigen und die einzelnen Seiten herunterzuladen.
   ![splitting-result](images/splitting-document-3.png)
4. Überprüfen Sie das letzte Protokoll, um das Ergebnis der Validierungsfunktion
   zu sehen! [validation-splitting](images/splitting-document-4.png)

### Extraktion

1. Starten Sie die „Extraktion“! [extraction-start](images/extraction1.png)
2. Wählen Sie ein Dokument aus der Liste aus und klicken Sie dann auf
   „Verarbeiten“, um das Ergebnis der Extraktion zu überprüfen.
   ![extraction-review](images/extraction2.png)
3. Klicken Sie auf „Verarbeiten“, um das Extraktionsergebnis anzuzeigen!
   [extraction-result](images/extraction3.png)
4. Überprüfen Sie das letzte Protokoll, um das Ergebnis der Validierungsfunktion
   zu sehen! [validation-extraction](images/extraction4.png)

## Setup

Bevor eine Interaktion zwischen der Axon Ivy Engine und den IDP-Diensten
stattfinden kann, müssen die folgenden Schritte durchgeführt werden:

1. `Beziehen Sie einen funktionierenden Axon Ivy IDP-API-Schlüssel`, indem Sie
   sich an support@axonivy.com wenden – dieser ist erforderlich, um die
   REST-API-Dienste aufzurufen.
2. Überschreiben Sie die globale Variable für „ `“ apiKeySecret` im Demo-Projekt
   wie im folgenden Beispiel gezeigt.

```
@variables.yaml@
```
