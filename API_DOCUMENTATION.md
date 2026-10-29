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

`GET http://localhost:8080/api/bookings`

**Example response:**

```json
[
  {
    "id": 1,
    "roomReference": "ROOM-A",
    "organizer": "Sandeep Sarvesh",
    "startTime": "2026-11-02T10:00:00",
    "endTime": "2026-11-02T11:00:00",
    "status": "CONFIRMED"
  }
]
```

If no bookings exist, the response is `200 OK` with an empty array: `[]`.

## 3. Retrieve Bookings by Organizer

- **Method:** GET
- **Endpoint:** `/api/bookings/organizer/{organizerId}`
- **Purpose:** Retrieves bookings for the specified organizer.
- **Success status:** `200 OK`

**Example request:**

`GET http://localhost:8080/api/bookings/organizer/Sandeep%20Sarvesh`

**Example response:** A JSON array containing matching booking objects, using the fields shown in Section 2. If no bookings match, the response is `[]`.

## 4. Retrieve a Booking by ID

- **Method:** GET
- **Endpoint:** `/api/bookings/{id}`
- **Purpose:** Retrieves one booking by its unique ID.

**Example request:**

`GET http://localhost:8080/api/bookings/1`

**Success response:** `200 OK`, with one booking object.

**Example error request:**

`GET http://localhost:8080/api/bookings/99999`

**Not-found response:** `404 Not Found` when that booking ID does not exist.

## 5. Booking Response Fields

- `id`: Unique booking identifier.
- `roomReference`: Meeting room reference.
- `organizer`: Organizer name or identifier.
- `startTime`: Booking start date and time.
- `endTime`: Booking end date and time.
- `status`: Booking status.

## 6. Testing

The endpoints were manually tested using Postman.

Automated tests were executed using Maven:

`mvn clean test`

**Recorded result:** BUILD SUCCESS — 6 tests, 0 failures, 0 errors.

The automated tests cover successful retrieval, an empty booking list, organizer-based retrieval, retrieval of an existing booking, and the not-found response for a missing booking.

## 7. Technologies Used

- Java 21
- Spring Boot
- Maven
- PostgreSQL
- pgAdmin 4
- Postman
- JUnit 5
- Spring MockMvc