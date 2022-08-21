package org.drasyl.networkexplorer.telemetry;

import org.json.JSONObject;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT;
import static org.springframework.http.MediaType.APPLICATION_JSON;

@SpringBootTest(webEnvironment = RANDOM_PORT)
class TelemetryControllerTest {
    @LocalServerPort
    private int port;
    @Autowired
    private TestRestTemplate restTemplate;

    @Test
    void shouldAcceptSilentlyValidData() {
        // arrange
        String body = new JSONObject(Map.of(
                "address", "9ce1b5d734458165237819bc9d73c43491bf1d08669a7591cf97f030e0c0e7d7",
                "superPeers", new JSONObject(),
                "childrenPeers", new JSONObject(),
                "peers", new JSONObject()
        )).toString();
        final HttpHeaders headers = new HttpHeaders();
        headers.setContentType(APPLICATION_JSON);
        HttpEntity<String> request = new HttpEntity<>(body, headers);

        // act
        final ResponseEntity<String> response = restTemplate.postForEntity("http://localhost:" + port + "/", request, String.class);

        // assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNull(response.getBody());
    }

    @Test
    void shouldReturnErrorOnInvalidData() {
        // arrange
        String body = "";
        final HttpHeaders headers = new HttpHeaders();
        headers.setContentType(APPLICATION_JSON);
        HttpEntity<String> request = new HttpEntity<>(body, headers);

        // act
        final ResponseEntity<String> response = restTemplate.postForEntity("http://localhost:" + port + "/", request, String.class);

        // assert
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    }
}