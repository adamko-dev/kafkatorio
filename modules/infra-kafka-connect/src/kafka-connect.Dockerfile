ARG KAFKA_CONNECT_VERSION


FROM confluentinc/cp-kafka-connect-base:${KAFKA_CONNECT_VERSION}

ENV CONNECT_VERSION=${KAFKA_CONNECT_VERSION}

USER 0:0

COPY ./camel-kafka-connectors /usr/share/java/

#WORKDIR /usr/share/java/
#
##ENV CAMEL_KC_VERSION=0.9.0
##ENV CAMEL_CONNECTOR=camel-file-kafka-connector
#
#ENV URL=https://repo.maven.apache.org/maven2/org/apache/camel/kafkaconnector/camel-file-kafka-connector/0.9.0/camel-file-kafka-connector-0.9.0-package.tar.gz
#
#RUN curl -L $URL | tar -xzvf - --strip=0 $CAMEL_CONNECTOR
