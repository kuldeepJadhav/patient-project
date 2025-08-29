# 🏥 Patient Management System

A comprehensive microservices-based patient management system built with Spring Boot, featuring authentication, patient data management, and cohort analysis capabilities.

## 🏗️ Architecture Overview

The system follows a microservices architecture with the following components:

- **API Gateway Service** - Central entry point with authentication and routing
- **Auth Service** - JWT-based authentication and user management
- **Patient Backend Service** - Core patient data management and cohort operations
- **MySQL Database** - Persistent data storage

## 🚀 Quick Start

### Prerequisites
- Java 17+
- Maven 3.6+
- Podman & Podman Compose
- MySQL 8.0+

### Option 1: Podman Compose (Recommended)
```bash
# Clone the repository
git clone <repository-url>
cd patient-project
mvn clean install
# Start all services
podman-compose up -d
```


## 📋 Service Details

### 1. 🐬 MySQL Database
- **Port**: 3306
- **Database**: `healthdb`
- **Credentials**: 
  - Username: `root`
  - Password: `root`
- **Purpose**: Persistent data storage for all services

### 2. 🏥 Patient Backend Service
- **Port**: 9092
- **Technology**: Spring Boot with Spring Data JPA
- **Database**: MySQL with Liquibase migrations
- **Features**:
  - Patient CRUD operations
  - Patient visit management
  - Cohort creation and management
  - Advanced filtering and search capabilities

#### Key Components:
- **Models**: Patient, PatientVisit, Cohort, BloodGroup, Gender, Status
- **Services**: PatientService, CohortService, PatientVisitService
- **Repositories**: PatientRepository, CohortRepository, PatientVisitRepository
- **Controllers**: PatientController, CohortController

### 3. 🔐 Auth Service
- **Port**: 9094
- **Technology**: Spring Boot with Spring Security
- **Features**:
  - JWT token generation and validation
  - User authentication
  - Role-based access control

#### Key Components:
- **Models**: User, Role
- **Services**: JwtService, CustomUserDetailsService
- **Controllers**: AuthController
- **Security**: JwtAuthenticationFilter, SecurityConfig

### 4. 🌐 API Gateway Service
- **Port**: 8081
- **Technology**: Spring Cloud Gateway
- **Features**:
  - Route management
  - Authentication middleware
  - Request/response transformation
  - Load balancing

#### Key Components:
- **Routes**: Auth routes, API routes, Test routes
- **Filters**: Token validation, path rewriting
- **Services**: AuthService for token validation

## 🌐 API Endpoints

### Gateway Service (Main Entry Point)
All external requests should go through the gateway at `http://localhost:8081`

#### Authentication Endpoints
```
POST /gateway/auth
Body: {"username": "string", "password": "string"}
Response: {"token": "jwt_token", "message": "string"}
```

#### Patient Management Endpoints
```
GET    /gateway/api/patients              # Get all patients
GET    /gateway/api/patients/{id}         # Get patient by ID
POST   /gateway/api/patients              # Create new patient
PUT    /gateway/api/patients/{id}         # Update patient
DELETE /gateway/api/patients/{id}         # Delete patient
```

#### Cohort Management Endpoints
```
GET    /gateway/api/cohorts               # Get all cohorts
GET    /gateway/api/cohorts/{id}          # Get cohort by ID
POST   /gateway/api/cohorts               # Create new cohort
PUT    /gateway/api/cohorts/{id}          # Update cohort
DELETE /gateway/api/cohorts/{id}          # Delete cohort
```

#### Patient Visit Endpoints
```
GET    /gateway/api/visits                # Get all visits
GET    /gateway/api/visits/{id}           # Get visit by ID
POST   /gateway/api/visits                # Create new visit
PUT    /gateway/api/visits/{id}           # Update visit
DELETE /gateway/api/visits/{id}           # Delete visit
```

### Direct Service Access (Development Only)

#### Patient Backend Service
```
GET    /actuator/health                   # Health check
GET    /api/patients                      # Patient endpoints
GET    /api/cohorts                       # Cohort endpoints
GET    /api/visits                        # Visit endpoints
```

#### Auth Service
```
GET    /actuator/health                   # Health check
POST   /auth                              # Authentication
GET    /auth/verify                       # Token verification
```

## 🔐 Authentication Flow

### 1. User Login
```bash
curl -X POST http://localhost:8081/gateway/auth \
  -H "Content-Type: application/json" \
  -d '{"username": "admin", "password": "password"}'
```

### 2. Use JWT Token
```bash
curl -X GET http://localhost:8081/gateway/api/patients \
  -H "Authorization: Bearer <jwt_token>"
```

## 🖥️ UI Workflow & Middleware Authentication

The system includes a UI-Middleware service that handles authentication between the frontend UI and backend services, providing a secure and seamless user experience.

### Authentication Flow Architecture

```
[UI] ←→ [UI-Middleware Service] ←→ [Auth Service]
         ↓
[Patient Backend Service]
```

### Detailed Workflow

#### 1. **Initial Authentication**
- **UI** sends login credentials to **UI-Middleware Service**
- **UI-Middleware Service** forwards credentials to **Auth Service**
- **Auth Service** validates credentials and returns JWT token
- **UI-Middleware Service** stores JWT token in an HTTP-only cookie
- **UI** receives authentication success response

