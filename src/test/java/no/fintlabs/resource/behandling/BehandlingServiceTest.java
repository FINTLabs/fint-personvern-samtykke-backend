package no.fintlabs.resource.behandling;

import no.fint.model.resource.personvern.samtykke.BehandlingResource;
import no.fintlabs.adapter.models.OperationType;
import no.fintlabs.adapter.models.RequestFintEvent;
import no.fintlabs.resource.tjeneste.TjenesteService;
import no.fintlabs.utils.EventStatusService;
import no.fintlabs.utils.KafkaProducer;
import no.fintlabs.utils.ResourceCollection;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class BehandlingServiceTest {

    @Mock
    private ResourceCollection<BehandlingResource> behandlingResources;

    @Mock
    private KafkaProducer kafkaProducer;

    @Mock
    private EventStatusService eventStatusService;

    @Mock
    private BehandlingMapper behandlingMapper;

    @Mock
    private TjenesteService tjenesteService;

    @InjectMocks
    private BehandlingService behandlingService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }


    @Test
    void getBehandlingerReturnsListWhenGivvenOrgId() {
        String orgId = "orgId";
        BehandlingResource resource1 = new BehandlingResource();
        BehandlingResource resource2 = new BehandlingResource();
        when(behandlingResources.getResources(orgId)).thenReturn(Arrays.asList(resource1, resource2));

        List<Behandling> behandlinger = behandlingService.getBehandlinger(orgId);

        assertNotNull(behandlinger);
        assertEquals(2, behandlinger.size());
    }

    @Test
    void createBehandling() {
        String orgName = "orgName";
        String formal = "someFormal";
        Behandling behandling = new Behandling();
        behandling.setFormal(formal);
        behandling.setTjenesteIds(Collections.singletonList("tjenesteId"));
        behandling.setBehandlingsgrunnlagIds(Collections.singletonList("behandlingsgrunnlagId"));
        behandling.setPersonopplysningIds(Collections.singletonList("personopplysningId"));

        when(kafkaProducer.sendEvent(any(OperationType.class), anyString(), anyString(), any()))
                .thenReturn(new RequestFintEvent("correlationId", "orgId", "domainName", "packageName", "resourceName", OperationType.CREATE, 123L, 456L, "value"));

        String correlationId = behandlingService.create(orgName, behandling);

        assertNotNull(correlationId);
        assertEquals("correlationId", correlationId);
        verify(eventStatusService).add("correlationId");
        verify(tjenesteService).updateTjeneste(orgName, behandling);
    }

    @Test
    void updateStatusTest() {
        String orgName = "orgName";
        String id = "someId";
        boolean aktiv = true;
        BehandlingResource behandlingResource = new BehandlingResource();
        behandlingResource.setAktiv(false);
        Optional<BehandlingResource> result = Optional.of(behandlingResource);

        when(behandlingResources.getResource(orgName, id)).thenReturn(result);
        when(kafkaProducer.sendEvent(any(OperationType.class), anyString(), anyString(), any()))
                .thenReturn(new RequestFintEvent("correlationId", "orgId", "domainName",
                        "packageName", "resourceName", OperationType.CREATE, 123L,
                        456L, "value"));

        String correlationId = behandlingService.updateState(orgName, id, aktiv);

        assertEquals("correlationId", correlationId);
        assertTrue(behandlingResource.getAktiv());
        verify(eventStatusService).add("correlationId");
    }
}