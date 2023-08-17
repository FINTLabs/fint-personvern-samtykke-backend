package no.fintlabs.resource.behandlingsgrunnlag;

import lombok.extern.slf4j.Slf4j;
import no.fint.model.resource.personvern.kodeverk.BehandlingsgrunnlagResource;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@Service
public class BehandlingsgrunnlagService {

    private final Map<String, BehandlingsgrunnlagResource> behandlingsgrunnlagResources = new HashMap<>();

    public List<Behandlingsgrunnlag> getBehandlingsgrunnlags() {
        return behandlingsgrunnlagResources.values()
                .stream()
                .map(this::createBehandlingsgrunnlag)
                .toList();
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
