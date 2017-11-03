# How to Run

This is a step-by-step guide how to run the example:

## Installation

* The example is implemented in Java. See
   https://www.java.com/en/download/help/download_options.xml . The
   examples need to be compiled so you need to install a JDK (Java
   Development Kit). A JRE (Java Runtime Environment) is not
   sufficient. After the installation you should be able to execute
   `java` and `javac` on the command line.

* Maven is needed to build the examples. See
  https://maven.apache.org/download.cgi for installation . You should be
  able to execute `mvn`on the command line after the installation.

* The example run in Docker Containers. You need to install Docker
  Community Edition, see https://www.docker.com/community-edition/
  . You should be able to run `docker` after the installation.

* The example need a lot of RAM. You should configure Docker to use 4
  GB of RAM. Otherwise Docker containers might be killed due to lack
  of RAM. On Windows and macOS you can find the RAM setting in the
  Docker application under Preferences/ Advanced.
  
* After installing Docker you should also be able to run
  `docker-compose`. If this is not possible, you might need to install
  it separately. See https://docs.docker.com/compose/install/ .

## Build

Change to the directory `microservice-atom-demo` and run `mvn clean
package`. This will take a while:

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

If this does not work:

* Ensure that `settings.xml` in the directory `.m2` in your home
directory contains no configuration for a specific Maven repo. If in
doubt: delete the file.

* The tests use some ports on the local machine. Make sure that no
server runs in the background.

* Skip the tests: `mvn clean package package -Dmaven.test.skip=true`.

* In rare cases dependencies might not be downloaded correctly. In
  that case: Remove the directory `repository` in the directory `.m2`
  in your home directory. Note that this means all dependencies will
  be downloaded again.

## Run the containers

First you need to build the Docker images. Change to the directory
`docker` and run `docker-compose build`. This will download some base
images, install software into Docker images and will therefore take
its time:

```
[~/microservice-atom/docker]docker-compose build 
...
Step 7/7 : CMD apache2ctl -D FOREGROUND
 ---> Using cache
 ---> af6e0b1495b4
Successfully built af6e0b1495b4
Successfully tagged msatom_apache:latest
```

Afterwards the Docker images should have been created. They have the prefix
`msatom`:

```
[~/microservice-atom/docker]docker images 
REPOSITORY                                              TAG                 IMAGE ID            CREATED             SIZE
msatom_invoicing                                        latest              0090cd18bcef        30 seconds ago      210MB
msatom_shipping                                         latest              46697f86312e        33 seconds ago      210MB
msatom_order                                            latest              42f6aa9b18a8        36 seconds ago      210MB
msatom_apache                                           latest              af6e0b1495b4        12 days ago         244MB
msatom_postgres                                         latest              2b2f4f035d6d        12 days ago         269MB
```

If the build fails, you can use `docker-compose build --no-cache` to
build them from scratch.

Now you can start the containers using `docker-compose up -d`. The
`-d` option means that the containers will be started in the
background and won't output their stdout to the command line:

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

Check wether all containers are running:

```
[~/microservice-atom/docker]docker ps
CONTAINER ID        IMAGE               COMMAND                  CREATED             STATUS              PORTS                  NAMES
12b68d92c816        msatom_apache       "/bin/sh -c 'apach..."   17 seconds ago      Up 15 seconds       0.0.0.0:8080->80/tcp   msatom_apache_1
e814ed3846e7        msatom_shipping     "/bin/sh -c '/usr/..."   19 seconds ago      Up 17 seconds       8080/tcp               msatom_shipping_1
d40ae718ff2d        msatom_invoicing    "/bin/sh -c '/usr/..."   19 seconds ago      Up 16 seconds       8080/tcp               msatom_invoicing_1
9790867cbba1        msatom_order        "/bin/sh -c '/usr/..."   20 seconds ago      Up 19 seconds       8080/tcp               msatom_order_1
6591b192c64a        msatom_postgres     "docker-entrypoint..."   21 seconds ago      Up 20 seconds       5432/tcp               msatom_postgres_1
```

`docker ps -a`  also shows the terminated Docker containers. That is
useful to see Docker containers that crashed rigth after they started.

If one of the containers is not running, you can look at its logs using
e.g.  `docker logs msatom_order_1`. The name of the container is
given in the last column of the output of `docker ps`. Looking at the
logs even works after the container has been
terminated. If the log says that the container has been `killed`, you
need to increase the RAM assigned to Docker to e.g. 4GB. On Windows
and macOS you can find the RAM setting in the Docker application under
Preferences/ Advanced.
  
If you need to do more trouble shooting open a shell in the container
using e.g. `docker exec -it msatom_catalog_1 /bin/sh` or execute
command using `docker exec msatom_catalog_1 /bin/ls`.

You can now go to http://localhost:8080/ and enter an order. That will
create a shipping and an invoice in the other two microservices.

You can terminate all containers using `docker-compose down`.

