package no.fintlabs.resource.behandling;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import no.fint.model.resource.personvern.samtykke.BehandlingResource;
import no.fintlabs.utils.FintUtils;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@RequiredArgsConstructor
@Service
public class BehandlingService {

    private final List<BehandlingResource> behandlingResources;
    private final FintUtils fintUtils;

    public List<Behandling> getBehandlinger() {
        List<Behandling> behandlinger = new ArrayList<>();

        behandlingResources.forEach(behandlingResource -> {
            Behandling behandling = createBehandlingResource(behandlingResource);
            behandlinger.add(behandling);
        });

        return behandlinger;
    }

    private Behandling createBehandlingResource(BehandlingResource behandlingResource) {
//        Behandling behandling = new Behandling();

//        behandling.setId(behandlingResource.getSystemId().getIdentifikatorverdi());
//        behandling.setAktiv(behandlingResource.getAktiv());
//        behandling.setFormal(behandlingResource.getFormal());

//        behandling.setTjenesteIds(fintUtils.getRelationIdsFromLinks(behandlingResource, "tjeneste"));
//        behandling.setPersonopplysningIds(fintUtils.getRelationIdsFromLinks(behandlingResource, "personopplysning"));
//        behandling.setBehandlingsgrunnlagIds(fintUtils.getRelationIdsFromLinks(behandlingResource, "behandlingsgrunnlag"));

        return null;
    }

    public void addResource(BehandlingResource resource) {
        log.info(resource.toString());
        behandlingResources.add(resource);
    }

}
