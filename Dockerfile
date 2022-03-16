FROM maven:3.8.3-openjdk-11-slim as build

RUN mkdir -p /app/source && mkdir -p /app/log && mkdir -p /app/build && mkdir -p /target/dependency

COPY . /app/source/

WORKDIR /app/source/
RUN mvn dependency:go-offline
RUN mvn clean package -DskipTests

RUN cp /app/source/target/*.war /app/build/application.war

RUN (cd /target/dependency; jar -xvf /app/build/application.war)

FROM eclipse-temurin:11.0.13_8-jre-alpine

# Create working folder in image
RUN mkdir -p /app/lib && mkdir -p /app/META-INF && mkdir -p /target/dependency && mkdir -p /app/log

# Copy file build
COPY --from=build /target/dependency /target/dependency

# Copy all source to working folder
RUN cp -r /target/dependency/WEB-INF/lib/* /app/lib
RUN cp -r /target/dependency/META-INF/* /app/META-INF
RUN cp -r /target/dependency/WEB-INF/classes/* /app

# Remove temp folder
RUN rm -rf /target/dependency

# Run this command when container start
ENTRYPOINT java -Xms128m -Xmx2g com.dvtt.demo.demospringbootkafka.DemoSpringbootKafkaApplication