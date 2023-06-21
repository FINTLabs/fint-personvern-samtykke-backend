package no.fintlabs.behandling;

import no.fint.model.resource.personvern.samtykke.BehandlingResource;
import no.fint.model.resource.personvern.samtykke.BehandlingResources;
import no.fintlabs.utils.FintUtils;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.util.ArrayList;
import java.util.List;

@Service
public class BehandlingService {

    private final FintUtils fintUtils;

    private final WebClient webClient;

    public BehandlingService(FintUtils fintUtils, WebClient webClient) {
        this.webClient = webClient;
        this.fintUtils = fintUtils;
    }

    public List<Behandling> getBehandlinger() {
        List<Behandling> behandlinger = new ArrayList<>();

        fetchBehandlingResources().
                subscribe(behandlingResources -> behandlingResources
                        .getContent()
                        .forEach(behandlingResource -> {
                            Behandling behandling = createBehandlingResource(behandlingResource);
                            behandlinger.add(behandling);
                        }));

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

    private Mono<BehandlingResources> fetchBehandlingResources() {
        return webClient.get()
                .uri("/behandling")
                .retrieve()
                .bodyToMono(BehandlingResources.class);
    }

}
