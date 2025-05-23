# service_aggregator

A lightweight Spring Boot REST API to **record** and **retrieve** vehicle service history based on VIN (Vehicle Identification Number).

This service uses an in-memory H2 database and exposes two endpoints: one for submitting service data and one for retrieving it.

---

## Tech Stack

- Java 17+
- Spring Boot
- Spring Data JPA
- H2 In-Memory Database
- Gradle

---

## API Endpoints

### 1. `POST /service`

Submit one or more service records. The service will validate each record and store only the valid ones. Invalid records are returned in the response.

#### Request Body (JSON Array)

Each object must follow this structure:

```json
{
  "vin": "1HGCM82633A123456",
  "service_date": "2023-05-12",
  "mileage": 50000,
  "service_type": "Oil Change",
  "service_cost": 49.99
}
```

### 2. `GET /service/{vin}`

Retrieves service records based on a given VIN, records sorted by service_date

returned objects follow this structure:

```json
{
  "id":1,
  "vin": "1HGCM82633A123456",
  "service_date": "2023-05-12",
  "mileage": 50000,
  "service_type": "Oil Change",
  "service_cost": 49.99
}
```

## Building the Docker Image

To build the Docker image for the `service_aggregator` app, run the following command:

```bash
docker build -t service_aggregator .
```

You can follow up with instructions on how to run the container, like this:

## Running the Docker Container
Once the image is built, run the container with:

```bash
docker run -p 8106:8106 service_aggregator
```
This will start the application and expose it on port 8106. You can then access the app at http://localhost:8106.

