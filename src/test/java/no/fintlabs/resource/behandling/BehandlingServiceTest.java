package no.fintlabs.resource.behandling;

import no.fint.model.resource.personvern.samtykke.BehandlingResource;
import no.fintlabs.adapter.models.OperationType;
import no.fintlabs.adapter.models.RequestFintEvent;
import no.fintlabs.config.ApplicationProperties;
import no.fintlabs.resource.tjeneste.TjenesteService;
import no.fintlabs.utils.EventStatusService;
import no.fintlabs.utils.KafkaProducer;
import no.fintlabs.utils.OrgIdUtil;
import no.fintlabs.utils.ResourceCollection;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@Disabled
class BehandlingServiceTest {

    private BehandlingMapper behandlingMapper;

    private ResourceCollection<BehandlingResource> behandlingResources;

    private RequestFintEvent requestFintEvent;

    @Mock
    private EventStatusService eventStatusService;

    @Mock
    private KafkaProducer kafkaProducer;

    @Mock
    private TjenesteService tjenesteService;

    @InjectMocks
    private BehandlingService behandlingService;

    private BehandlingResource behandlingResource;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
//        kafkaProducer = Mockito.mock(KafkaProducer.class);
//        eventStatusService = Mockito.mock(EventStatusService.class);
//        tjenesteService = Mockito.mock(TjenesteService.class);

//        behandlingResources = mock(ResourceCollection.class);

        requestFintEvent = new RequestFintEvent();
        behandlingResources = new ResourceCollection<>();
        ApplicationProperties applicationProperties = new ApplicationProperties();
        BehandlingMapper behandlingMapper = new BehandlingMapper(applicationProperties);
        behandlingService = new BehandlingService(kafkaProducer, eventStatusService, behandlingMapper, tjenesteService, behandlingResources);
        behandlingService = new BehandlingService(kafkaProducer, eventStatusService, behandlingMapper, tjenesteService);

        Behandling behandling = new Behandling();
        behandling.setId("id");
        behandling.setAktiv(true);
        behandling.setFormal("Test formal");
        behandling.setTjenesteIds(Collections.singletonList("tjenesteId"));

        behandlingResource = behandlingMapper.toBehandlingResource(behandling);
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
        RequestFintEvent requestFintEvent = new RequestFintEvent();
        String orgName = "orgName";
        String id = "ae581cbe-8c8f-4157-8bda-01e499c89143";

        behandlingResource = new BehandlingResource();

        behandlingResource.setAktiv(false);

        requestFintEvent = kafkaProducer.sendEvent(OperationType.CREATE, "behandling", orgName, behandlingResource);
        behandlingResource.setAktiv(true);
        requestFintEvent.setCorrId(id);
        eventStatusService.add(requestFintEvent.getCorrId());

        verify(eventStatusService).add(requestFintEvent.getCorrId());
        assertTrue(behandlingResource.getAktiv());
    }


    @Test
    public void testAddResource() {
        String orgId = "orgId";
        String orgName = "orgName";
        behandlingService.addResource(orgId, behandlingResource);

        String identifikatorverdi = behandlingResource.getSystemId().getIdentifikatorverdi();

        behandlingResources.put(OrgIdUtil.uniform(orgId), identifikatorverdi, behandlingResource);

        Optional<BehandlingResource> resourceOptional = behandlingResources.getResource(orgId, identifikatorverdi);
        assertTrue(resourceOptional.isPresent());
    }

    @Test
    public void testStatus() {
        Behandling behandling = new Behandling();
        behandling.setAktiv(true);
        behandling.setId("id");
        boolean result = behandlingService.status(behandling.getId());
        assertFalse(result);
    }

    @Disabled
    @Test
    public void testGetBehandlinger() {
        String orgName = "orgName";
        OrgIdUtil orgIdUtil = mock(OrgIdUtil.class);
        Behandling behandling = new Behandling();

        BehandlingResource resource1 = new BehandlingResource();

        List<Behandling> behandlinger = behandlingService.getBehandlinger(orgName);

        assertEquals(2, behandlinger.size());
    }
}
