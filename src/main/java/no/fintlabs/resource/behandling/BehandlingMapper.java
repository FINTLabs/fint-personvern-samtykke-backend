package no.fintlabs.resource.behandling;

import no.fint.model.felles.kompleksedatatyper.Identifikator;
import no.fint.model.resource.Link;
import no.fint.model.resource.personvern.samtykke.BehandlingResource;
import no.fintlabs.config.ApplicationProperties;
import no.fintlabs.config.Endpoints;
import no.fintlabs.utils.FintUtils;
import org.springframework.util.StringUtils;

import java.util.UUID;

public class BehandlingMapper {

    private final ApplicationProperties applicationProperties;

    public BehandlingMapper(ApplicationProperties applicationProperties) {
        this.applicationProperties = applicationProperties;
    }

    public Behandling toBehandling(BehandlingResource behandlingResource, String orgId) {
        Behandling behandling = new Behandling();

        behandling.setId(behandlingResource.getSystemId().getIdentifikatorverdi());
        behandling.setAktiv(behandlingResource.getAktiv());
        behandling.setFormal(behandlingResource.getFormal());
        behandling.setOrgId(orgId);

        behandling.setTjenesteIds(FintUtils.getRelationIdsFromLinks(behandlingResource, "tjeneste"));
        behandling.setPersonopplysningIds(FintUtils.getRelationIdsFromLinks(behandlingResource, "personopplysning"));
        behandling.setBehandlingsgrunnlagIds(FintUtils.getRelationIdsFromLinks(behandlingResource, "behandlingsgrunnlag"));

        return behandling;
    }

    public BehandlingResource toBehandlingResource(Behandling behandling) {
        BehandlingResource behandlingResource = new BehandlingResource();
        Identifikator identifikator = new Identifikator();
        identifikator.setIdentifikatorverdi(StringUtils.hasText(behandling.getId()) ? behandling.getId() : UUID.randomUUID().toString());
        identifikator.setIdentifikatorverdi(behandling.getId());
        behandlingResource.setFormal(behandling.getFormal());

        behandling.getTjenesteIds().forEach(id -> behandlingResource.addTjeneste(createLink(Endpoints.TJENESTE, id)));
        behandling.getBehandlingsgrunnlagIds().forEach(id -> behandlingResource.addBehandlingsgrunnlag(createLink(Endpoints.BEHNADLINGSGRUNNLAG, id)));
        behandling.getPersonopplysningIds().forEach(id -> behandlingResource.addPersonopplysning(createLink(Endpoints.PERSONOPPLYSNING, id)));

        return behandlingResource;
    }

    private Link createLink(String endpoints, String id) {
        return FintUtils.createLink(applicationProperties.getBaseUrl(), endpoints, id);
    }
}
