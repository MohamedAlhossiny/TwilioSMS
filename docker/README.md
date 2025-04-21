# Docker Setup for TwilioSMS Project

This directory contains Docker configuration to run the TwilioSMS application with a MySQL database.

## Prerequisites

- Docker
- Docker Compose
- JDK 11
- Maven

## Getting Started

1. Build the project locally first (from the parent directory):

```bash
mvn clean package
```

2. Build and start the containers (from this directory):

```bash
docker-compose up -d
```

3. The application will be available at http://localhost:8080/sms

4. MySQL database is accessible at:
   - Host: localhost
   - Port: 3306
   - Database: twiliodb
   - Username: root
   - Password: root

## Configuration Details

- The Docker setup uses a pre-built WAR file from your local Maven build
- The setup includes a custom startup script to properly configure JAVA_HOME
- The MySQL database is configured with the same settings as in the hibernate.cfg.xml file
- Database initialization is done using the create_database.sql script
- Data is persisted in a Docker volume named mysql-data
- The application and database are connected via a bridge network

## Stopping the Application

```bash
docker-compose down
```

To completely remove all data and volumes:

```bash
docker-compose down -v
```

## Troubleshooting

If you encounter Java-related issues:

1. Check the logs to see the actual JAVA_HOME path being used:
```bash
docker-compose logs app
```

2. Rebuild the containers after making changes:
```bash
docker-compose down
docker-compose build --no-cache
docker-compose up -d
``` 