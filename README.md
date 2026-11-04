# Testmanagement-Webanwendung für manuelle Anwendertests (Jakarta EE)

Mehrbenutzer-Webanwendung zur Organisation manueller Anwendertests: Anforderungen erfassen,
Testfälle daraus ableiten, Testläufe zusammenstellen, Tester zuweisen und Ergebnisse festhalten.
Jede Benutzerrolle sieht ihre eigene Oberfläche.

Entstanden im Studium (Softwareentwicklung, Programmierung von industriellen
Informationssystemen mit Java EE) als Prototyp für das fiktive Startup-Projekt *Require4Testing*.
Der Prototyp deckt die fünf höchstpriorisierten User Stories vollständig ab und wurde danach um
Konfiguration per Umgebungsvariablen und Docker ergänzt.

## Funktionen und Rollen

| Rolle | Aufgabe | Seite |
|---|---|---|
| Requirements Engineer | Anforderungen mit Titel und Beschreibung anlegen | `requirementsengineer.xhtml` |
| Testfallersteller | Testfälle zu einer Anforderung erstellen | `testfallersteller.xhtml` |
| Testmanager | Testfälle zu Testläufen bündeln und Tester zuweisen | `testmanager.xhtml` |
| Tester | zugewiesene Testfälle ausführen und das Ergebnis speichern | `tester.xhtml` |

Nach dem Login wird je nach Rolle auf die passende Seite weitergeleitet, jede Seite hat einen
Logout.

## Umgesetzte User Stories

Priorisiert nach der MoSCoW-Methode:

| Priorität | User Story |
|---|---|
| Must | Der Requirements Engineer erstellt zu testende Anforderungen. |
| Must | Der Testmanager legt Testläufe an. |
| Must | Der Testfallersteller erstellt zu einer Anforderung Testfälle. |
| Should | Der Testmanager ordnet einem Testlauf verschiedene Testfälle und einen Tester zu. |
| Should | Der Tester versieht ihm zugeordnete Testfälle mit einem Ergebnis. |

## Technik

| Schicht | Technologie |
|---|---|
| Präsentation | JSF (Mojarra), PrimeFaces, XHTML |
| Logik | CDI (Weld), Schichten Controller, Service, Repository |
| Persistenz | JPA mit Hibernate, MySQL 8 |
| Laufzeit | Tomcat 10.1, Java 17 |
| Build und Betrieb | Maven, Docker Compose |

## Architektur und Entwurf

Die Anwendung ist als Schichtenarchitektur aufgebaut: JSF-Seiten sprechen **Controller** (Backing
Beans) an, diese nutzen **Services** mit der Geschäftslogik, die über **Repositories** per JPA auf
die Datenbank zugreifen. Die Verbindung der Schichten übernimmt CDI per Dependency Injection. Die
angemeldete Person wird in einer sitzungsbezogenen Bean gehalten.

**Klassendiagramm** (Model, Controller, Service, Repository, Util):

![Klassendiagramm](docs/klassendiagramm.png)

**Datenbankmodell** (MySQL, Zuordnungstabellen für n:m-Beziehungen):

![ER-Diagramm](docs/er-diagramm.png)

**Navigation nach dem Login** (Weiterleitung je nach Benutzerrolle):

![Zustandsdiagramm der Navigation](docs/navigation-zustandsdiagramm.png)

**Wireframes der Seiten:**

![Wireframes](docs/wireframes.png)

## Starten mit Docker (empfohlen)

```bash
cp .env.example .env      # Werte anpassen
docker compose up --build
```

Danach: <http://localhost:8080/login.xhtml>. Beim ersten Start werden in der leeren Datenbank
sieben Demo-Benutzer angelegt (`Admin`, `NameTestmanager`, `Nametester`, …). Ihr gemeinsames
Passwort ist der Wert von `APP_SEED_PASSWORD` aus `.env`.

## Starten ohne Docker

Voraussetzungen: JDK 17, Maven, Tomcat 10.1, MySQL 8.

```powershell
# Umgebungsvariablen für die Datenbank setzen (Defaults siehe EntityManagerProducer)
$env:DB_URL = "jdbc:mysql://localhost:3306/fallstudiedb"
$env:DB_USER = "fallstudie"
$env:DB_PASSWORD = "..."
$env:APP_SEED_PASSWORD = "..."
.\scripts\deploy-war-tomcat.ps1 -TomcatHome "C:\tools\apache-tomcat-10.1.36"
```

## Projektstruktur

```
src/main/java/
  controller/   JSF-Backing-Beans und Login-Filter
  service/      Geschäftslogik
  repository/   JPA-Zugriffe
  model/        Entitäten
  util/         EntityManager-Producer, Session-Bean
src/main/webapp/  XHTML-Seiten, CSS
```

## Ausblick

- Pro Zuweisung eine eigene Testfall-Instanz, damit Ergebnisse verschiedener Testläufe
  unabhängig voneinander bleiben
- Gemeinsame Bestandteile der Seiten (zum Beispiel der Logout-Button) in Templates auslagern
- Registrierung neuer Benutzer und Passwortänderung
