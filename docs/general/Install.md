# INSTALL

This document is structured in way to provide you with options on how to get the application up and running. If you want
to get an overview of all options then you might want to read this document in full. We currently have 3 ways to run the
application. These are _maven_, _make_ and _docker_. If you want to use something else, please update this document.

You probably know by now that this is a Java so you need both maven and Java. Depending on which route you pick you
might also need to install other dependencies. The project is developed on a Linux machine with Apache Maven 3.6.0 and
Java 11. Java 11 is a minimum requirement to build the code. Please make sure both of these are installed before you
attempt to run the project. You can verify your versions with:

    mvn --version
    java -version

## Getting the code

The latest version of the code is available from Gitlab at
[OsloMet-ABI/nikita-noark5-core](https://gitlab.com/OsloMet-ABI/nikita-noark5-core).
If you haven't cloned the project then:

    git clone https://gitlab.com/OsloMet-ABI/nikita-noark5-core.git

If you already have the code consider synchronizing your local copy:

    git fetch --all
    git checkout origin/master

Remember to also read [Test data](Testa-data.md) to understand how you can
populate the core with data.


## Database and Elasticsearch Configuration

The default profile creates an in-memory database using H2 and connects to an Elasticsearch
instance running on localhost:9200 without any username or password. MySQL or Postgres can be
used instead of H2 by activating the corresponding Spring profile ("mysql" or "postgres").

The following environment variables can be set to further configure the data connections:

| Variable Name      | Comment           | Default  |
| ------------- |:-------------:| -----:|
| NIKITA_BASE_DIR | The root directory used for storage of document files in Nikita. | `/data2` |
| DB_URI | The JDBC string for connecting to the DB when using the mysql / postgres profiles. | Mysql: `jdbc:mysql://localhost:3306/nikita_noark5_prod?serverTimezone=Europe/Oslo` Postgres: `jdbc:postgresql://localhost:5432/nikita` |
| DB_USER | The DB user. | `INSERT-USERNAME-HERE` (or blank when using H2). |
| DB_PASS | The password for DB_USER. | `INSERT-PASSWORD-HERE` (or blank when using H2). |
| ELASTIC_URI | URI to the Elastic host | `http://localhost:8200`. |
| ELASTIC_USER | The elastic user. | No default (i.e. no auth required). |
| ELASTIC_PASS | The elastic user password. | No default. |


## Makefile

This option is a wrapper around the maven command.

To compile the core and start it automatically, from the top level directory:

    make

## Maven

Please note that maven will automatically download all dependencies (jar files)
and put them in a directory ~/.m2. If you are uncomfortable with this, please check the pom.xml files to find out which
jar files will be downloaded.

    mvn clean validate install
    mvn spring-boot:run
    # Or, using an alternative ELASIC_URI:
    # mvn spring-boot:run -Dspring-boot.run.jvmArguments="-DELASTIC_URI=http://elasticsearch:9200"
    # Or, using an alternative Spring profile and a custom DB username and password:
    # mvn spring-boot:run -Dspring-boot.run.profiles=mysql -Dspring-boot.run.jvmArguments="-DDB_USER=mydbuser -DDB_PASS=mypass"

The debian operating system provides some packages that can install some of these packages for you. If you have your
maven repository you can set it accordingly in pom.xml.

You will see a lot of different startup messages, but there should be no exceptions. (Please let us know if there are
any exceptions).

The program should output something like the following if everything is successful

 	Application 'OsloMet Noark 5 Core (Demo mode)' is running! Access URLs:
 	Local: 			http://localhost:8092
 	External: 		http://127.0.1.1:8092
 	contextPath: 	http://127.0.1.1:8092/noark5v5
 	Application is running with following profile(s): []

## Docker

It is a goal of the project to index all content (metadata as well as documents) in elasticsearch. We have had limited
success with elastic search but as the project progresses towards version 1.0 elasticsearch support will be brought back
in. Therefore, you may see varying use for elasticsearch as we test things. As elasticsearch and other services are
introduced (conversion to archive format (PDF/A)) other containers that nikita requires for a fully working application
will be documented and available with the following script.

    scripts/start-containers

### Docker hub

When a push to master occurs the CI pipeline automatically builds nikita and pushes it to docker hub. The latest version
of nikita can be pulled and built from docker hub using

    docker pull oslometabi/nikita-noark5-core

To start nikita you can use

    docker run  -dit -v /data:/data -p8092:8092 oslometabi/nikita-noark5-core:latest

The /data:/data is required for the document store. The version of nikita from docker hub runs in a **demo** mode with
the H2 in memory database for the persistence layer. **Note**: _This means **all** data in the database is lost when the
image is stopped_. The document store should be OK. The -p8092:8092 is to expose the port that nikita uses.

Note we use the jib:build plugin to build and push nikita to docker hub. You can use a similar approach if you want to
build and push to your own repository.

### Local maven build

Spring Boot >= 2.3.0 allows you to package nikita into a docker image. Just run

     mvn spring-boot:build-image

in the root of the project directory and it should build a docker image for you.

To run the image use:

    docker run  -dit -v /data:/data -p8092:8092 oslometabi/nikita-noark5-core:latest

We currently don't have a Dockerfile as the maven approach works just fine. If this is something you would like to add,
send a merge request with a working approach.
