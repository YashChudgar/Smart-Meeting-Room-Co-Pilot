# Smart Meeting Room Booking API Documentation

## 1. Project Overview

This API retrieves meeting room bookings from a PostgreSQL database using Spring Boot.

**Base URL:** `http://localhost:8080`

## 2. Retrieve All Bookings

- **Method:** GET
- **Endpoint:** `/api/bookings`
- **Purpose:** Retrieves all existing bookings.
- **Success status:** `200 OK`

**Example request:**

```http
GET http://localhost:8080/api/bookings
```

**Example response:**

```json
[
  {
    "id": 1,
    "roomReference": "ROOM-A",
    "organizer": "Sandeep Sarvesh",
    "startTime": "2026-11-05T10:00:00",
    "endTime": "2026-11-05T11:00:00",
    "status": "CONFIRMED"
  }
]
```

If no bookings exist, the API returns `200 OK` with an empty array: `[]`.

## 3. Retrieve Bookings by Organizer

- **Method:** GET
- **Endpoint:** `/api/bookings/organizer/{organizerId}`
- **Purpose:** Retrieves bookings for the specified organizer.
- **Success status:** `200 OK`

**Example request:**

```http
GET http://localhost:8080/api/bookings/organizer/Sandeep%20Sarvesh
```

**Example response:** A JSON array containing matching booking objects, using the fields shown in Section 2.

If no bookings match the organizer, the API returns `200 OK` with `[]`.

## 4. Retrieve a Booking by ID

- **Method:** GET
- **Endpoint:** `/api/bookings/{id}`
- **Purpose:** Retrieves one booking using its unique ID.

**Example request:**

```http
GET http://localhost:8080/api/bookings/1
```

**Success response:** `200 OK`, with one booking object.

**Example error request:**

```http
GET http://localhost:8080/api/bookings/99999
```

**Not-found response:** `404 Not Found` when the booking ID does not exist.

## 5. Booking Response Fields

| Field | Description |
|---|---|
| `id` | Unique booking identifier |
| `roomReference` | Meeting room reference |
| `organizer` | Organizer name or identifier |
| `startTime` | Booking start date and time |
| `endTime` | Booking end date and time |
| `status` | Booking status |

## 6. Testing

The API endpoints were manually tested using Postman.

Automated tests were executed using Maven:

```bash
mvn clean test
```

**Recorded test result:**

- Tests run: 4
- Failures: 0
- Errors: 0
- Skipped: 0
- Build result: BUILD SUCCESS

The automated tests verify booking retrieval scenarios using JUnit 5, Mockito and Spring MockMvc.

## 7. Technologies Used

- Java 21
- Spring Boot
- Maven
- PostgreSQL
- pgAdmin 4
- Postman
- JUnit 5
- Mockito
- Spring MockMvc