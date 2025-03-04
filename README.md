# TwilioSMS System Design

## Project Description

TwilioSMS is a robust SMS management system that integrates with Twilio's messaging services to provide a comprehensive platform for sending and managing SMS messages. The system offers user authentication, message tracking, and verification code services through a well-structured API architecture.


## Core Components

- **API Layer**
  - UserAPI
  - TwilioAPI
  - MessageAPI
  - VerificationCodeAPI
- **Service Layer**
  - UserService
  - TwilioService
  - MessageService
  - VerificationCodeService
- **DAO Layer**
  - UserDao
  - TwilioDao
  - MessageDao
  - VerificationCodeDao
- **Data Models**
  - User
  - Twilio
  - OutboundMsg
  - VerificationCode

## Database Setup

- Database creation script: `/create_database.sql`
- Set the database connection details in the `src/main/resources/hibernate.cfg.xml` file

## API Specifications

### General Requirements

- All JSON attributes must match database column names
- Complete object representations in API requests/responses
- Session management requirements:

```javascript
// Example request with credentials
fetch('/sms/api/', {
  method: 'POST',
  headers: { 'Content-Type': 'application/json' },
  body: JSON.stringify({}),
  credentials: 'include'
});
```

**Note:** 
- Always check session expiry/login state at the beginning of admin/customer pages using this code sample:

```javascript
fetch('/sms/api/user/checkSession', {
    method: 'GET',
    credentials: 'include' // Ensures cookies are sent
})
.then(response => {
    if (!response.ok) {
        window.location.href = '/sms/Pages/login.html'; // Redirect if session is not valid
    } else {
        console.log("Session not found or expired");
    }
```
- If forwarding a date to the API is needed, convert the format as following:

```javascript
const date = new Date()
                .toISOString()  // Converts to format: 2000-09-05T21:00:00.000Z
                .replace('.000', '') + '[UTC]';  // Removes milliseconds and adds [UTC]
```
## API Endpoints

### User Management

| Method | Endpoint | Description | Request Body | Response Body | Response Status | Permissions |
|--------|----------|-------------|--------------|---------------|-----------------|-------------|
| GET | `/sms/api/user` | Get all users | None | List<User> | 200 OK / 401 UNAUTHORIZED for who are not admins | Admin |
| GET | `/sms/api/user/checkSession` | Check if session is expired | None | None | 200 OK / 401 Unauthorized | Public |
| POST | `/sms/api/user/logout` | Logout user session | None | None | 200 OK | Public | 
| POST | `/sms/api/user/login` | User login | `{username: string, passwd: string}` | User | 200 OK / 401 Unauthorized for incorrect user username or password | Public |
| GET | `/sms/api/user/{userId}` | Get user by ID | None | User | 200 OK / 401 UNAUTHORIZED | Admin/User himself |
| POST | `/sms/api/user` | Register new user | `{username: string, passwd: string, full_name: string, email: string, address: string, birth_date: string, job: string}` | User | 200 OK / 400 Bad Request | Public |
| PUT | `/sms/api/user/{userId}` | Update user | User object | User | 200 OK / 403 Forbidden | Admin/User himself |
| DELETE | `/sms/api/user/{userId}` | Delete user | None | None | 200 OK / 403 Forbidden | Admin/User himself |

### Twilio Integration

| Method | Endpoint | Description | Request Body | Response Body | Response Status | Permissions |
|--------|----------|-------------|--------------|---------------|-----------------|-------------|
| POST | `/sms/api/twilio` | Register Twilio account | `{sid: string, auth_token: string, sender_id: string, phone_number: string}` | None | 200 OK / 400 Bad Request / 401 UNAUTHORIZED | Public |
| GET | `/sms/api/twilio/{userId}` | Get Twilio by user ID | None | Twilio | 200 OK / 404 Not Found | Admin/User himself |
| PUT | `/sms/api/twilio/` | Update Twilio account | Twilio object | None | 200 OK / 400 Bad Request / 401 UNAUTHORIZED | User himself |
| DELETE | `/sms/api/twilio/{twilioId}` | Delete Twilio account | None | None | 200 OK / 400 Bad Request / 400 Bad Request / 401 UNAUTHORIZED | User himself |
| GET | `/sms/api/twilio/accounts` | Get all Twilio accounts | None | List<Twilio> | 200 OK / 400 Bad Request / 401 UNAUTHORIZED | Admin |

### Verification Services

| Method | Endpoint | Description | Request Body | Response Body | Response Status | Permissions |
|--------|----------|-------------|--------------|---------------|-----------------|-------------|
| POST | `/sms/api/verificationCode/send` | Send verification code | None | None | 200 OK (sent) / 400 Bad Request / 401 UNAUTHORIZED | User himself |
| POST | `/sms/api/verificationCode/verify` | Verify code | `{verification_code: string}` | None | 200 OK (verified) / 400 Bad Request (Not verified) | User himself |

### Message Handling

| Method | Endpoint | Description | Request Body | Response Body | Response Status | Permissions |
|--------|----------|-------------|--------------|---------------|-----------------|-------------|
| POST | `/sms/api/message` | Send message | `{from_num: string, to_num: string, body: string}` | OutboundMsg | 200 OK / 400 Bad Request / 401 UNAUTHORIZED | User himself |
| GET | `/sms/api/message` | Get all messages | None | List<OutboundMsg> | 200 OK / 400 Bad Request / 401 UNAUTHORIZED | Admin |
| GET | `/sms/api/message/{userId}` | Get messages by user | None | List<OutboundMsg> | 200 OK / 400 Bad Request / 401 UNAUTHORIZED | Admin/User himself |
| DELETE | `/sms/api/message/{msgId}` | Delete message | None | None | 200 OK / 400 Bad Request | Admin/User himself |
