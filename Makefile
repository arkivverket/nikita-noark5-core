
project ?=oslometabi/nikita-noark5-core
repo_tester_dir ?=../noark5-tester
repo_tester ?=https://github.com/petterreinholdtsen/noark5-tester

all: run

build:
	mvn clean validate install
run: build
	mvn -f pom.xml spring-boot:run
clean:
	mvn -DskipTests=true clean
webjars:
	mvn validate
# This target should be run after spinning up elasticsearch and the application.
# The tester might have more dependencies but you should at least have installed
# python3.
tt:
	if ! test -d $(repo_tester_dir); then \
	  git clone $(repo_tester) $(repo_tester_dir); \
	fi
	cd $(repo_tester_dir) && ./runtest
docker:
	mvn -DskipTests=true spring-boot:build-image
docker_push: docker
	@echo Assuming DOCKER_HUB_USER DOCKER_HUB_PASSWORD shell variables are set to log into Docker hub
	mvn compile -DskipTests=true jib:build -Djib.to.image=registry.hub.docker.com/oslometabi/nikita-noark5-core:latest -Djib.to.auth.username=$$DOCKER_HUB_USER -Djib.to.auth.password=$$DOCKER_HUB_PASSWORD
docker_run: docker
	docker run  -dit -v /data:/data -p8092:8092 oslometabi/nikita-noark5-core:latest
docker_tail:
	docker logs `docker ps | grep ${project} | awk ' { print $$1 } '`
docker_compose:
	docker-compose up
