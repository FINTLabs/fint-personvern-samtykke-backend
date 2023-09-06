package no.fintlabs.resource.behandling;

import no.fint.model.resource.personvern.samtykke.BehandlingResource;
import no.fint.model.resource.personvern.samtykke.BehandlingResources;
import no.fintlabs.adapter.models.OperationType;
import no.fintlabs.adapter.models.RequestFintEvent;
import no.fintlabs.config.ApplicationProperties;
import no.fintlabs.resource.tjeneste.TjenesteService;
import no.fintlabs.utils.EventStatusService;
import no.fintlabs.utils.KafkaProducer;
import no.fintlabs.utils.ResourceCollection;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;

import java.util.Arrays;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

class BehandlingServiceTest {

    @Mock
    private ResourceCollection<BehandlingResource> behandlingResources;

    @Mock
    private EventStatusService eventStatusService;

    @Mock
    private KafkaProducer kafkaProducer;

    @Mock
    private TjenesteService tjenesteService;

    @InjectMocks
    private BehandlingService behandlingService;


    @BeforeEach
    void setUp() {
        kafkaProducer = Mockito.mock(KafkaProducer.class);
        eventStatusService = Mockito.mock(EventStatusService.class);
        tjenesteService = Mockito.mock(TjenesteService.class);

        behandlingResources = mock(ResourceCollection.class);
        ApplicationProperties applicationProperties = new ApplicationProperties();
        BehandlingMapper behandlingMapper = new BehandlingMapper(applicationProperties);
        behandlingService = new BehandlingService(kafkaProducer, eventStatusService, behandlingMapper, tjenesteService, behandlingResources);
    }

    @Test
    public void testCreateBehandling() {
        String orgName = "TestName";
        Behandling behandling = new Behandling();
        behandling.setFormal("testFormal");
        behandling.setTjenesteIds(Arrays.asList("tjeneste1", "tjeneste2"));
        behandling.setBehandlingsgrunnlagIds(Arrays.asList("grunnlag1", "grunnlag2"));
        behandling.setPersonopplysningIds(Arrays.asList("opplysning1", "opplysning2"));

        RequestFintEvent requestFintEvent = new RequestFintEvent();
        when(kafkaProducer.sendEvent(eq(OperationType.CREATE), eq("behandling"), eq(orgName), any(BehandlingResource.class))).thenReturn(requestFintEvent);

        String corrId = behandlingService.create(orgName, behandling);

        assertEquals(corrId, requestFintEvent.getCorrId());

    }

    @Test
    void testUpdateBehandlingState() {
        BehandlingResource behandlingResource = new BehandlingResource();
        String orgName = "orgName";
        String id = "ae581cbe-8c8f-4157-8bda-01e499c89143";
        boolean aktiv = true;


        when(behandlingResources.getResource(orgName, id)).thenReturn(Optional.of(behandlingResource));

        RequestFintEvent requestFintEvent = new RequestFintEvent();
        when(kafkaProducer.sendEvent(OperationType.CREATE, "behandling", orgName, behandlingResource)).thenReturn(requestFintEvent);

        String result = behandlingService.updateState(orgName, id, aktiv);

        assertEquals(requestFintEvent.getCorrId(), result);
        assertEquals(aktiv, behandlingResource.getAktiv());
        verify(eventStatusService).add(requestFintEvent.getCorrId());
    }
}
