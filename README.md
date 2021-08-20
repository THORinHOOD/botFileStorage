### Build docker image

docker build --build-arg JAR_FILE=build/libs/benchBot-0.0.1.jar -t thorinhood/benchbot .

### Run docker container

docker run -d -p 8080:8080 -v <path to logs in machine>:/logs thorinhood/benchbot