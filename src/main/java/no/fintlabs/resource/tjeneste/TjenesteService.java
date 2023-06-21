package no.fintlabs.resource.tjeneste;

import lombok.extern.slf4j.Slf4j;
import no.fint.model.resource.personvern.samtykke.TjenesteResource;
import no.fintlabs.utils.FintUtils;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
public class TjenesteService {

    private final List<TjenesteResource> tjenesteResources;

    private final FintUtils fintUtils;

    public TjenesteService(FintUtils fintUtils) {
        this.tjenesteResources = new ArrayList<>();
        this.fintUtils = fintUtils;
    }

    public List<Tjeneste> getTjenester() {
        List<Tjeneste> tjenester = new ArrayList<>();

        getTjenesteResources().forEach(tjenesteResource -> {
            Tjeneste tjeneste = createTjeneste(tjenesteResource);
            tjenester.add(tjeneste);
        });

        return tjenester;
    }

    private Tjeneste createTjeneste(TjenesteResource tjenesteResource) {
        Tjeneste tjeneste = new Tjeneste();
        tjeneste.setId(tjenesteResource.getSystemId().getIdentifikatorverdi());
        tjeneste.setName(tjenesteResource.getNavn());
        tjeneste.setBehandlingIds(fintUtils.getRelationIdsFromLinks(tjenesteResource, "behandling"));
        return tjeneste;
    }

    private List<TjenesteResource> getTjenesteResources() {
        return tjenesteResources;
    }

    public void addTjeneste(TjenesteResource tjenesteResource) {
        tjenesteResources.add(tjenesteResource);
    }
}
