
# 🚀 Compliance Training Manager

AI-powered web application for managing compliance training with automated insights and recommendations.

---

## 🧱 Tech Stack

- Backend: Spring Boot (Java 17)
- AI Service: Flask (Python)
- Frontend: React + Vite
- Database: PostgreSQL
- Cache: Redis
- AI: Groq (LLaMA 3.3)
- Containerization: Docker + Docker Compose

---

## ⚙️ Setup Instructions

### 1. Clone Repository
git clone <https://github.com/darshanhkdarshan223-create/compliance-training-manager.git>
cd your-project-folder

### 2. Setup Environment
cp .env.example .env

Add your GROQ_API_KEY inside `.env`

---

### 3. Run Project
docker-compose up --build

---

## 🌐 Access Services

| Service       | URL                         |
|--------------|----------------------------|
| Frontend     | http://localhost:3000      |
| Backend API  | http://localhost:8080      |
| Swagger      | http://localhost:8080/swagger-ui.html |
| AI Service   | http://localhost:5000/health |

---

## 🔄 Architecture Flow

Frontend (React)
↓
Backend (Spring Boot)
↓
AI Service (Flask)
↓
Groq API + ChromaDB

---

## 🔐 Features

- JWT Authentication
- Role-Based Access (ADMIN / MANAGER / VIEWER)
- AI Description Generation
- AI Recommendations
- Dashboard Analytics
- Email Notifications
- Redis Caching
- Scheduled Jobs

---

## 🐳 Run Commands

Start:
docker-compose up --build

Stop:
docker-compose down

Reset DB:
docker-compose down -v

---

## ⚠️ Important Notes

- Frontend NEVER calls AI directly
- Backend handles all AI communication
- Environment variables must not be hardcoded
- Use Docker for consistent environment

---

## 🎯 Demo Flow

1. Login
2. Create training record
3. AI generates description
4. View recommendations
5. Dashboard analytics
