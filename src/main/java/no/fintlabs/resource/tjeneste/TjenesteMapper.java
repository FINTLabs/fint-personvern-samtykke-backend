package no.fintlabs.resource.tjeneste;

import no.fint.model.felles.kompleksedatatyper.Identifikator;
import no.fint.model.resource.personvern.samtykke.TjenesteResource;
import no.fintlabs.utils.FintUtils;
import org.springframework.util.StringUtils;

import java.util.UUID;

public class TjenesteMapper {

    public static Tjeneste createTjeneste(TjenesteResource resource) {
        Tjeneste tjeneste = new Tjeneste();

        tjeneste.setId(resource.getSystemId().getIdentifikatorverdi());
        tjeneste.setNavn(resource.getNavn());
        tjeneste.setBehandlingIds(FintUtils.getRelationIdsFromLinks(resource, "behandling"));

        return tjeneste;
    }

    public static TjenesteResource createTjenesteResource(Tjeneste tjeneste) {
        TjenesteResource tjenesteResource = new TjenesteResource();
        Identifikator identifikator = new Identifikator();
        identifikator.setIdentifikatorverdi(StringUtils.hasText(tjeneste.getId()) ? tjeneste.getId() : UUID.randomUUID().toString());
        tjenesteResource.setSystemId(identifikator);
        tjeneste.setNavn(tjenesteResource.getNavn());
        //TODO: tjeneste.setBehandlingIds(fintUtils.getRelationIdsFromLinks(resource, "behandling"));

        return tjenesteResource;
    }
}
