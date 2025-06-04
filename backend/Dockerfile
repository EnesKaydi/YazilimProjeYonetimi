FROM ubuntu:22.04

RUN apt-get update && \
    apt-get install -y doxygen graphviz && \
    apt-get clean

WORKDIR /app

ENTRYPOINT ["doxygen"]
