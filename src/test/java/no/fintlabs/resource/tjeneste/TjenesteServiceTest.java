package no.fintlabs.resource.tjeneste;

import no.fint.model.felles.kompleksedatatyper.Identifikator;
import no.fint.model.resource.personvern.samtykke.TjenesteResource;
import no.fintlabs.adapter.models.OperationType;
import no.fintlabs.adapter.models.RequestFintEvent;
import no.fintlabs.config.ApplicationProperties;
import no.fintlabs.resource.behandling.Behandling;
import no.fintlabs.utils.EventStatusService;
import no.fintlabs.utils.KafkaProducer;
import no.fintlabs.utils.ResourceCollection;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.Assert.assertThrows;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;


@Disabled
class TjenesteServiceTest {

    TjenesteMapper tjenesteMapper = new TjenesteMapper(new ApplicationProperties());
    private TjenesteService tjenesteService;
    private KafkaProducer kafkaProducer;

    @Mock
    private EventStatusService eventStatusService;

    @Mock
    private ResourceCollection<TjenesteResource> tjenesteResources;


    @BeforeEach
    void setUp() {
        ApplicationProperties applicationProperties = new ApplicationProperties();
        kafkaProducer = mock(KafkaProducer.class);
        eventStatusService = mock(EventStatusService.class);
        tjenesteService = new TjenesteService(eventStatusService, kafkaProducer, tjenesteMapper, applicationProperties);
        tjenesteResources = new ResourceCollection<>();
    }


    @Test
    public void testCreateTjeneste() {
        Tjeneste tjeneste = new Tjeneste();
        String orgName = "orgName";
        tjeneste.setNavn("This is a test tjeneste");

        TjenesteResource tjenesteResource = tjenesteMapper.toTjenesteResource(tjeneste);

        RequestFintEvent requestFintEvent = new RequestFintEvent();
        when(kafkaProducer.sendEvent(Mockito.eq(OperationType.CREATE), Mockito.eq("tjeneste"), Mockito.eq(orgName), Mockito.any(TjenesteResource.class))).thenReturn(requestFintEvent);
        requestFintEvent.setCorrId("e6de5650-47f7-11ee-be56-0242ac120002");

        String corrId = tjenesteService.create(orgName, tjeneste);

        assertEquals(corrId, "e6de5650-47f7-11ee-be56-0242ac120002");
    }

    @Test
    public void testExeption() {
        Tjeneste tjeneste = new Tjeneste();
        tjeneste.setNavn("");

        assertThrows(IllegalArgumentException.class, () -> tjenesteService.create("", tjeneste));
    }

    @Test
    public void testAddResource() {
        String orgId = "orgId";
        String corrId = "acf8afd4-472b-11ee-be56-0242ac120002";
        TjenesteResource tjenesteResource = new TjenesteResource();

        tjenesteResource.setSystemId(new Identifikator());
        tjenesteResource.getSystemId().setIdentifikatorverdi("123");

        tjenesteService.addResource(orgId, tjenesteResource, corrId);

        verify(eventStatusService).update(corrId);

    }

    @Disabled
    @Test
    public void testUpdateTjeneste() {
        String orgName = "TestOrg";
        Behandling behandling = new Behandling();
        String tjenesteId = "TjenesteId";
        List<String> tjenesteIds = new ArrayList<>();
        tjenesteIds.add(tjenesteId);
        behandling.setTjenesteIds(tjenesteIds);

        TjenesteResource tjenesteResource = new TjenesteResource();
        Mockito.when(tjenesteResources.getResource(orgName, tjenesteId)).thenReturn(Optional.of(tjenesteResource));

        tjenesteService.updateTjeneste(orgName, behandling);

        verify(kafkaProducer, times(1)).sendEvent(eq(OperationType.UPDATE), eq("tjeneste"), eq(orgName), any(Tjeneste.class));
    }

}
