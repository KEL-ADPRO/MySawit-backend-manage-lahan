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
| `POST` | `/api/kebun` | Create a new Kebun entry | `{ "nama": "string" }` |


*(Note: CORS is configured to allow requests from the frontend running on `http://localhost:3000`)*