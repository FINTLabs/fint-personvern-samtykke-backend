package no.fintlabs;

import no.fintlabs.resource.tjeneste.Tjeneste;
import no.fintlabs.resource.tjeneste.TjenesteController;
import no.fintlabs.resource.tjeneste.TjenesteService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.MediaType;
import org.springframework.mock.http.server.reactive.MockServerHttpRequest;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.springframework.web.server.adapter.DefaultServerWebExchange;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

public class TjenesteControllerTest {

    @Mock
    private TjenesteService tjenesteService;

    @InjectMocks
    private TjenesteController tjenesteController;

    private WebTestClient webTestClient;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        webTestClient = WebTestClient.bindToController(tjenesteController).build();
    }

    @Test
    public void testCreateTjenesteNotCreated() {
        String orgName = "testOrg";
        Tjeneste tjeneste = new Tjeneste(); // Assuming a default constructor or provide necessary arguments
        String mockCorrId = "mockedCorrId";

        when(tjenesteService.create(orgName, tjeneste)).thenReturn(mockCorrId);

        webTestClient.post()
                .uri("/{orgName}", orgName)
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(tjeneste)
                .exchange()
                .expectStatus().isNotFound();
    }

    @Test
    public void testCreateTjenesteWhenFound() {
        String orgName = "testOrg";
        Tjeneste tjeneste = new Tjeneste(); // Assuming a default constructor or provide necessary arguments
        String mockCorrId = "mockedCorrId";

        when(tjenesteService.create(orgName, tjeneste)).thenReturn(mockCorrId);

        webTestClient.post()
                .uri("/tjeneste/{orgName}", orgName)
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(tjeneste)
                .exchange()
                .expectStatus().isCreated()
                .expectHeader().valueEquals("Location", "/tjeneste/status/mockedCorrId");
    }
}
