# Smart-Meeting-Room-Co-Pilot-
An automated room coordination platform enforces strict backend rules by inserting 15-minute meeting buffers, optimizing layouts through group capacity matching, and using a cron daemon to auto-cancel bookings if the host fails to check in within 15 minutes.

## Local Development Setup

### Prerequisites

Before running the backend, make sure you have:

* Java 21
* Maven (or use the included Maven Wrapper)
* PostgreSQL
* Git
* VS Code or another Java-compatible IDE

### 1. Clone the Repository

Clone the repository and switch to the `staging` branch:

```bash
git clone https://github.com/YashChudgar/Smart-Meeting-Room-Co-Pilot.git
cd Smart-Meeting-Room-Co-Pilot
git checkout staging
```

### 2. Create the PostgreSQL Database

Open PostgreSQL/pgAdmin and create a database named:

```text
smart_meeting_room
```

The application uses the local PostgreSQL server on port `5432`.

### 3. Configure Local Environment Variables

Create a `.env` file in the root of the project:

```text
Smart Meeting Room/
├── .env
├── .gitignore
├── pom.xml
└── src/
```

Add the following:

```env
DB_URL=jdbc:postgresql://localhost:5432/smart_meeting_room
DB_USERNAME=postgres
DB_PASSWORD=YOUR_POSTGRES_PASSWORD
```

Replace `YOUR_POSTGRES_PASSWORD` with the password for the local PostgreSQL user.

**Do not commit the `.env` file to GitHub.**

The `.env` file is already excluded through `.gitignore`.

### 4. Run the Application

Using the Maven Wrapper on Windows:

```powershell
.\mvnw.cmd spring-boot:run
```

The backend should start on:

```text
http://localhost:8080
```

### 5. Test the Backend

Open:

```text
http://localhost:8080/api/health
```

A successful response should be:

```text
Smart Meeting Room backend is running!
```

### 6. Build the Project

To verify that the Maven build succeeds:

```powershell
.\mvnw.cmd clean package
```

A successful build should finish with:

```text
BUILD SUCCESS
```

### Important Notes

* Each team member should use their own local PostgreSQL installation and credentials.
* Do not commit database passwords or other secrets to GitHub.
* Use the `staging` branch for integration.
* Feature branches should be created from `staging`.
* The `main` branch should remain stable.
