package no.fintlabs.resource.tjeneste;

import no.fint.model.resource.personvern.samtykke.TjenesteResource;
import no.fintlabs.config.ApplicationProperties;
import no.fintlabs.utils.EventStatusService;
import no.fintlabs.utils.KafkaProducer;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import spock.lang.Ignore;

import static org.codehaus.groovy.runtime.DefaultGroovyMethods.any;
import static org.junit.jupiter.api.Assertions.*;
import static reactor.core.publisher.Mono.when;

@Ignore
class TjenesteServiceTest {

    @InjectMocks
    private TjenesteService tjenesteService;

    @Mock
    private EventStatusService eventStatusService;

    @Mock
    private KafkaProducer kafkaProducer;

    @Mock
    private TjenesteMapper tjenesteMapper;

    @Mock
    private ApplicationProperties applicationProperties;

    @Test
    public void testCreateTjeneste() {
        Tjeneste tjeneste = new Tjeneste();
        tjeneste.setNavn("Test Tjeneste");

        when(applicationProperties.getBaseUrl()).thenReturn("http://example.com");
        when(tjenesteMapper.toTjenesteResource(any(Tjeneste.class))).thenReturn(new TjenesteResource());
        when(kafkaProducer.sendEvent(any(), any(), any(), any())).thenReturn(new Request FintEvent("correlationId"));

        String orgName = "orgName";
        String corrId = tjenesteService.create(orgName, tjeneste);

        assertNotNull(corrId);
        verify(eventStatusService).add("correlationId");
    }


}