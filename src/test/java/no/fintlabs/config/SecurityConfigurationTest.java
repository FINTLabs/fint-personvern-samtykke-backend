package no.fintlabs.config;

import no.fintlabs.Application;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.jwt.ReactiveJwtDecoder;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Mono;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@ExtendWith(SpringExtension.class)
@SpringBootTest(classes = Application.class,webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Disabled
public class SecurityConfigurationTest {

    @Autowired
    private WebTestClient webTestClient;

    @MockBean
    private ReactiveJwtDecoder jwtDecoder;

    @Test
    public void testEndpointAccessWithValidToken() {
        Jwt jwt = mock(Jwt.class);
        when(jwtDecoder.decode(any())).thenReturn(Mono.just(jwt));

        webTestClient
                .get().uri("/behandling/fintlabs-no")
                .header("Authorization", "Bearer VALID_TOKEN")
                .exchange()
                .expectStatus().isOk();
    }

    @Test
    public void testEndpointAccessWithoutToken() {
        webTestClient
                .get().uri("/consentadmin/behandling/my-org")
                .exchange()
                .expectStatus().isUnauthorized();
    }
}
