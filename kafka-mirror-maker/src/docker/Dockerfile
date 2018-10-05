FROM confluentinc/cp-kafka
LABEL maintainer="giorgio.gilitos@gmail.com"

ENV COMPONENT=kafka-mirror-maker

RUN mkdir -p /etc/"${COMPONENT}"
COPY include/etc/giogt/docker /etc/giogt/docker

CMD ["/etc/giogt/docker/run"]
