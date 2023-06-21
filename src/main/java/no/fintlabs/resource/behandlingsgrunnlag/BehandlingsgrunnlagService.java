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

        behandlingsgrunnlagResources.forEach(behandlingsgrunnlagResource -> {
            Behandlingsgrunnlag behandlingsgrunnlag = createBehandlingsgrunnlag(behandlingsgrunnlagResource);
            behandlingsgrunnlags.add(behandlingsgrunnlag);
        });

        return behandlingsgrunnlags;
    }

    private Behandlingsgrunnlag createBehandlingsgrunnlag(BehandlingsgrunnlagResource behandlingsgrunnlagResource) {
        Behandlingsgrunnlag behandlingsgrunnlag = new Behandlingsgrunnlag();

        behandlingsgrunnlag.setId(behandlingsgrunnlagResource.getSystemId().getIdentifikatorverdi());
        behandlingsgrunnlag.setCode(behandlingsgrunnlagResource.getKode());
        behandlingsgrunnlag.setName(behandlingsgrunnlagResource.getNavn());

        return behandlingsgrunnlag;
    }

    public void addResource(BehandlingsgrunnlagResource behandlingsgrunnlagResource) {
        behandlingsgrunnlagResources.add(behandlingsgrunnlagResource);
    }

}
