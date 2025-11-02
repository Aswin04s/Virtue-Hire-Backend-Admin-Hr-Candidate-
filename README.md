# VirtueHire - Smart Recruitment Platform Backend

## 📋 Project Description

VirtueHire is a comprehensive recruitment solution that connects HR professionals with pre-assessed candidates. This repository contains the backend API built with Spring Boot that powers the entire recruitment ecosystem.

## 🚀 Features

### 🔐 Authentication & Authorization
- **Multi-role system** (Admin, HR, Candidate)
- **Session-based authentication**
- **Role-based access control**

### 👥 Candidate Management
- **Registration with file uploads** (Resume, Profile Picture, ID Card)
- **Multi-level assessment system** (Easy → Medium → Hard)
- **Skill-based candidate profiling**
- **Admin verification system** for college credentials
- **Badge awarding** for completing assessments

### 💼 HR Professional Features
- **Verified HR registration** with ID proof upload
- **Flexible subscription plans** (Single, 10 Candidates, Monthly Unlimited)
- **Advanced candidate search** with filtering by skills, experience, and scores
- **Candidate profile access control**

### ⚙️ Admin Dashboard
- **Complete platform oversight**
- **Candidate and HR verification**
- **Question bank management** with subject-based organization
- **Payment monitoring and revenue tracking**
- **System analytics and reporting**

### 💳 Payment Integration
- **Multiple subscription tiers**
- **Payment status tracking**
- **Revenue analytics**

## 🛠️ Technology Stack

- **Framework**: Spring Boot 3.x
- **Database**: H2 (Development) / MySQL (Production ready)
- **Template Engine**: Thymeleaf
- **Security**: Spring Security
- **File Handling**: Multipart file uploads with size validation
- **Build Tool**: Maven

## 📁 Project Structure

```
src/
├── main/
│   ├── java/com/virtuehire/
│   │   ├── controller/     # MVC Controllers
│   │   ├── model/          # Entity classes
│   │   ├── repository/     # Data access layer
│   │   ├── service/        # Business logic
│   │   └── config/         # Configuration classes
│   └── resources/
│       ├── templates/      # Thymeleaf templates
│       ├── static/         # CSS, JS, images
│       └── application.properties
```

## 🚀 Quick Start

### Prerequisites
- Java 17 or higher
- Maven 3.6+
- Git

### Installation & Setup

1. **Clone the repository**
   ```bash
   git clone https://github.com/your-username/virtuehire-backend.git
   cd virtuehire-backend
   ```

2. **Configure application properties**
   ```properties
   # Database configuration
   spring.datasource.url=jdbc:h2:file:./data/virtuehire
   spring.datasource.driverClassName=org.h2.Driver
   spring.datasource.username=sa
   spring.datasource.password=password
   
   # File upload settings
   spring.servlet.multipart.max-file-size=10MB
   spring.servlet.multipart.max-request-size=10MB
   
   # H2 Console (for development)
   spring.h2.console.enabled=true
   ```

3. **Build and run the application**
   ```bash
   mvn clean install
   mvn spring-boot:run
   ```

4. **Access the application**
   - Main Application: http://localhost:8080
   - H2 Database Console: http://localhost:8080/h2-console
     - JDBC URL: `jdbc:h2:file:./data/virtuehire`
     - Username: `sa`
     - Password: `password`

## 👤 Default Access

### Admin Access
- **URL**: `/admin/dashboard`
- **Note**: First run will create default admin through DataLoader

### HR Registration
- **URL**: `/hrs/register`

### Candidate Registration  
- **URL**: `/candidates/register`

## 📊 Key Endpoints

| Role | Endpoint | Description |
|------|----------|-------------|
| Admin | `/admin/dashboard` | Admin overview |
| Admin | `/admin/questions` | Question bank management |
| Admin | `/admin/hrs` | HR management |
| Admin | `/admin/candidates` | Candidate management |
| HR | `/hrs/dashboard` | HR dashboard |
| HR | `/hrs/candidates` | Browse candidates |
| Candidate | `/candidates/login` | Candidate login |
| Candidate | `/assessment` | Take assessments |

## 🔧 Configuration

### File Upload Settings
- **Max Resume Size**: 5MB
- **Max Profile Picture**: 2MB  
- **Max ID Card Size**: 5MB
- **Upload Directory**: `./uploads/`

### Database
- **Development**: H2 file-based database
- **Production**: MySQL/PostgreSQL ready

## 🎯 Core Entities

- **Candidate**: Job seekers with skills and assessments
- **HR**: Recruitment professionals with subscription plans
- **Question**: Assessment questions organized by subject and level
- **Payment**: Subscription and payment records
- **AssessmentResult**: Candidate test scores and progress

## 📈 Assessment System

- **Three difficulty levels**: Easy, Medium, Hard
- **Subject-based questions**: Java, React, etc.
- **Progressive unlocking**: Must pass current level to advance
- **Score tracking**: Percentage-based scoring
- **Badge system**: Awards for completion

## 💳 Subscription Model

| Plan | Candidate Access | Duration |
|------|------------------|----------|
| Single | 1 candidate | One-time |
| 10 Candidates | 10 candidates | One-time |
| Monthly Unlimited | Unlimited | 30 days |

## 🐛 Troubleshooting

### Common Issues

1. **File Upload Errors**
   - Check `spring.servlet.multipart` settings
   - Verify upload directory permissions

2. **Database Connection**
   - Ensure H2 database file is not locked
   - Check JDBC URL in application.properties

3. **Template Errors**
   - Verify Thymeleaf dependencies
   - Check template file paths

### Logs
- Application logs are available in console
- H2 database logs: `./data/virtuehire.mv.db`

## 🤝 Contributing

1. Fork the repository
2. Create a feature branch (`git checkout -b feature/AmazingFeature`)
3. Commit your changes (`git commit -m 'Add some AmazingFeature'`)
4. Push to the branch (`git push origin feature/AmazingFeature`)
5. Open a Pull Request
