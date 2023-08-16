package no.fintlabs.utils;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class FintUtilsTest {

    @Test
    void createLink() {
        assertEquals("https://api.felleskomponent.no/utdanning/elev/systemid/321", FintUtils.createLink("https://api.felleskomponent.no", "/utdanning/elev", "321").getHref());
        assertEquals("https://beta.felleskomponent.no/okonomi/faktura/mva/systemid/1000", FintUtils.createLink("https://beta.felleskomponent.no", "/okonomi/faktura/mva", "1000").getHref());
    }
}