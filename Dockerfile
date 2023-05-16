#BASE IMAGE for server
FROM bellsoft/liberica-openjdk-alpine-musl:17 AS server-build
COPY . /docker
WORKDIR /docker
RUN javac Server_app.java
CMD ["java", "Server_app"]

#BASE IMAGE for client1
FROM bellsoft/liberica-openjdk-alpine-musl:17 AS client1-build
COPY . /docker
WORKDIR /docker
RUN javac Client1_app.java
CMD ["java", "Client1_app"]


#BASE IMAGE for client2
FROM bellsoft/liberica-openjdk-alpine-musl:17 AS client2-build
COPY . /docker
WORKDIR /docker
RUN javac Client2_app.java
CMD ["java", "Client2_app"]
