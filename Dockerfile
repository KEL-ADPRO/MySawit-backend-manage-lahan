FROM docker.io/library/eclipse-temurin:21-jdk-alpine AS builder

WORKDIR /src/MySawit-backend-manage-lahan
COPY . .

RUN chmod +x gradlew

RUN ./gradlew clean bootJar

FROM docker.io/library/eclipse-temurin:21-jre-alpine AS runner
LABEL authors="Samuel"

ARG USER_NAME=mysawit-kebun
ARG USER_UID=1000
ARG USER_GID=${USER_UID}

RUN addgroup -g ${USER_GID} ${USER_NAME} \
    && adduser -h /opt/MySawit-backend-manage-lahan -D -u ${USER_UID} -G ${USER_NAME} ${USER_NAME}

USER ${USER_NAME}
WORKDIR /opt/MySawit-backend-manage-lahan
COPY --from=builder --chown=${USER_UID}:${USER_GID} /src/MySawit-backend-manage-lahan/build/libs/*.jar app.jar

EXPOSE 8082

ENTRYPOINT ["java"]
CMD ["-jar", "app.jar"]