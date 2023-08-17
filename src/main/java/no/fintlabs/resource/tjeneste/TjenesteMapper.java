package no.fintlabs.resource.tjeneste;

import no.fint.model.felles.kompleksedatatyper.Identifikator;
import no.fint.model.resource.personvern.samtykke.TjenesteResource;
import no.fintlabs.config.ApplicationProperties;
import no.fintlabs.config.Endpoints;
import no.fintlabs.utils.FintUtils;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.UUID;

@Service
public class TjenesteMapper {

    private final ApplicationProperties applicationProperties;

    public TjenesteMapper(ApplicationProperties applicationProperties) {
        this.applicationProperties = applicationProperties;
    }

    public Tjeneste toTjeneste(TjenesteResource resource, String orgId) {
        Tjeneste tjeneste = new Tjeneste();
        tjeneste.setOrgId(orgId);

        tjeneste.setId(resource.getSystemId().getIdentifikatorverdi());
        tjeneste.setNavn(resource.getNavn());
        tjeneste.setBehandlingIds(FintUtils.getRelationIdsFromLinks(resource, "behandling"));

        return tjeneste;
    }

    public TjenesteResource toTjenesteResource(Tjeneste tjeneste) {
        TjenesteResource tjenesteResource = new TjenesteResource();
        Identifikator identifikator = new Identifikator();
        identifikator.setIdentifikatorverdi(StringUtils.hasText(tjeneste.getId()) ? tjeneste.getId() : UUID.randomUUID().toString());
        tjenesteResource.setSystemId(identifikator);
        tjenesteResource.setNavn(tjeneste.getNavn());
        tjeneste.getBehandlingIds().forEach(id -> tjenesteResource.addBehandling(FintUtils.createLink(applicationProperties.getBaseUrl(), Endpoints.BEHANDLING, id)));

        return tjenesteResource;
    }
}
