FROM amazoncorretto:20-alpine-jdk as build
WORKDIR /workspace/app

COPY mvnw .
COPY .mvn .mvn
COPY pom.xml .
COPY src src

RUN ./mvnw install -DskipTests
RUN mkdir -p target/dependency && (cd target/dependency; jar -xf ../*.jar)

FROM amazoncorretto:20-alpine-jdk
VOLUME /tmp
ARG DEPENDENCY=/workspace/app/target/dependency
COPY --from=build ${DEPENDENCY}/BOOT-INF/lib /app/lib
COPY --from=build ${DEPENDENCY}/META-INF /app/META-INF
COPY --from=build ${DEPENDENCY}/BOOT-INF/classes /app
EXPOSE 8080
HEALTHCHECK --start-period=5s --interval=30s --timeout=10s --retries=5 CMD wget --no-verbose --tries=1 --spider localhost:8080/actuator/health || exit 1
ENTRYPOINT ["java","-cp","app:app/lib/*","com.mcfly.template.Sb3Application"]