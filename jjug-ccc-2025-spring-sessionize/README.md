JJUG CCC 2025 Sessionise cfp submission
----

Session Title
----
Kafka Connect: a 12-Step Program for Java Developers

-----------

Description
----
_Session overview (visible to participants) Session details for selection committee should be posted in "presentation
outline". / タイムテーブルに掲載されるセッション概要。セッションの詳細情報は、Presentation outlineに掲載してください。_

Apache Kafka is widely considered to be the industry-leading event streaming platform; it runs on the JVM, and it is
Open Source.

Kafka Connect is an integral component written in Java that allows you to reliably and scalably stream large amounts
of data between Apache Kafka and other systems in a fault-tolerant manner, including Databases.

Join me on a 12-step program where we will use Java, Gradle, Spring Boot, and Docker Compose as the main build tools to
create and run an integrated stack of Confluent Community version Kafka components and a Postgres Database.

Once our Kafka environment is in place we will create a JDBCSinkConnector to ingest data from Kafka to Postgres,
write a custom SMT in Java to transform the data, and validate the code is production ready with a @SpringBootTest
integration test.

Topics, Producers, Consumers, and Connectors... find out what it's all about.

Let's get Connected with Java and Kafka Connect, then take the next step by yourself with all the new knowledge!


todo
---

Written in Java, Kafka Connect also runs on the jvm, so you can use all your existing Java skills to write a Single
Message Transform (SMT) to shape your data as it flows through Kafka Connect (they will come in handy for debugging too
when things start to break.)

----

Presentation outline
---
_Abstracts and agenda of your presentation, and scope/coverage of your talk. (for selection purpose and visible to
selection committee only). / 発表要旨、アジェンダ、発表の範囲（話すこと、話さないこと）【選考にのみ使用_

The presentation will start with explanations of the core concepts of Kafka (Broker/Topic/Producer/Consumer etc.)
and the software components composing an integrated Kafka Connect stack using the Confluent Community version of Kafka.

I will demonstrate to the attendees how to use the Confluent Community version tools to help the beginner get familiar
with Kafka, Topics, and the Schema Registry using the Control Center UI.

Moving forward in the session, I will then start to build a Gradle project from scratch and write Java code to show a
beginner level example of an SMT that transforms data as it flows from Kafka to a Postgres Database. I will finish the
live coding session with a Spring Boot integration test to validate that the code is production ready, and then deploy
the code to the cloud with a UI web app where attendees can POST data on a form and see it flow through Kafka to the DB.

Attendees can use the codebase after the session as a starting point for their own Kafka Connect application and start
building a Docker image to use in production.

I have been building up this project over the past couple of years based on the knowledge I have learned by deploying
and maintaining a Kafka Connect application running in a Production environment with Docker and AWS ECS.

The twelve steps I will demonstrate are:

1. Run a Kafka Connect Docker Compose stack
2. Create a JDBCSinkConnector - v1
3. Create a Gradle project to build an SMT library
4. Code a custom toJson Kafka Connect SMT in Java
5. Build our own Kafka Connect Docker Image
6. Create a JDBCSinkConnector - v2 - using our toJson SMT
7. Implement connector creation at container startup
8. Apply a Gradle Multi-Project structure
9. Code a Spring Boot Kafka Producer application
10. Code a @SpringBootTest Integration Test (with gradle-docker-compose-plugin)
11. Apply the JaCoCo Report Aggregation Plugin
12. Deploy and test on AWS with an interactive UI web app

----

Tag line
---
Data Engineer - Woven by Toyota

----

Speaker Biography
---
Barry has been developing with Java since 2007 in a range of industries. He started implementing solutions in 2022 with
Java, Apache Kafka, and AWS to solve real-world problems.

He was previously co-leader of the Dublin Java User Group (DubJug) in Ireland before moving to Japan in 2022, and
recently moved to Tokyo and wants to share his positive experiences using Kafka Connect with the Java Community.
