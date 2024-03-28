FROM sbtscala/scala-sbt:eclipse-temurin-17.0.4_1.7.1_3.2.0

WORKDIR /app

COPY . /app

RUN sbt clean compile

CMD ["sbt", "run"]