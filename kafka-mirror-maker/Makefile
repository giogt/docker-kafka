SHELL := /bin/bash

build:
	set -e
	sbt clean package

	cd ./src/docker; \
	docker build -t kafka-mirror-maker:${tag} .

publish:
	docker tag kafka-mirror-maker:${tag} giogt/kafka-mirror-maker:${tag}
	docker push giogt/kafka-mirror-maker:${tag}
	docker tag kafka-mirror-maker:${tag} giogt/kafka-mirror-maker:latest
	docker push giogt/kafka-mirror-maker:latest
