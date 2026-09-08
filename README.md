# 💰 MoneyMantra – AI-Powered Personal Finance Tracker

MoneyMantra is a full-stack personal finance management application that helps users track income, expenses, categories, and overall financial performance.

The application also integrates **AI-powered financial insights** using **Spring AI and Google Gemini** to analyze spending patterns, savings, category-wise expenses, and month-over-month financial trends.

🔗 **Live Application:** https://moneymantra-v1.netlify.app

🔗 **Backend API:** https://moneymantra.onrender.com/api/v1.0/health

🔗 **GitHub Repository:** https://github.com/ashna-verma/moneymantra

---

## ✨ Features

### 💰 Personal Finance Management

* Track income and expenses
* Create and manage financial categories
* View total balance, income, and expenses
* Monitor recent income and expense transactions
* Analyze category-wise spending
* View financial summaries and dashboard analytics

### 📊 Dashboard & Analytics

* Total balance overview
* Income and expense summaries
* Recent transactions
* Recent income and expense tracking
* Financial overview visualizations
* Month-wise financial analysis

### 🤖 AI-Powered Financial Insights

MoneyMantra uses **Spring AI and Google Gemini** to generate personalized financial insights based on the user's financial activity.

The AI analyzes:

* Monthly income
* Monthly expenses
* Savings and savings rate
* Category-wise expenses
* Month-over-month income changes
* Month-over-month expense changes

The AI generates:

* 📋 Overall financial summary
* 📊 Personalized spending insights
* 💡 Practical budgeting recommendations

Example AI response:

```json
{
  "summary": "Your expenses are currently well below your income, resulting in a healthy savings rate this month.",
  "insights": [
    "Food is currently your highest spending category.",
    "Your monthly expenses increased compared to the previous month.",
    "You are maintaining a positive savings balance."
  ],
  "recommendation": "Consider setting a monthly budget for your highest spending category to maintain your savings rate."
}
```

### 🔐 Authentication & Security

* User registration and login
* JWT-based authentication
* Secured REST APIs
* Password encryption using Spring Security
* Protected frontend routes

### 📧 Reporting & Utilities

* Excel export for financial data
* Email-based financial reports
* Profile image upload
* Category filtering
* Transaction management

---

## 🛠️ Tech Stack

### Frontend

* React.js
* Vite
* Tailwind CSS
* React Router
* Axios
* Lucide React
* React Hot Toast

### Backend

* Java
* Spring Boot
* Spring Data JPA
* Spring Security
* Spring AI
* RESTful APIs

### AI

* Spring AI
* Google Gemini

### Database

* PostgreSQL

### Tools & Deployment

* Git & GitHub
* Postman
* Render
* Brevo Email API
* Cloudinary

---

## 🏗️ Architecture

```text
                        ┌─────────────────┐
                        │   React + Vite  │
                        │    Frontend     │
                        └────────┬────────┘
                                 │
                              REST APIs
                                 │
                        ┌────────▼────────┐
                        │   Spring Boot   │
                        │     Backend     │
                        └────────┬────────┘
                                 │
              ┌──────────────────┼──────────────────┐
              │                  │                  │
              ▼                  ▼                  ▼
        PostgreSQL          Google Gemini       External APIs
          Database            Spring AI       Email / Cloudinary
```

---

# 🤖 AI Financial Insights

The AI Insights feature analyzes user financial data before sending relevant information to the AI model.

### Financial Metrics Calculated

#### Savings

```text
Savings = Total Income - Total Expenses
```

#### Savings Rate

```text
Savings Rate = (Savings / Total Income) × 100
```

#### Month-over-Month Change

```text
Percentage Change =
((Current Month - Previous Month) / Previous Month) × 100
```

The calculated financial data is passed to the AI model, which generates contextual and personalized insights.


---

# 🚀 Getting Started

## Prerequisites

Make sure you have the following installed:

* Java 17 or later
* Node.js
* npm
* PostgreSQL
* Maven

You will also need API credentials for:

* Google Gemini
* Cloudinary
* Brevo (if using email functionality)

---

## 1️⃣ Clone the Repository

```bash
git clone https://github.com/ashna-verma/moneymantra.git
```

Navigate to the project:

```bash
cd moneymantra
```

---

# ⚙️ Backend Setup

Navigate to the backend directory:

```bash
cd backend
```

### Configure Environment Variables

Do **not** commit secrets to GitHub.

Configure the required environment variables:

```properties
SPRING_DATASOURCE_URL=
SPRING_DATASOURCE_USERNAME=
SPRING_DATASOURCE_PASSWORD=

JWT_SECRET=

GEMINI_API_KEY=

BREVO_API_KEY=

CLOUDINARY_CLOUD_NAME=
CLOUDINARY_API_KEY=
CLOUDINARY_API_SECRET=
```

