# Auth Service

This service provides JWT-based authentication against the `PATIENT_MANAGEMENT_USER` table.

## Features

- JWT token generation with user ID and roles as claims
- Database-backed user authentication
- BCrypt password encoding
- Role-based authorization support

## Database Setup

1. Ensure your MySQL database is running
2. Execute the SQL script in `src/main/resources/db/init.sql` to create the required table
3. The script creates a sample user:
   - Username: `admin`
   - Password: `password123`
   - Roles: `ADMIN,USER`

## Configuration

The service is configured to:
- Run on port 8080
- Connect to MySQL database
- Use BCrypt password encoding
- Generate JWT tokens with 24-hour expiration
- Require authentication for all endpoints except:
  - `/actuator/**` (health checks)
  - `/health/**` (health endpoints)
  - `/auth` (authentication endpoint)

## API Endpoints

### Public Endpoints (No Authentication Required)
- `POST /auth` - Authenticate and get JWT token
- `GET /actuator/**` - Spring Boot Actuator endpoints
- `GET /health/**` - Health check endpoints

### Protected Endpoints (JWT Authentication Required)
- `GET /api/test/secured` - Secured test endpoint
- `POST /api/test/validate-token` - Validate JWT token

## Authentication Flow

### 1. Get JWT Token
```bash
curl -X POST http://localhost:8080/auth \
     -H "Content-Type: application/json" \
     -d '{"username":"admin","password":"password123"}'
```

**Response:**
```json
{
    "token": "eyJhbGciOiJIUzUxMiJ9...",
    "message": "Authentication successful"
}
```

### 2. Use JWT Token
```bash
curl -H "Authorization: Bearer eyJhbGciOiJIUzUxMiJ9..." \
     http://localhost:8080/api/test/secured
```

### 3. Validate Token
```bash
curl -X POST "http://localhost:8080/api/test/validate-token?token=eyJhbGciOiJIUzUxMiJ9..."
```

## JWT Token Claims

The generated JWT token contains:
- **Subject**: Username
- **userId**: User ID from database
- **roles**: Array of user roles
- **iat**: Issued at timestamp
- **exp**: Expiration timestamp

## Testing

### Using curl

1. **Authenticate and get token:**
   ```bash
   curl -X POST http://localhost:8080/auth \
        -H "Content-Type: application/json" \
        -d '{"username":"admin","password":"password123"}'
   ```

2. **Use the token to access secured endpoint:**
   ```bash
   curl -H "Authorization: Bearer YOUR_JWT_TOKEN" \
        http://localhost:8080/api/test/secured
   ```

3. **Validate the token:**
   ```bash
   curl -X POST "http://localhost:8080/api/test/validate-token?token=YOUR_JWT_TOKEN"
   ```

## Security Features

- **JWT Tokens**: Secure, stateless authentication
- **Password Encoding**: All passwords are encoded using BCrypt
- **Role-Based Access**: Support for multiple roles per user
- **Token Expiration**: Configurable token lifetime (default: 24 hours)
- **Secure Signing**: HMAC-SHA512 algorithm for token signing

## Troubleshooting

- Ensure the database connection is working
- Check that the `PATIENT_MANAGEMENT_USER` table exists
- Verify that the sample user was created successfully
- Check application logs for any authentication errors
- Ensure the JWT secret key is properly configured 