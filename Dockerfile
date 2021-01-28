FROM alpine:3.13
LABEL version="1.0"

COPY src /warehouse/src/
COPY gradle /warehouse/gradle/
COPY build.gradle /warehouse/
COPY settings.gradle /warehouse/
COPY gradlew /warehouse/

WORKDIR /warehouse/

RUN apk --no-cache add gradle 
RUN apk --no-cache add ca-certificates
RUN apk --no-cache add libc6-compat
RUN apk --no-cache add openjdk11
RUN gradle wrapper

EXPOSE 9091
 
ENTRYPOINT ["./gradlew", "bootRun" ]
