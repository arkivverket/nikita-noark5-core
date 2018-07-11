# docker build says: reference format: repository name must be lowercase
project ?=oslomet/nikita-noark5-core
repo_tester_dir ?=../noark5-tester
repo_tester ?=https://github.com/petterreinholdtsen/noark5-tester

all: run

build:
	mvn -Dmaven.test.skip=true clean validate install
run: build
	mvn -f pom.xml spring-boot:run
clean:
	mvn -Dmaven.test.skip=true clean
webjars:
	mvn validate
# This target should be run after spinning up elasticsearch and the application.
# The tester might have more dependencies but you should at least have installed
# python-mechanize.
tt:
	if ! test -d $(repo_tester_dir); then \
	  git clone $(repo_tester) $(repo_tester_dir); \
	fi
	cd $(repo_tester_dir) && ./runtest
docker:
	docker build -t ${project} .
docker_deploy: docker docker_push
	echo "Pushed to docker, https://hub.docker.com/r/${project}"
docker_run: docker
	docker run --network="host" --add-host=$(shell hostname):127.0.0.1 ${project}

docker_push:
	docker push ${project}

docker_tail:
	docker logs `docker ps | grep ${project} | awk ' { print $$1 } '`

docker_compose:
	docker-compose up

# Prepare a package which can be used to deploy the application
package: build
	mvn -Dmaven.test.skip=true package spring-boot:repackage

stop-containers:
	docker stop server web

vagrant:
	vagrant box update
	vagrant up
