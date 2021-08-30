package com.baeldung.autoconfig.exclude;

import io.restassured.RestAssured;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;

import static org.junit.Assert.assertEquals;

@SpringBootTest(classes = TestApplication.class, webEnvironment = WebEnvironment.RANDOM_PORT)
public class ExcludeAutoConfig4IntegrationTest {

    /**
     * Encapsulates the random port the test server is listening on.
     */
    @LocalServerPort
    private int port;

    @Test
    public void givenSecurityConfigExcluded_whenAccessHome_thenNoAuthenticationRequired() {
        int statusCode = RestAssured.get("http://localhost:" + port).statusCode();
        assertEquals(HttpStatus.OK.value(), statusCode);
    }
}
