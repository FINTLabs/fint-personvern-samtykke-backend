package no.fintlabs.resource.tjeneste;

import lombok.extern.slf4j.Slf4j;
import no.fint.model.resource.personvern.samtykke.TjenesteResource;
import no.fintlabs.utils.FintUtils;
import no.fintlabs.utils.OrgIdUtil;
import no.fintlabs.utils.ResourceCollection;
import org.springframework.stereotype.Service;

import java.util.*;

@Slf4j
@Service
public class TjenesteService {

    private final ResourceCollection<TjenesteResource> tjenesteResources;
    private final FintUtils fintUtils;

    public TjenesteService(FintUtils fintUtils) {
        this.fintUtils = fintUtils;
        tjenesteResources = new ResourceCollection<>();
    }

    public List<Tjeneste> getTjenester(String orgName) {
        List<Tjeneste> tjenester = new ArrayList<>();

        tjenesteResources.getResources(OrgIdUtil.uniform(orgName)).forEach(resource -> {
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

    public void addResource(String orgId, TjenesteResource resource) {
        log.info("Received tjeneste for: " + resource.getSystemId().getIdentifikatorverdi());
        tjenesteResources.put(OrgIdUtil.uniform(orgId), resource.getSystemId().getIdentifikatorverdi(), resource);
    }
}
