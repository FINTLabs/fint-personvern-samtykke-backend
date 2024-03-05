package no.fintlabs.resource.behandlingsgrunnlag;

import no.fint.model.felles.kompleksedatatyper.Identifikator;
import no.fint.model.resource.personvern.kodeverk.BehandlingsgrunnlagResource;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class BehandlingsgrunnlagServiceTest {

    private BehandlingsgrunnlagService behandlingsgrunnlagService;

    @BeforeEach
    void setUp() {
        behandlingsgrunnlagService = new BehandlingsgrunnlagService();
    }

    @Test
    void testGetBehandlingsgrunnlags() {
        BehandlingsgrunnlagResource resource1 = new BehandlingsgrunnlagResource();
        resource1.setKode("kode1");
        resource1.setNavn("navn1");
        Identifikator identifikator1 = new Identifikator();
        identifikator1.setIdentifikatorverdi("22");
        resource1.setSystemId(identifikator1);
        behandlingsgrunnlagService.addResource(resource1);

        BehandlingsgrunnlagResource resource2 = new BehandlingsgrunnlagResource();
        resource2.setKode("kode2");
        resource2.setNavn("navn2");
        Identifikator identifikator2 = new Identifikator();
        identifikator2.setIdentifikatorverdi("33");
        resource2.setSystemId(identifikator2);
        behandlingsgrunnlagService.addResource(resource2);

        List<Behandlingsgrunnlag> behandlingsgrunnlags = behandlingsgrunnlagService.getBehandlingsgrunnlags();


        assertEquals(2, behandlingsgrunnlags.size());
        assertEquals("kode1", behandlingsgrunnlags.get(0).getKode());
        assertEquals("navn1", behandlingsgrunnlags.get(0).getNavn());
        assertEquals("kode2", behandlingsgrunnlags.get(1).getKode());
        assertEquals("navn2", behandlingsgrunnlags.get(1).getNavn());
    }

}
