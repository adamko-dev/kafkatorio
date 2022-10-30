# Running Kafkatorio

### Building the project

#### Requirements

* Docker
* Docker Compose
* Java 11

Kafkatorio uses [Gradle](https://docs.gradle.org/current/userguide/getting_started.html) to
build the project, and Docker Compose to create images.

* `./gradlew build` - build and test the project
* `./gradlew runKafkatorio` - run locally (if Docker is running, a Kafka instance will start)
* `./gradlew dockerComposeBuild` - build, test, and create Kafkatorio Docker images

### Server set up

> See [./kafkatorio-platform/docker-compose.yml](kafkatorio-platform/docker-compose.yml) for a
> complete example

1. Copy the [kafkatorio-platform](./kafkatorio-platform) directory to your computer
2. Rename
   [`example.secret.kafkatorio-config.yml`](./kafkatorio-platform/example.secret.kafkatorio-config.yml)
   to `.secret.kafkatorio-config.yml`

   In it, define both
    * a JWT secret password (a long alphanumeric string is best, you won't need to type this
      password manually)
    * a Factorio server, with a short-ID and a description.
3. Rename [`example.env`](./kafkatorio-platform/example.env) to `.env`
4. Create a JWT for your Factorio server, by going to https://jwt.io/

    * In 'PAYLOAD: DATA', set `sub` to your Factorio server's short ID.
      ```json
      {
        "sub": "<your server short ID>",
        "iat": ...
      }
      ```
      (`iat`, 'issued-at-time', should update automatically to be the current time.)
    * Replace `your-256-bit-secret` with your JWT secret password

   Copy the 'Encoded' token (it should start with `ey`).

   In the `.env` file, set `KAFKATORIO_TOKEN` to equal the token.
5. In `kafkatorio-platform`, start the docker containers

   ```terminal
   docker-compose up -d; docker-compose logs -f
   ```
