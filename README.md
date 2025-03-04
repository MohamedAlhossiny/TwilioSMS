# TwilioSMS System Design

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

**Note:** Always check session cookies for authentication

## API Endpoints

### User Management

| Method | Endpoint | Description | Request Body | Response Body | Permissions |
|--------|----------|-------------|--------------|---------------|-------------|
| GET | `/sms/api/user` | Get all users | None | List<User> | Admin |
| POST | `/sms/api/user/login` | User login | `{username: string, passwd: string}` | User | Public |
| GET | `/sms/api/user/{userId}` | Get user by ID | None | User | Admin/User himself |
| POST | `/sms/api/user/register` | Register new user | `{username: string, passwd: string, full_name: string, email: string, address: string, birth_date: string, job: string}` | User | Public |
| PUT | `/sms/api/user/{userId}` | Update user | User object | User | Admin/User himself |
| DELETE | `/sms/api/user/{userId}` | Delete user | None | None | Admin/User himself |

### Twilio Integration

| Method | Endpoint | Description | Request Body | Response Body | Permissions |
|--------|----------|-------------|--------------|---------------|-------------|
| POST | `/sms/api/twilio` | Register Twilio account | `{sid: string, auth_token: string, sender_id: string, phone_number: string}` | None | Public |
| GET | `/sms/api/twilio/{userId}` | Get Twilio by user ID | None | Twilio | Admin/User himself |
| PUT | `/sms/api/twilio/{twilioId}` | Update Twilio account | Twilio object | None | User himself |
| DELETE | `/sms/api/twilio/{twilioId}` | Delete Twilio account | None | None | User himself |
| GET | `/sms/api/twilio/accounts` | Get all Twilio accounts | None | List<Twilio> | Admin |

### Verification Services

| Method | Endpoint | Description | Request Body | Response Body | Permissions |
|--------|----------|-------------|--------------|---------------|-------------|
| POST | `/sms/api/verificationCode/send` | Send verification code | None | None | User himself |
| POST | `/sms/api/verificationCode/verify` | Verify code | `{verification_code: string}` | None | User himself |

### Message Handling

| Method | Endpoint | Description | Request Body | Response Body | Permissions |
|--------|----------|-------------|--------------|---------------|-------------|
| POST | `/sms/api/message` | Send message | `{from_num: string, to_num: string, body: string}` | OutboundMsg | User himself |
| GET | `/sms/api/message` | Get all messages | None | List<OutboundMsg> | Admin |
| GET | `/sms/api/message/{userId}` | Get messages by user | None | List<OutboundMsg> | Admin/User himself |
| DELETE | `/sms/api/message/{msgId}` | Delete message | None | None | Admin/User himself |
