package no.fintlabs.resource.personopplysning;

import no.fint.model.felles.kompleksedatatyper.Identifikator;
import no.fint.model.resource.personvern.kodeverk.PersonopplysningResource;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class PersonopplysningServiceTest {
    private PersonopplysningService personopplysningService;

    @BeforeEach
    void setUp() {
        personopplysningService = new PersonopplysningService();
    }

    @Test
    void testGetPersonopplysningReturnsEmptyListWhenNoResourcesAdded() {
        List<Personopplysning> personopplysningList = personopplysningService.getPersonopplysning();
        assertEquals(0, personopplysningList.size());
    }

    @Test
    void testGetPersonopplysningReturnsCorrectListAfterAddingResources() {
        PersonopplysningResource resource1 = mock(PersonopplysningResource.class);
        Identifikator identifikator1 = mock(Identifikator.class);
        when(resource1.getSystemId()).thenReturn(identifikator1);
        when(identifikator1.getIdentifikatorverdi()).thenReturn("1");
        when(resource1.getKode()).thenReturn("kode1");
        when(resource1.getNavn()).thenReturn("navn1");

        PersonopplysningResource resource2 = mock(PersonopplysningResource.class);
        Identifikator identifikator2 = mock(Identifikator.class);
        when(resource2.getSystemId()).thenReturn(identifikator2);
        when(identifikator2.getIdentifikatorverdi()).thenReturn("2");
        when(resource2.getKode()).thenReturn("kode2");
        when(resource2.getNavn()).thenReturn("navn2");

        personopplysningService.addResource(resource1);
        personopplysningService.addResource(resource2);

        List<Personopplysning> personopplysningList = personopplysningService.getPersonopplysning();
        assertEquals(2, personopplysningList.size());
        assertEquals("1", personopplysningList.get(0).getId());
        assertEquals("kode1", personopplysningList.get(0).getKode());
        assertEquals("navn1", personopplysningList.get(0).getNavn());
        assertEquals("2", personopplysningList.get(1).getId());
        assertEquals("kode2", personopplysningList.get(1).getKode());
        assertEquals("navn2", personopplysningList.get(1).getNavn());
    }

    @Test
    void testAddResource() {
        PersonopplysningResource resource = mock(PersonopplysningResource.class);
        Identifikator identifikator = mock(Identifikator.class);
        when(resource.getSystemId()).thenReturn(identifikator);
        when(identifikator.getIdentifikatorverdi()).thenReturn("1");

        personopplysningService.addResource(resource);

        List<Personopplysning> personopplysningList = personopplysningService.getPersonopplysning();
        assertEquals(1, personopplysningList.size());
        assertEquals("1", personopplysningList.get(0).getId());
    }

}