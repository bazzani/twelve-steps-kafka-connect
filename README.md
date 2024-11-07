# Kafka Connect: a 12-Step Program for Java Developers

Kafka is the leading event streaming platform; it runs on the JVM, and it is Open Source.

Kafka Connect is an integral component of the platform that allows you to reliably and scalably stream large amounts of
data between Apache Kafka and other systems (including Databases) in a fault-tolerant manner.

Written in Java, Kafka Connect also runs on the jvm, so you can use all your existing Java
skills to write a Single Message Transform (SMT) to shape your data as it flows through Kafka Connect
(they will come in handy for debugging too when things start to break.)

Join me on a 12-step program where we will use Gradle as the main build tool to create and run a complete stack of
integrated Confluent software components and a Postgres Database from scratch with Docker Compose. We will create a
JDBCSinkConnector to ingest data from Kafka to Postgres, write a custom SMT in Java to transform the data, and validate
the code is production ready with a @SpringBootTest integration test.

Topics, Consumers, Producers, Offsets, Schema Registry, Connectors... what's it all about?

Let's get Connected with Java and Kafka Connect, then take the next step by yourself!

---

The Twelve Steps of Kafka Connect:

1. Kafka, Connect (and Connectors)
2. Kafka Connect Docker Compose Stack
3. Consuming the old way (Java, and Spring+JDBC)
4. Kafka Connect Docker Image
5. Kafka Connect REST API (with Postman)
6. JDBCSinkConnector v1 (using built-in SMT)
7. `StructArrayToJsonTransform` (Single Message Transform)
8. JDBCSinkConnector v2 (using custom SMT)
9. Automatic Connector Creation
10. @SpringBootTest integration test
11. gradle-docker-compose-plugin
12. Developer experience enhancements (logger REST API, debugging)

---

> _Written in Java, Kafka Connect also runs on the jvm, so you can use all your existing Java skills to write a Single
Message Transform (SMT) to shape your data as it flows through Kafka Connect (they will come in handy for debugging too
when things start to break.)_