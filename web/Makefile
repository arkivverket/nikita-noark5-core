project ?=nikita5/web
port ?=3000
instance_name=nikita_web

setup:
	npm install

setup-debian:
	apt-get install -y libjs-angularjs node-express node-body-parser wget
	mkdir -pv node_modules
	ln -s /usr/share/javascript/angular.js node_modules/angular
	ln -s /usr/lib/nodejs/express/ node_modules/express
	ln -s /usr/lib/nodejs/body-parser node_modules/body-parser
	# getmdl is not packaged for debian
	mkdir -pv node_modules/material-design-lite
	cd node_modules/material-design-lite
	wget https://code.getmdl.io/1.3.0/material.min.css
	wget dependencies/vendor/META-INF/resources/webjars/material-design-lite/1.3.0/material.min.js


run:
	DEBUG=web:* npm start
run-debian:
	nodejs server.js
# This target will restart server on changes.
tt:
	nodemon server.js

docker:
	docker build -t ${project} .
docker_deploy: docker docker_push
	echo "Pushed to docker, https://hub.docker.com/r/${project}"
docker_run: docker
	-docker stop ${instance_name}
	-docker rm ${instance_name}
	docker run --name=${instance_name} -d -p 3000:3000 ${project}
docker_push:
	docker push ${project}
docker_tail:
	docker logs `docker ps | grep ${project} | awk ' { print $$1 } '`

favicon.ico: favicon.svg
	convert favicon.svg -bordercolor white -border 0 \
  \( -clone 0 -resize 16x16 \) \
  \( -clone 0 -resize 32x32 \)  \
  \( -clone 0 -resize 48x48 \) \
  \( -clone 0 -resize 64x64 \) \
  -alpha off -colors 256 favicon.ico

