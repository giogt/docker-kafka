# kafka-mirror-maker
Docker image for Kafka Mirror Maker

## What is Kafka Mirror Maker?

Kafka Mirror Maker is a simple tool able to mirror topics from a Kafka cluster to another Kafka cluster (https://kafka.apache.org/documentation/#basic_ops_mirror_maker, https://docs.confluent.io/current/multi-dc-replicator/mirrormaker.html).
This is a useful operation, since Kafka streaming application, at the moment of writing, only work within the same Kafka cluster.

It is part of the Kafka platform (http://kafka.apache.org/documentation/), it is not an extension made by `Confluent` and it is open source and free to use.
It is basically just a bit more than a Kafka consumer and a Kafka producer put together, therefore it provides a very basic mirroring strategy, which makes it good for simple use cases. Other tools, such as `Confluent Replicator`, are much more powerful and they are able to replicate also topic offsets, replicas, partitions, etc., making it possible to have an exact replica of a Kafka cluster for high availability and failover.

For simple use cases, though, `Mirror Maker` is exactly what you need.

## Why this image?

`Confluent` is packaging a lot of Docker images (https://hub.docker.com/u/confluentinc/), but not the `Mirror Maker` one. That's why this project exists.

## Usage

The Mirror Maker usage is described in the following links:

- https://kafka.apache.org/documentation/#basic_ops_mirror_maker
- https://docs.confluent.io/current/multi-dc-replicator/mirrormaker.html

This Docker image, similarly to other `Confluent` docker images, uses an approach to convert environment variables provided when you run the container to configuration properties files needed to run the tool.

- all environment variables with prefix `MIRROR_MAKER_CONSUMER_`, will be converted into properties inside the `consumer.properties` file
- all environment variables with prefix `MIRROR_MAKER_PRODUCER_`, will be converted into properties inside the `producer.properties` file

The conversion works as follows:
- the prefix will be stripped
- the string converted to lower case
- all the `_` will be replaced with `.`

So, for example, the environment variable `MIRROR_MAKER_CONSUMER_BOOTSTRAP_SERVERS=broker-1:9092` will be converted into the following property inside the `consumer.properties` file:

```
bootstrap.servers=broker-1:9092
```

The environment variable `MIRROR_MAKER_WHITELIST` is mandatory and describes the source topics whitelist (it is a list of Java regexs separated by `,`, refer to the documentation for more details).

The environment variable `MIRROR_MAKER_OPTS` lets you specify additional command line options (e.g., `--abort.on.send.failure=true`).


Example usage to run a container:

```
docker run -i -t \
  -e MIRROR_MAKER_WHITELIST="\.*" \
  -e MIRROR_MAKER_CONSUMER_BOOTSTRAP_SERVERS="broker-1:9092" \
  -e MIRROR_MAKER_CONSUMER_GROUP_ID=test \
  -e MIRROR_MAKER_CONSUMER_CLIENT_ID=test \
  -e MIRROR_MAKER_CONSUMER_ENABLE_AUTO_COMMIT=false \
  -e MIRROR_MAKER_CONSUMER_AUTO_OFFSET_RESET=earliest \
  -e MIRROR_MAKER_CONSUMER_PARTITION_ASSIGNMENT_STRATEGY="org.apache.kafka.clients.consumer.RoundRobinAssignor" \
  -e MIRROR_MAKER_PRODUCER_BOOTSTRAP_SERVERS="broker-2:9092" \
  -e MIRROR_MAKER_PRODUCER_CLIENT_ID=test \
  -e MIRROR_MAKER_PRODUCER_ACKS=-1 \
  -e MIRROR_MAKER_PRODUCER_BATCH_SIZE=100 \
  -e MIRROR_MAKER_PRODUCER_MAX_IN_FLIGHT_REQUESTS_PER_CONNECTION=1 \
  -e MIRROR_MAKER_PRODUCER_RETRIES=2147483647 \
  -e MIRROR_MAKER_OPTS="--abort.on.send.failure=true" \
  giogt/kafka-mirror-maker:2.0.0
```

## Project structure

- `src/docker` contains everything that is needed to build the Docker image
- `src/test` contains a test on the whitelist behaviour: it's written in scala, it uses the `Whitelist` class used by the mirror maker and it is useful to check whether a whitelist is matching a specific topic or not
- `build.sh` runs the scala test and builds the Docker image
- `publish.sh` publishes the Docker image to DockerHub
- `build.sbt` is the `sbt` configuration file to compile and run the scala test (with `sbt clean test`)
