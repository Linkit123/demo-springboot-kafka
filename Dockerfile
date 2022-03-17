# our base build image
FROM maven:3.8.4-jdk-11 as maven

# copy the project files
COPY ./pom.xml ./pom.xml

# build all dependencies
RUN mvn dependency:go-offline -B

# copy your other files
COPY ./src ./src

# build for release
RUN mvn clean package -DskipTests

# our final base image
FROM adoptopenjdk/openjdk11:x86_64-alpine-jre-11.0.5_10

# set deployment directory
WORKDIR /my-project

# copy over the built artifact from the maven image
COPY --from=maven target/demo-springboot-kafka-*.jar ./app.jar

# set the startup command to run your binary
ENTRYPOINT ["java", "-jar", "app.jar"]