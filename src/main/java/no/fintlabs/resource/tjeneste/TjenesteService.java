package no.fintlabs.resource.tjeneste;

import lombok.extern.slf4j.Slf4j;
import no.fint.model.felles.kompleksedatatyper.Identifikator;
import no.fint.model.resource.personvern.samtykke.TjenesteResource;
import no.fintlabs.adapter.models.OperationType;
import no.fintlabs.adapter.models.RequestFintEvent;
import no.fintlabs.utils.*;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
public class TjenesteService {

    private final EventStatusService eventStatusService;
    private final KafkaProducer kafkaProducer;
    private final ResourceCollection<TjenesteResource> tjenesteResources;
    private final FintUtils fintUtils;

    public TjenesteService(EventStatusService eventStatusService, KafkaProducer kafkaProducer, FintUtils fintUtils) {
        this.eventStatusService = eventStatusService;
        this.kafkaProducer = kafkaProducer;
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

    private TjenesteResource createTjenesteResource(Tjeneste tjeneste) {
        TjenesteResource tjenesteResource = new TjenesteResource();
        Identifikator identifikator = new Identifikator();
        identifikator.setIdentifikatorverdi(tjeneste.getId());
        tjenesteResource.setSystemId(identifikator);
        tjeneste.setNavn(tjenesteResource.getNavn());
        //TODO: tjeneste.setBehandlingIds(fintUtils.getRelationIdsFromLinks(resource, "behandling"));

        return tjenesteResource;
    }

    public void addResource(String orgId, TjenesteResource resource, String corrId) {
        log.info("Received tjeneste for: " + resource.getSystemId().getIdentifikatorverdi());
        tjenesteResources.put(OrgIdUtil.uniform(orgId), resource.getSystemId().getIdentifikatorverdi(), resource);
        eventStatusService.update(corrId);
    }

    public String create(String orgName, Tjeneste tjeneste) {
        RequestFintEvent requestFintEvent = kafkaProducer.sendEvent(OperationType.CREATE, "tjeneste", orgName, createTjenesteResource(tjeneste));
        eventStatusService.add(requestFintEvent.getCorrId());
        //TODO: Fulstendig location url:
        return requestFintEvent.getCorrId();
    }

    public boolean status(String corrId) {
        return eventStatusService.get(corrId);
    }
}
