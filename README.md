Backend project writting in Java Spring Boot

Create a application.properties file in src/main/resources and paste the following code:
```
spring.application.name=backend
server.port=3000

# Enable H2 database and console
spring.datasource.url=jdbc:h2:mem:testdb
spring.datasource.driver-class-name=org.h2.Driver
spring.datasource.username=sa
spring.datasource.password=sa
spring.h2.console.enabled=true

# Hibernate will auto-create the table from your entity
spring.jpa.hibernate.ddl-auto=create

# Force Spring Boot to always run schema/data SQL scripts
spring.sql.init.mode=always

# Cors localhost
cors.allowed-origins=http://localhost:8080

# JWT
jwt.secret-key=d2ViNC1ub3Qtc28tc2VjcmV0LWFjY2Vzcy1zZWNyZXQ=
jwt.token.issuer=backend_itip
jwt.token.lifetime=8h
```