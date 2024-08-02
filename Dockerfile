# Use Tomcat 10.1 with JDK 17
FROM tomcat:10.1-jdk17-temurin

# Remove default Tomcat applications
RUN rm -rf webapps/*

# Copy your WAR file to the webapps directory
#COPY build/AssignmentProject1.war webapps/AssignmentProject1.war

# Expose the default Tomcat port
EXPOSE 8080

# Start Tomcat
CMD ["catalina.sh", "run"]