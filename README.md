# MySawit Backend - Manajemen Kebun/Lahan 🌴

This is the Spring Boot backend service for the **MySawit** application, specifically handling the `Kebun` (Lahan) management module. It uses a PostgreSQL database and integrates with a Next.js frontend.

## 🚀 Prerequisites
To run this project locally, ensure you have the following installed:
1. Java Development Kit (JDK) 21
2. PostgreSQL (or a cloud provider like Neon DB)
3. Git

## ⚙️ Local Setup & Installation

**1. Clone the repository**
```bash
git clone https://github.com/KEL-ADPRO/MySawit-backend-manage-lahan.git
cd MySawit-backend-manage-lahan
```

**2. Setup Environment Variables**
Copy the example environment file to create your local `.env` file:
```bash
cp .env.example .env
```
*Update the newly created `.env` file with your active PostgreSQL URL, Username, and Password.*
- Go to or create `src/main/resources/application.properties` and paste
```bash
server.port=8082

spring.datasource.url=${NEON_DB_URL}

spring.datasource.username=${NEON_DB_USERNAME}
spring.datasource.password=${NEON_DB_PASSWORD}

spring.datasource.driver-class-name=org.postgresql.Driver
spring.jpa.hibernate.ddl-auto=update

spring.jpa.show-sql=true
spring.jpa.properties.hibernate.format_sql=true

spring.jpa.properties.hibernate.dialect=org.hibernate.dialect.PostgreSQLDialect

spring.jpa.hibernate.naming.physical-strategy=org.hibernate.boot.model.naming.PhysicalNamingStrategyStandardImpl
```

**3. Run the Application (via IntelliJ IDEA)**
- Click the three dots beside the Run button at the top right and select **Edit Configurations**.
- Click on the blue **Modify options** text, navigate to the **Operating System** section, then enable **Environment variables**.
- An environment variable input field should now be available.
- On the right of the input field, click on the folder/document icon and select your `.env` file. (Alternatively, if you use the EnvFile plugin, check "Enable EnvFile" and add it there).
- Start the Spring Boot server. The backend will start running on `http://localhost:8082`.

**4. Run the FrontEnd**\
*Note: This requires the separate frontend repository.*
Navigate to your frontend project directory in a separate terminal and start the Next.js server:
```bash
npm install
npm run dev
```
The application interface will be available at `http://localhost:3000/kebun`

## 📡 API Endpoints
Currently implemented endpoints for the Kebun module:

| Method | Endpoint | Description | Request Body |
| :--- | :--- | :--- | :--- |
| `GET` | `/api/kebun` | Fetch a list of all Kebun | N/A |
| `GET` | `/api/kebun/{id}` | Fetch a specific Kebun by its UUID | N/A |
| `GET` | `/api/kebun/name/{name}` | Fetch a specific Kebun by its exact name | N/A |
| `POST` | `/api/kebun` | Create a new Kebun entry | `{ "nama": "string", "luas": number, "area": { ... } }` |
| `PUT` | `/api/kebun/{id}` | Update an existing Kebun | `{ "nama": "string", "luas": number, "area": { ... } }` |
| `DELETE` | `/api/kebun/{id}` | Delete a Kebun by ID | N/A |
| `PATCH` | `/api/kebun/{kebunId}/mandor/{mandorId}` | Assign a Mandor to a specific Kebun | N/A |
| `PATCH` | `/api/kebun/{kebunId}/supir/{supirId}` | Assign a Supir Truk to a specific Kebun | N/A |
| `DELETE` | `/api/kebun/{kebunId}/supir/{supirId}` | Remove a specific Supir Truk from a Kebun | N/A |
| `GET` | `/api/kebun/check-mandor/{mandorId}` | Check if a Mandor is currently assigned to any Kebun | N/A |
| `GET` | `/api/kebun/check-supir/{supirId}` | Check if a Supir Truk is currently assigned to any Kebun | N/A |

*(Note: CORS is configured to allow requests from the frontend running on `http://localhost:3000`)*