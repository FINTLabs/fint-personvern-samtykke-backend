package no.fintlabs.resource.behandling;

import no.fint.model.felles.kompleksedatatyper.Identifikator;
import no.fint.model.resource.personvern.samtykke.BehandlingResource;
import no.fint.model.resource.personvern.samtykke.BehandlingResources;
import no.fintlabs.adapter.models.OperationType;
import no.fintlabs.adapter.models.RequestFintEvent;
import no.fintlabs.config.ApplicationProperties;
import no.fintlabs.resource.tjeneste.TjenesteService;
import no.fintlabs.utils.EventStatusService;
import no.fintlabs.utils.KafkaProducer;
import no.fintlabs.utils.OrgIdUtil;
import no.fintlabs.utils.ResourceCollection;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;

import javax.annotation.Resource;
import java.util.*;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertSame;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@Disabled
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


    private BehandlingResource behandlingResource;

    @BeforeEach
    void setUp() {
        kafkaProducer = Mockito.mock(KafkaProducer.class);
        eventStatusService = Mockito.mock(EventStatusService.class);
        tjenesteService = Mockito.mock(TjenesteService.class);

        behandlingResources = mock(ResourceCollection.class);
        ApplicationProperties applicationProperties = new ApplicationProperties();
        BehandlingMapper behandlingMapper = new BehandlingMapper(applicationProperties);
        behandlingService = new BehandlingService(kafkaProducer, eventStatusService, behandlingMapper, tjenesteService, behandlingResources);


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

    @Disabled
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

    @Disabled
    @Test
    public void testAddResource(){
        String orgId = "orgId";
        String orgName = "orgName";
        behandlingService.addResource(orgId, behandlingResource);
        String identifikatorverdi = behandlingResource.getSystemId().getIdentifikatorverdi();
        verify(behandlingResources).put(eq(OrgIdUtil.uniform(orgId)), eq(behandlingResource.getSystemId().getIdentifikatorverdi()), eq(behandlingResource));

        Optional<BehandlingResource> savedBehandlingResource = behandlingResources.getResource(orgName,identifikatorverdi);

        Assertions.assertSame(behandlingResource, savedBehandlingResource);
    }

    @Disabled
    @Test
    public void testGetBehandlinger() {
        String orgName = "orgName";
        OrgIdUtil orgIdUtil = mock(OrgIdUtil.class);
        //when(orgIdUtil.uniform(orgName)).thenReturn("uniformOrgName");

        // Create instances of BehandlingResource
        BehandlingResource resource1 = new BehandlingResource();
        BehandlingResource resource2 = new BehandlingResource();

        // Act
        List<Behandling> behandlinger = behandlingService.getBehandlinger(orgName);

        // Assert
        assertEquals(2, behandlinger.size());

        // Add assertions for Behandling instances based on your actual mapping logic
        // For example:
//        assertEquals(expectedBehandling1, behandlinger.get(0));
//        assertEquals(expectedBehandling2, behandlinger.get(1));
    }
}
