package no.fintlabs;
import no.fint.model.felles.kompleksedatatyper.Identifikator;
import no.fint.model.resource.personvern.samtykke.BehandlingResource;
import no.fintlabs.config.ApplicationProperties;
import no.fintlabs.resource.behandling.Behandling;
import no.fintlabs.resource.behandling.BehandlingMapper;
import org.junit.jupiter.api.Test;

import java.util.Collections;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class BehandlingMapperTest {

    private BehandlingMapper behandlingMapper;

    private ApplicationProperties applicationProperties;

    public BehandlingMapperTest() {
        applicationProperties = new ApplicationProperties();
        applicationProperties.setBaseUrl("http://example.com");
        behandlingMapper = new BehandlingMapper(applicationProperties);
    }

    @Test
    public void testToBehandling() {
        BehandlingResource behandlingResource = new BehandlingResource();
        behandlingResource.setSystemId(new Identifikator());
        behandlingResource.setAktiv(true);
        behandlingResource.setFormal("Test formal");

        Behandling behandling = behandlingMapper.toBehandling(behandlingResource, "orgId");

        assertEquals(behandlingResource.getSystemId().getIdentifikatorverdi(), behandling.getId());
        assertEquals(behandlingResource.getAktiv(), behandling.getAktiv());
        assertEquals(behandlingResource.getFormal(), behandling.getFormal());
        assertEquals("orgId", behandling.getOrgId());
    }

    @Test
    public void testToBehandlingResource() {
        Behandling behandling = new Behandling();
        behandling.setId("id");
        behandling.setAktiv(true);
        behandling.setFormal("Test formal");
        behandling.setTjenesteIds(Collections.singletonList("tjenesteId"));

        BehandlingResource behandlingResource = behandlingMapper.toBehandlingResource(behandling);

        assertEquals(behandling.getId(), behandlingResource.getSystemId().getIdentifikatorverdi());
        assertEquals(behandling.getAktiv(), behandlingResource.getAktiv());
        assertEquals(behandling.getFormal(), behandlingResource.getFormal());
        assertEquals(1, behandlingResource.getTjeneste().size());
    }
}
