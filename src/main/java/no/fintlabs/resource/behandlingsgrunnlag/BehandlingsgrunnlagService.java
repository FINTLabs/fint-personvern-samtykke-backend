package no.fintlabs.resource.behandlingsgrunnlag;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import no.fint.model.resource.personvern.kodeverk.BehandlingsgrunnlagResource;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@RequiredArgsConstructor
@Service
public class BehandlingsgrunnlagService {

    private final List<BehandlingsgrunnlagResource> behandlingsgrunnlagResources;

    public List<Behandlingsgrunnlag> getBehandlingsgrunnlags() {
        List<Behandlingsgrunnlag> personopplysnings = new ArrayList<>();

        behandlingsgrunnlagResources.forEach(resource -> {
            Behandlingsgrunnlag personopplysning = createBehandlingsgrunnlag(resource);
            personopplysnings.add(personopplysning);
        });

        return personopplysnings;
    }

    private Behandlingsgrunnlag createBehandlingsgrunnlag(BehandlingsgrunnlagResource resource) {
        Behandlingsgrunnlag personopplysning = new Behandlingsgrunnlag();

        personopplysning.setId(resource.getSystemId().getIdentifikatorverdi());
        personopplysning.setKode(resource.getKode());
        personopplysning.setNavn(resource.getNavn());

        return personopplysning;
    }

    public void addResource(BehandlingsgrunnlagResource resource) {
        log.info(resource.toString());
        behandlingsgrunnlagResources.add(resource);
    }

}
