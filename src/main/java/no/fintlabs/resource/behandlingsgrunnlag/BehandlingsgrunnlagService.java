package no.fintlabs.resource.behandlingsgrunnlag;

import lombok.RequiredArgsConstructor;
import no.fint.model.resource.personvern.kodeverk.BehandlingsgrunnlagResource;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@RequiredArgsConstructor
@Service
public class BehandlingsgrunnlagService {

    private final List<BehandlingsgrunnlagResource> behandlingsgrunnlagResources;

    public List<Behandlingsgrunnlag> getBehandlingsgrunnlags() {
        List<Behandlingsgrunnlag> behandlingsgrunnlags = new ArrayList<>();

        behandlingsgrunnlagResources.forEach(resource -> {
            Behandlingsgrunnlag behandlingsgrunnlag = createBehandlingsgrunnlag(resource);
            behandlingsgrunnlags.add(behandlingsgrunnlag);
        });

        return behandlingsgrunnlags;
    }

    private Behandlingsgrunnlag createBehandlingsgrunnlag(BehandlingsgrunnlagResource resource) {
        Behandlingsgrunnlag behandlingsgrunnlag = new Behandlingsgrunnlag();

        behandlingsgrunnlag.setId(resource.getSystemId().getIdentifikatorverdi());
        behandlingsgrunnlag.setCode(resource.getKode());
        behandlingsgrunnlag.setName(resource.getNavn());

        return behandlingsgrunnlag;
    }

    public void addResource(BehandlingsgrunnlagResource behandlingsgrunnlagResource) {
        behandlingsgrunnlagResources.add(behandlingsgrunnlagResource);
    }

}
