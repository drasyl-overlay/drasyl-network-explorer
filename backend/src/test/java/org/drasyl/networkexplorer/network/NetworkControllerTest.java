package org.drasyl.networkexplorer.network;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT;

@SpringBootTest(webEnvironment = RANDOM_PORT)
class NetworkControllerTest {
    @LocalServerPort
    private int port;
    @Autowired
    private TestRestTemplate restTemplate;

    @Test
    void shouldReturnNetwork() {
        // arrange

        // act
        final ResponseEntity<String> response = restTemplate.getForEntity("http://localhost:" + port + "/network-1.json", String.class);

        // assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("[]", response.getBody());
    }
}