Configure your application to use environment variables.

Example:

```properties
spring.datasource.url=${SPRING_DATASOURCE_URL}
spring.datasource.username=${SPRING_DATASOURCE_USERNAME}
spring.datasource.password=${SPRING_DATASOURCE_PASSWORD}

jwt.secret=${JWT_SECRET}
```

### Run the Backend

Using Maven:

```bash
mvn spring-boot:run
```

Or using the Maven Wrapper:

```bash
./mvnw spring-boot:run
```

The backend will run on:

```text
http://localhost:8080
```

API context path:

```text
/api/v1.0
```

Example:

```text
http://localhost:8080/api/v1.0
```

---

💻 Frontend Setup

Clone the frontend repository:

git clone https://github.com/ashna-verma/MoneyMantraWebApp.git

Install dependencies:

npm install

Start the development server:

npm run dev

The frontend will typically run on:

http://localhost:5173
---

# 🔗 Key API Endpoints

| Method | Endpoint       | Description                    |
| ------ | -------------- | ------------------------------ |
| POST   | `/register`    | Register a new user            |
| POST   | `/login`       | User authentication            |
| GET    | `/dashboard`   | Get dashboard financial data   |
| GET    | `/income`      | Get income records             |
| POST   | `/income`      | Add income                     |
| GET    | `/expense`     | Get expense records            |
| POST   | `/expense`     | Add expense                    |
| GET    | `/categories`  | Get categories                 |
| GET    | `/ai/test`     | Test AI integration            |
| GET    | `/ai/insights` | Generate AI financial insights |

> All endpoints are prefixed with `/api/v1.0`.

Example:

```text
GET /api/v1.0/ai/insights
```

---

# 📸 Screenshots

## Dashboard

<img width="1916" height="971" alt="image" src="https://github.com/user-attachments/assets/626baca9-5731-4ece-af3a-5e61d5625b4b" />


## Income Management

<img width="1917" height="967" alt="image" src="https://github.com/user-attachments/assets/c206853f-649d-4ac6-b4c9-9b9b14c3e4cf" />


## Expense Management

<img width="1917" height="965" alt="image" src="https://github.com/user-attachments/assets/cbe346da-65e1-45c6-be42-a04c5ef7c93c" />


## AI Financial Insights

<img width="1917" height="965" alt="image" src="https://github.com/user-attachments/assets/c257c15a-02b7-4660-840b-11c37af6e294" />

## Filters Component

<img width="1917" height="965" alt="image" src="https://github.com/user-attachments/assets/746ca563-641d-4e2a-af81-40d36143d306" />


# ☁️ Deployment

The backend is deployed as a **Spring Boot web service on Render**.

### Deployment Flow

```text
GitHub Repository
        ↓
Render
        ↓
Maven Build
        ↓
Spring Boot Application
        ↓
PostgreSQL Database
```

### Build Command

```bash
./mvnw clean package -DskipTests
```

### Start Command

```bash
java -jar target/moneymantra-0.0.1-SNAPSHOT.jar
```

### Production Configuration

Sensitive values such as database credentials and API keys are configured using environment variables and are not stored in the repository.

---

# 🔒 Security

The following files and secrets should never be committed:

```text
.env
application-local.properties
API keys
JWT secrets
Database passwords
Cloudinary credentials
Email service credentials
```

Example `.gitignore`:

```gitignore
# Build files
target/

# Environment files
.env
.env.*

# Application secrets
application-local.properties

# Node modules
node_modules/

# Build output
dist/
```

---

# 🎯 Key Technical Highlights

* Built a full-stack application using **React and Spring Boot**
* Designed and developed RESTful APIs
* Implemented **JWT authentication and Spring Security**
* Integrated **Spring AI with Google Gemini**
* Developed AI-powered financial analysis based on real user transaction data
* Implemented financial calculations for savings, savings rate, and month-over-month changes
* Built responsive financial dashboards using React and Tailwind CSS
* Integrated PostgreSQL using Spring Data JPA
* Implemented cloud deployment using Render
* Used environment variables to manage sensitive configuration
* Integrated third-party services for email and image management

---

# 🔮 Future Improvements

* AI-powered conversational financial assistant
* Monthly and yearly spending predictions
* Budget creation and tracking
* Spending anomaly detection
* Personalized savings goals
* Financial trend forecasting
* Automated monthly financial reports
* AI-generated visual insights
* Docker containerization
* CI/CD pipeline automation

---

# 👩‍💻 Author

**Ashna Verma**

Java Full Stack Developer | React | Spring Boot | PostgreSQL | AI Integration

🔗 GitHub: https://github.com/ashna-verma
🔗 LinkedIn: https://www.linkedin.com/in/ashna-verma-52b69b200/

---

⭐ If you found this project interesting, feel free to star the repository!
