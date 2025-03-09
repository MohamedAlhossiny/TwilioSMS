# Project Description

TwilioSMS is a robust SMS management system that integrates with Twilio's messaging services to provide a comprehensive platform for sending and managing SMS messages. The system offers user authentication, message tracking, and verification code services through a well-structured API architecture.

## Project Requirements

### Minimum System Requirements
- Java Development Kit (JDK) 17 or higher
- Apache NetBeans IDE 24 or higher
- Apache Tomcat 10.1.x or higher

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

## Future Enhancements
- [ ] Add Twilio Inbound message feature.

