package no.fintlabs.resource.tjeneste;

import lombok.RequiredArgsConstructor;
import no.fint.model.resource.personvern.samtykke.TjenesteResource;
import no.fintlabs.utils.FintUtils;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@RequiredArgsConstructor
@Service
public class TjenesteService {

    private final List<TjenesteResource> tjenesteResources;
    private final FintUtils fintUtils;

    public List<Tjeneste> getTjenester() {
        List<Tjeneste> tjenester = new ArrayList<>();

        tjenesteResources.forEach(resource -> {
            Tjeneste tjeneste = createTjeneste(resource);
            tjenester.add(tjeneste);
        });

        return tjenester;
    }

    private Tjeneste createTjeneste(TjenesteResource resource) {
        Tjeneste tjeneste = new Tjeneste();

        tjeneste.setId(resource.getSystemId().getIdentifikatorverdi());
        tjeneste.setNavn(resource.getNavn());
        tjeneste.setBehandlingIds(fintUtils.getRelationIdsFromLinks(resource, "behandling"));

        return tjeneste;
    }

    public void addResource(TjenesteResource resource) {
        tjenesteResources.add(resource);
    }
}
