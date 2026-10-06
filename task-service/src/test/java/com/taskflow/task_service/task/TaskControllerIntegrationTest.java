package com.taskflow.task_service.task;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import io.restassured.RestAssured;

@Testcontainers
@SpringBootTest(
    webEnvironment = WebEnvironment.RANDOM_PORT,
    properties = {
        // Desativa a segurança para resolver o erro 401
        "spring.autoconfigure.exclude=org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration,org.springframework.boot.autoconfigure.security.servlet.UserDetailsServiceAutoConfiguration"
    }
)
class TaskControllerIntegrationTest {

    @Container
    @ServiceConnection
    static PostgreSQLContainer<?> postgres = new PostgreSQLContainer<>("postgres:16");

    @LocalServerPort
    private int port;

    @BeforeEach
    void setUp() {
        RestAssured.port = port;
    }

    @Test
    void shouldCreateAndRetrieveTask() {
        // JSON corrigido com aspas escapadas (\")
        String jsonBody = "{"
                + "\"title\": \"Integration test task\","
                + "\"description\": \"desc\","
                + "\"dueDate\": \"2026-12-01\""
                + "}";

        given()
            .contentType("application/json")
            .body(jsonBody)
        .when()
            .post("/api/tasks")
        .then()
            .statusCode(201)
            .body("title", equalTo("Integration test task"))
            .body("status", equalTo("TODO"));
    }

    @Test
    void shouldReturn404WhenTaskDoesNotExist() {
        given()
        .when()
            .get("/api/tasks/999")
        .then()
            .statusCode(404);
    }
}
