package no.fintlabs.resource.behandling;

import lombok.extern.slf4j.Slf4j;
import no.fint.model.resource.personvern.samtykke.BehandlingResource;
import no.fintlabs.adapter.models.OperationType;
import no.fintlabs.adapter.models.RequestFintEvent;
import no.fintlabs.utils.EventStatusService;
import no.fintlabs.utils.KafkaProducer;
import no.fintlabs.utils.OrgIdUtil;
import no.fintlabs.utils.ResourceCollection;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
public class BehandlingService {

    private final ResourceCollection<BehandlingResource> behandlingResources;
    private final KafkaProducer kafkaProducer;
    private final EventStatusService eventStatusService;

    public BehandlingService(KafkaProducer kafkaProducer, EventStatusService eventStatusService) {
        this.kafkaProducer = kafkaProducer;
        this.eventStatusService = eventStatusService;
        behandlingResources = new ResourceCollection<>();
    }

    public List<Behandling> getBehandlinger(String orgName) {
        List<Behandling> behandlinger = new ArrayList<>();

        behandlingResources.getResources(OrgIdUtil.uniform(orgName)).forEach(behandlingResource -> {
            Behandling behandling = BehandlingMapper.createBehandling(behandlingResource);
            behandlinger.add(behandling);
        });

        return behandlinger;
    }

    public void addResource(String orgId, BehandlingResource resource) {

        log.info("Received behandling for: " + resource.getSystemId().getIdentifikatorverdi());
        behandlingResources.put(OrgIdUtil.uniform(orgId), resource.getSystemId().getIdentifikatorverdi(), resource);
    }

    public String create(String orgName, Behandling behandling) {
        RequestFintEvent requestFintEvent = kafkaProducer.sendEvent(OperationType.CREATE, "behandling", orgName, BehandlingMapper.createBehandlingResource(behandling));
        eventStatusService.add(requestFintEvent.getCorrId());
        return requestFintEvent.getCorrId();
    }

    public boolean status(String corrId) {
        return eventStatusService.get(corrId);
    }
}
