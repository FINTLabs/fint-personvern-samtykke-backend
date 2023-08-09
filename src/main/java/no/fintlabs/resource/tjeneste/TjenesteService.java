package no.fintlabs.resource.tjeneste;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import no.fint.model.resource.personvern.samtykke.TjenesteResource;
import no.fintlabs.utils.FintUtils;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@Service
public class TjenesteService {

    private final Map<String, TjenesteResource> tjenesteResources;
    private final FintUtils fintUtils;

    public TjenesteService(FintUtils fintUtils) {
        this.fintUtils = fintUtils;
        tjenesteResources = new HashMap<>();
    }

    public List<Tjeneste> getTjenester() {
        List<Tjeneste> tjenester = new ArrayList<>();

        tjenesteResources.values().forEach(resource -> {
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
        log.info("Received tjeneste for: "+ resource.getSystemId().getIdentifikatorverdi());
        tjenesteResources.put(resource.getSystemId().getIdentifikatorverdi(), resource);
    }
}
