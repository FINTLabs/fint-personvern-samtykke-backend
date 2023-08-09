package no.fintlabs.resource.behandling;

import lombok.extern.slf4j.Slf4j;
import no.fint.model.resource.personvern.samtykke.BehandlingResource;
import no.fintlabs.utils.FintUtils;
import no.fintlabs.utils.OrgIdUtil;
import no.fintlabs.utils.ResourceCollection;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@Service
public class BehandlingService {

    private final ResourceCollection<BehandlingResource> behandlingResources;
    private final FintUtils fintUtils;

    public BehandlingService(FintUtils fintUtils) {
        this.fintUtils = fintUtils;
        behandlingResources = new ResourceCollection<>();
    }

    public List<Behandling> getBehandlinger(String orgName) {
        List<Behandling> behandlinger = new ArrayList<>();

        behandlingResources.getResources(OrgIdUtil.uniform(orgName)).forEach(behandlingResource -> {
            Behandling behandling = createBehandlingResource(behandlingResource);
            behandlinger.add(behandling);
        });

        return behandlinger;
    }

    private Behandling createBehandlingResource(BehandlingResource behandlingResource) {
        Behandling behandling = new Behandling();

        behandling.setId(behandlingResource.getSystemId().getIdentifikatorverdi());
        behandling.setAktiv(behandlingResource.getAktiv());
        behandling.setFormal(behandlingResource.getFormal());

        behandling.setTjenesteIds(fintUtils.getRelationIdsFromLinks(behandlingResource, "tjeneste"));
        behandling.setPersonopplysningIds(fintUtils.getRelationIdsFromLinks(behandlingResource, "personopplysning"));
        behandling.setBehandlingsgrunnlagIds(fintUtils.getRelationIdsFromLinks(behandlingResource, "behandlingsgrunnlag"));

        return behandling;
    }

    public void addResource(String orgId, BehandlingResource resource) {

        log.info("Received behandling for: " + resource.getSystemId().getIdentifikatorverdi());
        behandlingResources.put(OrgIdUtil.uniform(orgId), resource.getSystemId().getIdentifikatorverdi(), resource);
    }

}
