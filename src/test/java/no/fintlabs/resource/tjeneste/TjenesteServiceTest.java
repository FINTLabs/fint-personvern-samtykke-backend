package no.fintlabs.resource.tjeneste;

import no.fint.model.felles.kompleksedatatyper.Identifikator;
import no.fint.model.resource.personvern.samtykke.TjenesteResource;
import no.fintlabs.adapter.models.OperationType;
import no.fintlabs.adapter.models.RequestFintEvent;
import no.fintlabs.config.ApplicationProperties;
import no.fintlabs.utils.EventStatusService;
import no.fintlabs.utils.KafkaProducer;
import no.fintlabs.utils.ResourceCollection;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import static org.junit.Assert.assertThrows;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;


class TjenesteServiceTest {

    @Mock
    private EventStatusService eventStatusService;

    @Mock
    private KafkaProducer kafkaProducer;

    @Mock
    private TjenesteMapper tjenesteMapper;

    @Mock
    private ResourceCollection<TjenesteResource> tjenesteResources;

    @InjectMocks
    private TjenesteService tjenesteService;

    @BeforeEach
    void setUp() {
        ApplicationProperties applicationProperties = new ApplicationProperties();

        kafkaProducer = mock(KafkaProducer.class);
        eventStatusService = mock(EventStatusService.class);
        tjenesteMapper = mock(TjenesteMapper.class);
        tjenesteService = new TjenesteService(eventStatusService, kafkaProducer, tjenesteMapper, applicationProperties);
    }


    @Test
    public void testCreateTjeneste() {
        String orgName = "orgName";
        Tjeneste tjeneste = new Tjeneste();
        tjeneste.setNavn("Test Tjeneste");
        TjenesteResource tjenesteResource = new TjenesteResource();

        when(tjenesteMapper.toTjenesteResource(tjeneste)).thenReturn(new TjenesteResource());

        RequestFintEvent requestFintEvent = new RequestFintEvent();
        requestFintEvent.setCorrId("ac48de94-4733-11ee-be56-0242ac120002");
        when(kafkaProducer.sendEvent(OperationType.CREATE, "tjeneste", orgName, tjenesteResource)).thenReturn(requestFintEvent);

        String corrId = tjenesteService.create(orgName, tjeneste);

        assertEquals(corrId, "ac48de94-4733-11ee-be56-0242ac120002");
        //verify(eventStatusService.add("corrId"));
    }

    @Test
    public void testExeption() {
        Tjeneste tjeneste = new Tjeneste();
        tjeneste.setNavn("");

        assertThrows(IllegalArgumentException.class, () -> tjenesteService.create("", tjeneste));
    }

    @Test
    public void testAddBehandling() {
        String orgId = "orgId";
        String corrId = "acf8afd4-472b-11ee-be56-0242ac120002";

        TjenesteResource tjenesteResource = new TjenesteResource();
        tjenesteResource.setSystemId(new Identifikator());
        tjenesteResource.getSystemId().setIdentifikatorverdi("123");


        tjenesteService.addResource(orgId, tjenesteResource, corrId);

        verify(eventStatusService).update(corrId);
    }

}
