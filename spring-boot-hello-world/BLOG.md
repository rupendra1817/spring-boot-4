# Dockerize a Spring Boot Hello World App — Step by Step

> Learn how to build a minimal Spring Boot REST API and containerize it using Docker with a multi-stage build.

---

## Introduction

In this post, we'll build a simple Spring Boot Hello World REST API and package it into a Docker image using a multi-stage Dockerfile. By the end, you'll have a running container serving your API on `http://localhost:8080/hello`.

Whether you're new to Docker or just want a clean reference setup, this guide walks you through every step.

---

## Tech Stack

| Technology  | Version |
|-------------|---------|
| Java        | 21      |
| Spring Boot | 4.0.6   |
| Maven       | 3.9     |
| Docker      | Latest  |

---

## Prerequisites

Before you begin, make sure you have the following installed:

- [Java 21+](https://adoptium.net/)
- [Maven 3.9+](https://maven.apache.org/)
- [Docker Desktop](https://www.docker.com/products/docker-desktop/)

---

## Project Structure

```
spring-boot-hello-world/
├── src/
│   └── main/
│       └── java/com/kainfosoft/
│           ├── controller/
│           │   └── HelloWorldController.java
│           └── SpringBootHelloWorldApplication.java
├── Dockerfile
├── pom.xml
└── README.md
```

---

## Step 1 — Create the Spring Boot Application

### pom.xml

The `pom.xml` defines the project dependencies and sets the JAR output name using `<finalName>`.

```xml
<?xml version="1.0" encoding="UTF-8"?>
<project xmlns="http://maven.apache.org/POM/4.0.0"
         xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
         xsi:schemaLocation="http://maven.apache.org/POM/4.0.0 https://maven.apache.org/xsd/maven-4.0.0.xsd">
    <modelVersion>4.0.0</modelVersion>

    <parent>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-starter-parent</artifactId>
        <version>4.0.6</version>
    </parent>

    <groupId>com.kainfosoft</groupId>
    <artifactId>spring-boot-hello-world</artifactId>
    <version>0.0.1-SNAPSHOT</version>

    <properties>
        <java.version>21</java.version>
    </properties>

    <dependencies>
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-webmvc</artifactId>
        </dependency>
    </dependencies>

    <build>
        <finalName>spring-boot-hello-world</finalName>
        <plugins>
            <plugin>
                <groupId>org.springframework.boot</groupId>
                <artifactId>spring-boot-maven-plugin</artifactId>
            </plugin>
        </plugins>
    </build>
</project>
```

> 💡 `<finalName>spring-boot-hello-world</finalName>` ensures the output JAR is named `spring-boot-hello-world.jar` instead of the default `artifactId-version.jar`.

---

### Main Application Class

```java
package com.kainfosoft;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class SpringBootHelloWorldApplication {
    public static void main(String[] args) {
        SpringApplication.run(SpringBootHelloWorldApplication.class, args);
    }
}
```

---

### REST Controller

```java
package com.kainfosoft.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HelloWorldController {

    @GetMapping("/hello")
    public String hello() {
        return "Welcome to Spring Boot!";
    }
}
```

This exposes a single GET endpoint at `/hello` that returns a plain text welcome message.

---

## Step 2 — Build and Run Locally

Before containerizing, verify the app works locally.

```bash
# Build the JAR (skipping tests)
mvn clean package -DskipTests

# Run the application
java -jar target/spring-boot-hello-world.jar
```

Test the endpoint:

```bash
curl http://localhost:8080/hello
```

Expected response:

```
Welcome to Spring Boot!
```

---

## Step 3 — Write the Dockerfile

We use a **multi-stage build** to keep the final image small:

- **Stage 1** — uses a Maven + JDK image to compile and package the app
- **Stage 2** — uses a slim JRE-only image to run the JAR

```dockerfile
# Stage 1: Build
FROM maven:3.9-eclipse-temurin-21 AS build
WORKDIR /app
COPY pom.xml .
COPY src ./src
RUN mvn clean package -DskipTests

# Stage 2: Run
FROM eclipse-temurin:21-jre
WORKDIR /app
COPY --from=build /app/target/spring-boot-hello-world.jar app.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "app.jar"]
```

> 💡 Multi-stage builds ensure the final image contains only the JRE and the JAR — no Maven, no source code, no build tools. This significantly reduces the image size.

---

## Step 4 — Build the Docker Image

> ⚠️ **Make sure Docker Desktop is running before executing any Docker command.**
> Verify by running:
> ```bash
> docker info
> ```
> If Docker Desktop is not running, you will see: `Cannot connect to the Docker daemon`.

You pass the image tag at build time using `-t`:

```bash
# Build with a specific version tag
docker build -t spring-boot-hello-world:1.0.0 .

# Build with latest tag
docker build -t spring-boot-hello-world:latest .

# Build with Docker Hub registry prefix
docker build -t <your-dockerhub-username>/spring-boot-hello-world:1.0.0 .
```

---

## Step 5 — Run the Container

```bash
# Run on port 8080
docker run -p 8080:8080 spring-boot-hello-world:1.0.0

# Run in detached (background) mode
docker run -d -p 8080:8080 --name hello-world spring-boot-hello-world:1.0.0
```

Test the running container:

```bash
curl http://localhost:8080/hello
```

Expected response:

```
Welcome to Spring Boot!
```

---

## Step 6 — Verify Image and Container

### Verify Image

```bash
# List all local images
docker images

# Filter by image name
docker images spring-boot-hello-world
```

### Verify Container

```bash
# List running containers
docker ps

# List all containers including stopped
docker ps -a
```

---

## Step 7 — Push to Docker Hub (Optional)

```bash
# Login to Docker Hub
docker login

# Push the image
docker push <your-dockerhub-username>/spring-boot-hello-world:1.0.0
```

---

## Step 8 — Stop and Clean Up

### Stop and Remove Container

```bash
# Stop the running container
docker stop hello-world

# Remove the stopped container
docker rm hello-world

# Stop and remove in one command
docker rm -f hello-world
```

### Remove Image

```bash
# Remove a specific image
docker rmi spring-boot-hello-world:1.0.0

# Remove all unused images
docker image prune -a
```

---

## API Reference

| Method | URL      | Response                  |
|--------|----------|---------------------------|
| GET    | `/hello` | `Welcome to Spring Boot!` |

---

## Summary

Here's what we covered in this post:

| Step | What We Did                                      |
|------|--------------------------------------------------|
| 1    | Created a Spring Boot REST API                   |
| 2    | Built and tested the app locally                 |
| 3    | Wrote a multi-stage Dockerfile                   |
| 4    | Built the Docker image with a custom tag         |
| 5    | Ran and tested the container                     |
| 6    | Verified image and container                     |
| 7    | Pushed the image to Docker Hub                   |
| 8    | Stopped and cleaned up containers and images     |

---

## Source Code

You can find the full source code on GitHub:

> 🔗 [github.com/kainfosoft/spring-boot-hello-world](https://github.com/kainfosoft/spring-boot-hello-world)

---

*Happy Coding! 🚀*
