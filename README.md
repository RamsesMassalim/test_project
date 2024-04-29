# Test project

## Installing

To install project, follow these steps:

git clone https://github.com/RamsesMassalim/test_project
cd test-project

## Configuring the Service

Describe any configuration files and how to modify them, if necessary. For example:

1. Open the `application.properties` file located in `src/main/resources`.
2. Update the properties according to your environment (PostgresSql and Cassandra)

## Running

After installing, you can run the service using the following steps:

./gradlew bootRun

## Running with Docker

This application can be run using Docker:

**Build and Run the Application**

Firstly build the project

./gradlew build

Then run application with docker

docker-compose up --build

## Using project

Open your browser and navigate to `http://localhost:8080`.

## Api documentation

For more details about API, see the Swagger documentation [here](./openApi.yaml) of [here](https://app.swaggerhub.com/apis/romi2000/testProject/1.0.0#/).
