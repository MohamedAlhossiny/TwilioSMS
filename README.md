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

## Future Enhancements
- [ ] Add Twilio Inbound message feature.

