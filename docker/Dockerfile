FROM tomcat:10-jdk11-openjdk

# Remove default Tomcat applications
RUN rm -rf /usr/local/tomcat/webapps/*

# Copy the pre-built WAR file into Tomcat's webapps directory
COPY target/*.war /usr/local/tomcat/webapps/sms.war

# Create a very simple custom startup script that directly calls Java
RUN echo '#!/bin/bash' > /usr/local/tomcat/bin/run.sh && \
    echo 'cd /usr/local/tomcat' >> /usr/local/tomcat/bin/run.sh && \
    echo 'export JAVA_OPTS="-Djava.awt.headless=true -Djava.security.egd=file:/dev/./urandom"' >> /usr/local/tomcat/bin/run.sh && \
    echo 'export CATALINA_OPTS="-Dhibernate.connection.url=jdbc:mysql://db:3306/twiliodb?useSSL=false&allowPublicKeyRetrieval=true"' >> /usr/local/tomcat/bin/run.sh && \
    echo 'exec java $JAVA_OPTS $CATALINA_OPTS -Djava.util.logging.config.file=/usr/local/tomcat/conf/logging.properties -Djava.util.logging.manager=org.apache.juli.ClassLoaderLogManager -Djdk.tls.ephemeralDHKeySize=2048 -Djava.protocol.handler.pkgs=org.apache.catalina.webresources -classpath /usr/local/tomcat/bin/bootstrap.jar:/usr/local/tomcat/bin/tomcat-juli.jar -Dcatalina.base=/usr/local/tomcat -Dcatalina.home=/usr/local/tomcat org.apache.catalina.startup.Bootstrap start' >> /usr/local/tomcat/bin/run.sh && \
    chmod +x /usr/local/tomcat/bin/run.sh

# Expose the port Tomcat runs on
EXPOSE 8080

CMD ["/usr/local/tomcat/bin/run.sh"] 