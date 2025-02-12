# API Gateway

## Overview
The **API Gateway** is a crucial microservice within the Movie Reservation System that acts as a single entry point for client requests. It manages authentication, request routing, and load balancing to ensure seamless communication between services.

## Features
- Centralized authentication and authorization using JWT.
- Routing for backend microservices.
- Handles concerns like security by sending certain requests through the user-service to validate the jwt token.

## Technologies Used
- **Spring Boot** – Core framework for building the microservice.
- **Spring Cloud Gateway** – For routing and load balancing.
- **Spring Security** – For handling authentication and authorization.
- **JWT Authentication** – Securing API endpoints.
- **Spring Cloud Eureka** – Service discovery and registration.

## Service Communication
- Routes requests to **User Service** first for authentication or user management.
- Routes requests to the corresponding service based on client needs.

## Installation & Setup
1. Clone the repository:
   ```bash
   git clone https://github.com/ChristosDurro/movie-reservation-system-api-gateway.git
   ```
2. Navigate to the project folder:
   ```bash
   cd movie-reservation-system-api-gateway
   ```
3. Configure the `application.properties` file:
   ```properties
    # Route configurations

   can either write service name or url and port number to map the requests to their corresponding routes
   
    spring.cloud.gateway.routes[0].id=movie-service
    spring.cloud.gateway.routes[0].uri=http://localhost:8080
    spring.cloud.gateway.routes[0].predicates[0]=Path=/movies/**
    
    spring.cloud.gateway.routes[1].id=user-service
    spring.cloud.gateway.routes[1].uri=lb://user-service
    spring.cloud.gateway.routes[1].predicates[0]=Path=/users/**
    
    spring.cloud.gateway.routes[2].id=schedule-service
    spring.cloud.gateway.routes[2].uri=lb://schedule-service
    spring.cloud.gateway.routes[2].predicates[0]=Path=/schedules/**
    
    spring.cloud.gateway.routes[3].id=seat-service
    spring.cloud.gateway.routes[3].uri=http://localhost:8083
    spring.cloud.gateway.routes[3].predicates[0]=Path=/seats/**
    
    spring.cloud.gateway.routes[4].id=ticket-service
    spring.cloud.gateway.routes[4].uri=http://localhost:8084
    spring.cloud.gateway.routes[4].predicates[0]=Path=/tickets/**
    
    spring.cloud.gateway.routes[5].id=reservation-service
    spring.cloud.gateway.routes[5].uri=http://localhost:8085
    spring.cloud.gateway.routes[5].predicates[0]=Path=/reservations/**
   ```
4. Build and run the service:
   ```bash
   mvn spring-boot:run
   ```

---

This service is part of the **Movie Reservation System**, designed to showcase a microservices-based architecture with Spring Boot.

