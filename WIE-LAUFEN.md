# Beispiel starten

Die ist eine Schritt-für-Schritt-Anleitung zum Starten der Beispiele.
Informationen zu Maven und Docker finden sich im
[Cheatsheet-Projekt](https://github.com/ewolff/cheatsheets-DE).

## Installation

* Die Beispiele sind in Java implementiert. Daher muss Java
  installiert werden. Die Anleitung findet sich unter
  https://www.java.com/en/download/help/download_options.xml . Da die
  Beispiele kompiliert werden müssen, muss ein JDK (Java Development
  Kit) installiert werden. Das JRE (Java Runtime Environment) reicht
  nicht aus. Nach der Installation sollte sowohl `java` und `javac` in
  der Eingabeaufforderung möglich sein.

* Die Projekte baut Maven. Zur Installation siehe
  https://maven.apache.org/download.cgi>. Nun sollte `mvn` in der
  Eingabeaufforderung eingegeben werden können.

* Die Beispiele laufen in Docker Containern. Dazu ist eine
  Installation von Docker Community Edition notwendig, siehe
  https://www.docker.com/community-edition/ . Docker kann mit
  `docker` aufgerufen werden. Das sollte nach der Installation ohne
  Fehler möglich sein.

* Die Beispiele benötigen zum Teil sehr viel Speicher. Daher sollte
  Docker ca. 4 GB zur Verfügung haben. Sonst kann es vorkommen, dass
  Docker Container aus Speichermangel beendet werden. Unter Windows
  und macOS findet sich die Einstellung dafür in der Docker-Anwendung
  unter Preferences/ Advanced.

* Nach der Installation von Docker sollte `docker-compose` aufrufbar
  sein. Wenn Docker Compose nicht aufgerufen werden kann, ist es nicht
  als Teil der Docker Community Edition installiert worden. Dann ist
  eine separate Installation notwendig, siehe
  https://docs.docker.com/compose/install/ .

## Build

Wechsel in das Verzeichnis `microservice-atom-demo` und starte `mvn clean
package`. Das wird einige Zeit dauern:

```
[~/microservice-atom/microservice-atom-demo]mvn clean package
...
[INFO] 
[INFO] --- maven-jar-plugin:2.6:jar (default-jar) @ microservice-atom-invoicing ---
[INFO] Building jar: /Users/wolff/microservice-atom/microservice-atom/microservice-atom-invoicing/target/microservice-atom-invoicing-0.0.1-SNAPSHOT.jar
[INFO] 
[INFO] --- spring-boot-maven-plugin:1.5.4.RELEASE:repackage (default) @ microservice-atom-invoicing ---
[INFO] ------------------------------------------------------------------------
[INFO] Reactor Summary:
[INFO] 
[INFO] microservice-atom .................................. SUCCESS [  0.913 s]
[INFO] microservice-atom-order ............................ SUCCESS [ 17.844 s]
[INFO] microservice-atom-shipping ......................... SUCCESS [ 14.340 s]
[INFO] microservice-atom-invoicing ........................ SUCCESS [ 15.222 s]
[INFO] ------------------------------------------------------------------------
[INFO] BUILD SUCCESS
[INFO] ------------------------------------------------------------------------
[INFO] Total time: 48.816 s
[INFO] Finished at: 2017-09-08T15:15:00+02:00
[INFO] Final Memory: 48M/517M
[INFO] ------------------------------------------------------------------------
```

Weitere Information zu Maven gibt es im
[Maven Cheatsheet](https://github.com/ewolff/cheatsheets-DE/blob/master/MavenCheatSheet.md).

Falls es dabei zu Fehlern kommt:

* Stelle sicher, dass die Datei `settings.xml` im Verzeichnis  `.m2`
in deinem Heimatverzeichnis keine Konfiguration für ein spezielles
Maven Repository enthalten. Im Zweifelsfall kannst du die Datei
einfach löschen.

* Die Tests nutzen einige Ports auf dem Rechner. Stelle sicher, dass
  im Hintergrund keine Server laufen.

* Führe die Tests beim Build nicht aus: `mvn clean package package
  -Dmaven.test.skip=true`.

* In einigen selten Fällen kann es vorkommen, dass die Abhängigkeiten
  nicht korrekt heruntergeladen werden. Wenn du das Verzeichnis
  `repository` im Verzeichnis `.m2` löscht, werden alle Abhängigkeiten
  erneut heruntergeladen.

## Docker Container starten

Zunächst musst du die Docker Images bauen. Wechsel in das Verzeichnis 
`docker` und starte `docker-compose build`. Das lädt die Basis-Images
herunter und installiert die Software in die Docker Images:

```
[~/microservice-atom/docker]docker-compose build 
...
Step 7/7 : CMD apache2ctl -D FOREGROUND
 ---> Using cache
 ---> af6e0b1495b4
Successfully built af6e0b1495b4
Successfully tagged msatom_apache:latest
```

Danach sollten die Docker Images erzeugt worden sein. Sie haben das
Präfix `msatom`:

```
[~/microservice-atom/docker]docker images 
REPOSITORY                                              TAG                 IMAGE ID            CREATED             SIZE
msatom_invoicing                                       latest              1fddb3132141        43 seconds ago      214MB
msatom_shipping                                        latest              7340d766ea6f        46 seconds ago      214MB
msatom_order                                           latest              0f9848e55054        49 seconds ago      215MB
msatom_atomcat                                        latest              461e8b02bb99        12 days ago          113MB
msatom_postgres                                        latest              2b2f4f035d6d        12 days ago          269MB
```

Weitere Information zu Docker gibt es im
[Docker Cheatsheet](https://github.com/ewolff/cheatsheets-DE/blob/master/DockerCheatSheet.md).

Wenn der Build nicht klappt, dann kann man mit `docker-compose build
--no-cache` die Container komplett neu bauen.

Nun kannst Du die Container mit `docker-compose up -d` starten. Die
Option `-d` bedeutet, dass die Container im Hintergrund gestartet
werden und keine Ausgabe auf der Kommandozeile erzeugen.

```
[~/microservice-atom/docker]docker-compose up -d
Creating network "msatom_default" with the default driver
Creating msatom_postgres_1 ... 
Creating msatom_postgres_1 ... done
Creating msatom_order_1 ... 
Creating msatom_order_1 ... done
Creating msatom_invoicing_1 ... 
Creating msatom_shipping_1 ... 
Creating msatom_shipping_1
Creating msatom_invoicing_1 ... done
Creating msatom_apache_1 ... 
Creating msatom_apache_1 ... done
```

Wenn das System zum ersten Mal gestartet wird, werden noch einige
Docker Images heruntergeladen.

Du kannst nun überprüfen, ob alle Docker Container laufen:

```
[~/microservice-atom/docker]docker ps
CONTAINER ID        IMAGE               COMMAND                  CREATED             STATUS              PORTS                  NAMES
12b68d92c816        msatom_apache       "/bin/sh -c 'apach..."   17 seconds ago      Up 15 seconds       0.0.0.0:8080->80/tcp   msatom_apache_1
e814ed3846e7        msatom_shipping     "/bin/sh -c '/usr/..."   19 seconds ago      Up 17 seconds       8080/tcp               msatom_shipping_1
d40ae718ff2d        msatom_invoicing    "/bin/sh -c '/usr/..."   19 seconds ago      Up 16 seconds       8080/tcp               msatom_invoicing_1
9790867cbba1        msatom_order        "/bin/sh -c '/usr/..."   20 seconds ago      Up 19 seconds       8080/tcp               msatom_order_1
6591b192c64a        msatom_postgres     "docker-entrypoint..."   21 seconds ago      Up 20 seconds       5432/tcp               msatom_postgres_1
```

`docker ps -a`  zeigt auch die terminierten Docker Container an. Das
ist nützlich, wenn ein Docker Container sich sofort nach dem Start
wieder beendet..

Wenn einer der Docker Container nicht läuft, kannst du dir die Logs
beispielsweise mit `docker logs msatom_order_1` anschauen. Der Name
der Container steht in der letzten Spalte der Ausgabe von `docker
ps`. Das Anzeigen der Logs funktioniert auch dann, wenn der Container
bereits beendet worden ist. Falls im Log steht, dass der Container
`killed` ist, dann hat Docker den Container wegen Speichermangel
beendet. Du solltest Docker mehr RAM zuweisen z.B. 4GB. Unter Windows
und macOS findet sich die RAM-Einstellung in der Docker application
unter Preferences/ Advanced.

Um einen Container genauer zu untersuchen, kannst du eine Shell in dem
Container starten. Beispielsweise mit `docker exec -it
msatom_catalog_1 /bin/sh` oder du kannst in dem Container ein
Kommando mit `docker exec msatom_catalog_1 /bin/ls` ausführen.

Unter http://localhost:8080/ kannst du nun eine Bestellung
erfassen. Nach einiger Zeit sollte für die Bestellung eine Lieferung
und eine Rechnung erstellt worden sei.

Mit `docker-compose down` kannst du alle Container beenden.
