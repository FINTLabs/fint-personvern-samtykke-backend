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

    public TjenesteService(EventStatusService eventStatusService, KafkaProducer kafkaProducer) {
        this.eventStatusService = eventStatusService;
        this.kafkaProducer = kafkaProducer;
        tjenesteResources = new ResourceCollection<>();
    }

    public List<Tjeneste> getTjenester(String orgName) {
        List<Tjeneste> tjenester = new ArrayList<>();

        tjenesteResources.getResources(OrgIdUtil.uniform(orgName)).forEach(resource -> {
            Tjeneste tjeneste = TjenesteMapper.createTjeneste(resource);
            tjenester.add(tjeneste);
        });

        return tjenester;
    }

    public void addResource(String orgId, TjenesteResource resource, String corrId) {
        log.info("Received tjeneste for: " + resource.getSystemId().getIdentifikatorverdi());
        tjenesteResources.put(OrgIdUtil.uniform(orgId), resource.getSystemId().getIdentifikatorverdi(), resource);
        eventStatusService.update(corrId);
    }

    public String create(String orgName, Tjeneste tjeneste) {
        RequestFintEvent requestFintEvent = kafkaProducer.sendEvent(OperationType.CREATE, "tjeneste", orgName, TjenesteMapper.createTjenesteResource(tjeneste));
        eventStatusService.add(requestFintEvent.getCorrId());
        return requestFintEvent.getCorrId();
    }

    public boolean status(String corrId) {
        log.info("Reached status in tjeneste service for corrId:" + corrId);
        return eventStatusService.get(corrId);
    }
}