#### 2. **Subsequent Requests**
- **UI** sends requests to **UI-Middleware Service** (JWT automatically included in cookies)
- **UI-Middleware Service** intercepts the request and extracts JWT from cookie
- **UI-Middleware Service** sends JWT to **Auth Service** for verification
- **Auth Service** validates the JWT token and returns verification result
- **UI-Middleware Service** forwards the request to **Patient Backend Service** if verification successful
- **Patient Backend Service** processes the request and returns response
- **UI-Middleware Service** forwards the response back to **UI**

### Security Features

- **HTTP-Only Cookies**: JWT tokens are stored in secure, HTTP-only cookies to prevent XSS attacks
- **Automatic Token Management**: UI doesn't need to manually handle JWT storage or transmission
- **Centralized Authentication**: All authentication logic is centralized in the middleware service
- **Seamless User Experience**: Users remain authenticated across browser sessions

### Cookie Configuration

```javascript
// Example cookie configuration in UI-Middleware Service
{
  httpOnly: true,           // Prevents JavaScript access
  secure: true,             // HTTPS only in production
  sameSite: 'strict',       // CSRF protection
  maxAge: 24 * 60 * 60 * 1000  // 24 hours expiration
}
```

### Error Handling

- **Invalid/Expired JWT**: User is redirected to login page
- **Authentication Failure**: Clear error messages are returned to UI
- **Network Issues**: Graceful fallback and retry mechanisms

### Benefits

1. **Enhanced Security**: JWT tokens are never exposed to client-side JavaScript
2. **Simplified UI**: Frontend doesn't need to implement complex token management
3. **Centralized Control**: Authentication logic is managed in one place
4. **Better User Experience**: Seamless authentication without manual token handling
5. **CSRF Protection**: Built-in protection against cross-site request forgery

## 🎨 UI Screens & User Interface

The system provides an intuitive and feature-rich user interface designed for healthcare professionals to efficiently manage patient data and cohort analysis.

### 1. 🔐 Login Screen

#### Features
- **Username/Password Fields**: Secure authentication input
- **Remember Me Option**: Optional persistent login
- **Forgot Password**: Password recovery functionality
- **Error Handling**: Clear validation messages



### 2. 🏠 Dashboard (ROLE_MANAGER)

#### Main Dashboard View
- **Welcome Header**: Personalized greeting with user information
- **Quick Stats**: Overview of key metrics (total patients, active cohorts, recent visits)
- **Navigation Tabs**: Easy access to different sections

#### Cohort Queries List
- **Saved Queries Table**: List of all saved cohort queries
- **Query Information**: Name, description, creation date, last used
- **Quick Actions**: View, edit, delete, and execute queries
- **Search & Filter**: Find specific queries quickly
- **Pagination**: Handle large numbers of queries efficiently

#### Cohort Query Details
- **Query Results**: Patient list matching the cohort criteria
- **Export Options**: Download results in various formats (CSV, PDF, Excel)
- **Query Statistics**: Count of matching patients, demographic breakdown
- **Modify & Re-run**: Edit query criteria and execute again

### 3. 👥 Patient Listing Tab

#### Patient Management Interface
- **Patient Table**: Comprehensive list of all patients
- **Sortable Columns**: Sort by name, date of birth, status, etc.
- **Advanced Filtering**: Filter by demographics, medical conditions, visit history
- **Search Functionality**: Global search across patient records

#### Individual Patient Row
- **Patient Information**: Name, age, gender, contact details
- **Status Indicators**: Visual status representation (active, inactive, deceased)
- **Quick Action Button**: "View Visits" button for immediate access
- **Row Actions**: Edit, delete, and view detailed patient information

#### Patient Visit Quick View
- **Visit Summary**: Recent visits with key information
- **Visit Details**: Date, type, diagnosis, treatment, status
- **Quick Actions**: Schedule new visit, update existing visit
- **Medical History**: Timeline of patient's medical journey

### 4. 🔍 Search & Filtering

#### Global Search
- **Smart Search**: Search across patients, cohorts, and visits
- **Auto-complete**: Intelligent suggestions as you type
- **Search History**: Remember recent searches
- **Saved Searches**: Save frequently used search criteria



### 5. ➕ Create New Cohort Query

#### Query Creation Modal
- **Field Selection**: All available patient fields for filtering
- **Preview Results**: See how many patients match current criteria



#### Query Actions
- **Save Query**: Save for future use with name and description
- **Save & Search**: Save query and immediately execute
- **Search Only**: Execute query without saving
- **Clear Form**: Reset all filter criteria
- **Load Template**: Use pre-defined query templates



## 🧪 Testing

### Running Tests

#### Unit Tests
```bash
# Patient Backend Service
cd patient-backend-service
mvn test

# Auth Service
cd auth-service
mvn test

# Gateway Service
cd gateway-service
mvn test
```

#### Integration Tests
```bash
# Patient Backend Service Integration Tests
cd patient-backend-service
mvn test -Dtest=PatientIntegrationTest

# Auth Service Integration Tests
cd auth-service
mvn test -Dtest=AuthControllerIntegrationTest
```


### Logs and Debugging
```bash
# View all service logs
podman-compose logs -f

# View specific service logs
podman-compose logs -f patient-backend-service
podman-compose logs -f auth-service
podman-compose logs -f gateway-service

# Check service status
podman-compose ps
```

## 🔄 Development Workflow

### Making Changes
1. Stop the specific service: `podman-compose stop <service-name>`
2. Rebuild: `podman-compose build <service-name>`
3. Start: `podman-compose up -d <service-name>`

### Code Changes
1. Modify source code
2. Run tests: `mvn test`
3. Build: `mvn clean install`
4. Restart service

## 📚 Additional Resources





