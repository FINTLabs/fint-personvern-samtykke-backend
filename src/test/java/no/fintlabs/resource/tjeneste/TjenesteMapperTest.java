package no.fintlabs.resource.tjeneste;

import no.fint.model.felles.kompleksedatatyper.Identifikator;
import no.fint.model.resource.personvern.samtykke.TjenesteResource;
import no.fintlabs.Application;
import no.fintlabs.config.ApplicationProperties;
import no.fintlabs.utils.FintUtils;
import org.junit.Ignore;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static reactor.core.publisher.Mono.when;

class TjenesteMapperTest {

    private TjenesteMapper tjenesteMapper;
    private ApplicationProperties applicationProperties;

    @BeforeEach
    void setUp(){
        applicationProperties = Mockito.mock(ApplicationProperties.class);
        tjenesteMapper = new TjenesteMapper(applicationProperties);
    }

    @Test
    public void testToTjeneste() {
        TjenesteResource tjenesteResource = new TjenesteResource();
        String orgId= "orgId";
        tjenesteResource.setSystemId(new Identifikator());
        tjenesteResource.setNavn("TjenesteName");
        tjenesteResource.addBehandling(FintUtils.createLink("Test.com", "/example", "id"));

        Tjeneste tjeneste = tjenesteMapper.toTjeneste(tjenesteResource, orgId);

        assertEquals(orgId, tjeneste.getOrgId());
        assertEquals(tjenesteResource.getSystemId().getIdentifikatorverdi(), tjeneste.getId());
        assertEquals(tjenesteResource.getNavn(), tjeneste.getNavn());
        assertEquals(1, tjeneste.getBehandlingIds().size());


    }


    @Test
    public void testToTjenesteResource(){
        Tjeneste tjeneste = new Tjeneste();
        tjeneste.setId("tjenesteId");
        tjeneste.setNavn("MappedTjeneste");

        TjenesteResource tjenesteResource = tjenesteMapper.toTjenesteResource(tjeneste);

        assertEquals(tjeneste.getId(), tjenesteResource.getSystemId().getIdentifikatorverdi());
        assertEquals(tjeneste.getNavn(), tjenesteResource.getNavn());
    }
}