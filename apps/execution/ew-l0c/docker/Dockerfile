#### Setup

ARG JDK_URI=https://download.java.net/java/GA/jdk17.0.2/dfd4a8d0985749f896bed50d7138ee7f/8/GPL/openjdk-17.0.2_linux-x64_bin.tar.gz
ARG JDK_VERSION=17.0.2

#### Fetch JDK

FROM ubuntu:focal AS jdk

RUN apt-get update \
    && apt-get install --no-install-recommends -y ca-certificates curl \
    && rm -rf /var/lib/apt/lists/*

ARG JDK_URI
RUN curl -L $JDK_URI > jdk.tar.gz

#### Build image from IPF

FROM artifactory.coprs.esa-copernicus.eu/s2-docker/s2level0:5.1.0

USER root

## Wrapper configuration
ARG JDK_VERSION
RUN groupadd -g 1001 wrapper
RUN adduser -p '*' -d /wrapper -u 1001 -g 1001 wrapper
RUN chmod 755 /wrapper
COPY --from=jdk jdk.tar.gz /wrapper/jdk.tar.gz
RUN cd /wrapper \
    && tar xvf jdk.tar.gz \
    && mv jdk-$JDK_VERSION jdk \
    && rm jdk.tar.gz
COPY *.jar /wrapper/app.jar
RUN chown -R wrapper:wrapper /wrapper

## Setup workspace folder
RUN mkdir -p /workspace
RUN chmod -R 755 /workspace
RUN chown -R wrapper:wrapper /workspace

## Setup INPUT folder (on shared volume)
RUN mkdir -p /input
RUN chmod -R 755 /input
RUN chown -R wrapper:wrapper /input

WORKDIR /wrapper
USER wrapper

ENTRYPOINT ["/wrapper/jdk/bin/java","-jar","/wrapper/app.jar"]
