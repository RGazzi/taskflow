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
@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
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
        given()
            .contentType("application/json")
            .body("""
                {"title":"Integration test task","description":"desc","dueDate":"2026-12-01"}
                """)
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
    		.get("api/tasks/999")
    	.then()
    		.statusCode(404);
    }
    
    

}
