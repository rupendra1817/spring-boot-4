# Spring Boot Hello World

A minimal Spring Boot REST API that returns a welcome message.

---

## Tech Stack

| Technology     | Version |
|----------------|---------|
| Java           | 21      |
| Spring Boot    | 4.0.6   |
| Maven          | 3.9     |
| Docker         | Latest  |

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

## API Endpoint

| Method | URL             | Response                  |
|--------|-----------------|---------------------------|
| GET    | `/hello`        | `Welcome to Spring Boot!` |

---

## Run Locally

### Prerequisites
- Java 21+
- Maven 3.9+

### Build & Run

```bash
# Clean and build JAR
mvn clean package -DskipTests

# Run the application
java -jar target/spring-boot-hello-world.jar
```

App will start at: `http://localhost:8080`

---

## Docker

### Prerequisites
- Docker installed and running

> ⚠️ **Before running any Docker command, make sure Docker Desktop is running on your local machine.**
> You can verify by running:
> ```bash
> docker info
> ```
> If Docker Desktop is not running, you will see an error like `Cannot connect to the Docker daemon`.

### Build Image

```bash
# Build with a specific tag
docker build -t spring-boot-hello-world:1.0.0 .

# Build with latest tag
docker build -t spring-boot-hello-world:latest .

# Build with custom registry and tag (e.g. Docker Hub)
docker build -t <your-dockerhub-username>/spring-boot-hello-world:1.0.0 .

# Example with Docker Hub username
docker build -t rupendra1817/spring-boot-hello-world:1.0.0 .
```

### Run Container

```bash
# Run on default port 8080
docker run -p 8080:8080 spring-boot-hello-world:1.0.0

# Run in detached mode
docker run -d -p 8080:8080 --name spring-boot-hello-world spring-boot-hello-world:1.0.0

# Example with Docker Hub image
docker run -p 8080:8080 --name spring-boot-hello-world rupendra1817/spring-boot-hello-world:1.0.0

# Example with Docker Hub image in detached mode
docker run -d -p 8080:8080 --name spring-boot-hello-world rupendra1817/spring-boot-hello-world:1.0.0
```

> 💡 **Note:** If the image is not available locally, Docker will automatically pull it from Docker Hub.

### Push to Registry

```bash
# Login to Docker Hub
docker login

# Push image
docker push <your-dockerhub-username>/spring-boot-hello-world:1.0.0

# Example with Docker Hub username
docker push rupendra1817/spring-boot-hello-world:1.0.0
```

### Verify Image

```bash
# List all images
docker images

# Check specific image
docker images spring-boot-hello-world
```

### Verify Container Running

```bash
# List running containers
docker ps

# List all containers (including stopped)
docker ps -a
```

### Stop and Remove Container

```bash
# Stop running container
docker stop spring-boot-hello-world

# Remove stopped container
docker rm spring-boot-hello-world

# Stop and remove in one command
docker rm -f spring-boot-hello-world
```

### Remove Image

```bash
# Remove specific image
docker rmi spring-boot-hello-world:1.0.0

# Remove all unused images
docker image prune -a
```

---

## Test the API

```bash
curl http://localhost:8080/hello
```

Expected response:
```
Welcome to Spring Boot!
```
