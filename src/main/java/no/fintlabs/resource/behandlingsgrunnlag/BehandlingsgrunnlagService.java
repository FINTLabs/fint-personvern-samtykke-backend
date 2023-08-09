package no.fintlabs.resource.behandlingsgrunnlag;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import no.fint.model.resource.personvern.kodeverk.BehandlingsgrunnlagResource;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@Service
public class BehandlingsgrunnlagService {

    private final Map<String, BehandlingsgrunnlagResource> behandlingsgrunnlagResources;

    public BehandlingsgrunnlagService() {
        behandlingsgrunnlagResources = new HashMap<>();
    }

    public List<Behandlingsgrunnlag> getBehandlingsgrunnlags() {
        List<Behandlingsgrunnlag> behandlingsgrunnlags = new ArrayList<>();

        behandlingsgrunnlagResources.values().forEach(resource -> {
            Behandlingsgrunnlag behandlingsgrunnlag = createBehandlingsgrunnlag(resource);
            behandlingsgrunnlags.add(behandlingsgrunnlag);
        });

        return behandlingsgrunnlags;
    }

    private Behandlingsgrunnlag createBehandlingsgrunnlag(BehandlingsgrunnlagResource resource) {
        Behandlingsgrunnlag behandlingsgrunnlag = new Behandlingsgrunnlag();

        behandlingsgrunnlag.setId(resource.getSystemId().getIdentifikatorverdi());
        behandlingsgrunnlag.setKode(resource.getKode());
        behandlingsgrunnlag.setNavn(resource.getNavn());

        return behandlingsgrunnlag;
    }

    public void addResource(BehandlingsgrunnlagResource resource) {
        log.info("Received behandlingsgrunnlag for: " + resource.getSystemId().getIdentifikatorverdi());
        behandlingsgrunnlagResources.put(resource.getSystemId().getIdentifikatorverdi(), resource);
    }

}
