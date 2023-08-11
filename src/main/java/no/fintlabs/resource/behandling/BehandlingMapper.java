package no.fintlabs.resource.behandling;

import no.fint.model.felles.kompleksedatatyper.Identifikator;
import no.fint.model.resource.personvern.samtykke.BehandlingResource;
import no.fintlabs.utils.FintUtils;

public class BehandlingMapper {

    public static Behandling createBehandling(BehandlingResource behandlingResource) {
        Behandling behandling = new Behandling();

        behandling.setId(behandlingResource.getSystemId().getIdentifikatorverdi());
        behandling.setAktiv(behandlingResource.getAktiv());
        behandling.setFormal(behandlingResource.getFormal());

        behandling.setTjenesteIds(FintUtils.getRelationIdsFromLinks(behandlingResource, "tjeneste"));
        behandling.setPersonopplysningIds(FintUtils.getRelationIdsFromLinks(behandlingResource, "personopplysning"));
        behandling.setBehandlingsgrunnlagIds(FintUtils.getRelationIdsFromLinks(behandlingResource, "behandlingsgrunnlag"));

        return behandling;
    }

    public static BehandlingResource createBehandlingResource(Behandling behandling) {
        BehandlingResource behandlingResource = new BehandlingResource();
        Identifikator identifikator = new Identifikator();
        identifikator.setIdentifikatorverdi(behandling.getId());
        behandlingResource.setFormal(behandling.getFormal());

        return behandlingResource;
    }
}
