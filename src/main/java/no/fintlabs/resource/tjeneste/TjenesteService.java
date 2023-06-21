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

        tjenesteResources.forEach(tjenesteResource -> {
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

    public void addTjeneste(TjenesteResource tjenesteResource) {
        tjenesteResources.add(tjenesteResource);
    }
}
