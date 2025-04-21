#!/bin/bash

# Set JAVA_OPTS as a single properly quoted string to avoid shell interpretation issues
JAVA_OPTS="-Djava.awt.headless=true -Djava.security.egd=file:/dev/./urandom"

# Set CATALINA_OPTS separately to ensure it's properly handled
CATALINA_OPTS="-Dhibernate.connection.url=jdbc:mysql://db:3306/twiliodb?useSSL=false&allowPublicKeyRetrieval=true"

# Export the variables
export JAVA_OPTS
export CATALINA_OPTS 