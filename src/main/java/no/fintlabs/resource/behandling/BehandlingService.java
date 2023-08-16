package no.fintlabs.resource.behandling;

import lombok.extern.slf4j.Slf4j;
import no.fint.model.resource.personvern.samtykke.BehandlingResource;
import no.fint.model.resource.personvern.samtykke.TjenesteResource;
import no.fintlabs.adapter.models.OperationType;
import no.fintlabs.adapter.models.RequestFintEvent;
import no.fintlabs.config.ApplicationProperties;
import no.fintlabs.config.Endpoints;
import no.fintlabs.resource.tjeneste.TjenesteService;
import no.fintlabs.utils.*;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Slf4j
@Service
public class BehandlingService {

    private final ResourceCollection<BehandlingResource> behandlingResources;
    private final KafkaProducer kafkaProducer;
    private final EventStatusService eventStatusService;
    private final BehandlingMapper behandlingMapper;
    private final TjenesteService tjenesteService;

    public BehandlingService(KafkaProducer kafkaProducer, EventStatusService eventStatusService, BehandlingMapper behandlingMapper, TjenesteService tjenesteService) {
        this.kafkaProducer = kafkaProducer;
        this.eventStatusService = eventStatusService;
        this.behandlingMapper = behandlingMapper;
        this.tjenesteService = tjenesteService;
        behandlingResources = new ResourceCollection<>();
    }

    public List<Behandling> getBehandlinger(String orgName) {
        List<Behandling> behandlinger = new ArrayList<>();

        behandlingResources.getResources(OrgIdUtil.uniform(orgName)).forEach(behandlingResource -> {
            Behandling behandling = behandlingMapper.toBehandling(behandlingResource, orgName);
            behandlinger.add(behandling);
        });

        return behandlinger;
    }

    public void addResource(String orgId, BehandlingResource resource) {

        log.info("Received behandling for: " + resource.getSystemId().getIdentifikatorverdi());
        behandlingResources.put(OrgIdUtil.uniform(orgId), resource.getSystemId().getIdentifikatorverdi(), resource);
    }

    public String create(String orgName, Behandling behandling) {
        if (!StringUtils.hasText(behandling.getFormal())) throw new IllegalArgumentException("Formal required");
        RequestFintEvent requestFintEvent = kafkaProducer.sendEvent(OperationType.CREATE, "behandling", orgName, behandlingMapper.toBehandlingResource(behandling));
        eventStatusService.add(requestFintEvent.getCorrId());
        tjenesteService.updateTjeneste(orgName, behandling);
        return requestFintEvent.getCorrId();
    }

    public boolean status(String corrId) {
        return eventStatusService.get(corrId);
    }
}
