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

## Starten mit Docker

Voraussetzung: [Docker Desktop](https://www.docker.com/products/docker-desktop) ist installiert und läuft.

**1. Projekt herunterladen**

```bash
git clone https://github.com/smilenow789/test-management-webapp-jakarta-ee.git
cd test-management-webapp-jakarta-ee
```

**2. Einstellungen anlegen:** Die Datei `.env.example` unter dem Namen `.env` kopieren.

```bash
# Windows (PowerShell)
copy .env.example .env

# macOS / Linux
cp .env.example .env
```

Danach die Datei `.env` in einem Texteditor öffnen und die drei Passwörter durch eigene ersetzen
(`MYSQL_ROOT_PASSWORD`, `DB_PASSWORD`, `APP_SEED_PASSWORD`). Die Werte sind frei wählbar. Alle
anderen Zeilen bleiben unverändert.

**3. Starten**

```bash
docker compose up --build
```

Der erste Start dauert einige Minuten, weil die Anwendung gebaut wird. Sobald das Terminal
ruhig bleibt, ist die Anwendung bereit.

**4. Anmelden:** Im Browser <http://localhost:8080/login.xhtml> öffnen. Als Benutzername zum
Beispiel `Admin` eingeben. Das Passwort ist der Wert von `APP_SEED_PASSWORD` aus der Datei `.env`.
Weitere Demo-Benutzer sind `NameTestmanager` und `Nametester` (insgesamt sieben, alle mit dem
gleichen Passwort).

**Beenden:** `Strg + C` im Terminal, danach `docker compose down`.
Mit `docker compose down -v` werden zusätzlich alle gespeicherten Daten gelöscht.


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
