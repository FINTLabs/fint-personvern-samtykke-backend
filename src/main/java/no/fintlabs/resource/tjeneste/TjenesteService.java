package no.fintlabs.resource.tjeneste;

import lombok.extern.slf4j.Slf4j;
import no.fint.model.resource.personvern.samtykke.TjenesteResource;
import no.fintlabs.adapter.models.OperationType;
import no.fintlabs.adapter.models.RequestFintEvent;
import no.fintlabs.config.ApplicationProperties;
import no.fintlabs.config.Endpoints;
import no.fintlabs.resource.behandling.Behandling;
import no.fintlabs.utils.*;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.List;

@Slf4j
@Service
public class TjenesteService {

    private final EventStatusService eventStatusService;
    private final KafkaProducer kafkaProducer;
    private final ResourceCollection<TjenesteResource> tjenesteResources;
    private final TjenesteMapper tjenesteMapper;
    private final ApplicationProperties applicationProperties;

    public TjenesteService(EventStatusService eventStatusService, KafkaProducer kafkaProducer, TjenesteMapper tjenesteMapper, ApplicationProperties applicationProperties) {
        this.eventStatusService = eventStatusService;
        this.kafkaProducer = kafkaProducer;
        this.tjenesteMapper = tjenesteMapper;
        this.applicationProperties = applicationProperties;
        tjenesteResources = new ResourceCollection<>();
    }

    public List<Tjeneste> getTjenester(String orgName) {
        return tjenesteResources
                .getResources(OrgIdUtil.uniform(orgName))
                .stream()
                .map(resource -> tjenesteMapper.toTjeneste(resource, orgName))
                .toList();
    }

    public void addResource(String orgId, TjenesteResource resource, String corrId) {
        tjenesteResources.put(OrgIdUtil.uniform(orgId), resource.getSystemId().getIdentifikatorverdi(), resource);
        eventStatusService.update(corrId);
    }

    public String create(String orgName, Tjeneste tjeneste) {
        if (!StringUtils.hasText(tjeneste.getNavn())) throw new IllegalArgumentException("Name required");
        RequestFintEvent requestFintEvent = kafkaProducer.sendEvent(OperationType.CREATE, "tjeneste", orgName, tjenesteMapper.toTjenesteResource(tjeneste));
        eventStatusService.add(requestFintEvent.getCorrId());
        return requestFintEvent.getCorrId();
    }

    public boolean status(String corrId) {
        return eventStatusService.get(corrId);
    }

    public void updateTjeneste(String orgName, Behandling behandling) {
        String tjenesteId = behandling.getTjenesteIds().get(0);
        tjenesteResources.getResource(orgName, tjenesteId).ifPresentOrElse(
                tjeneste -> {
                    log.info("Update tjeneste with relation to behandling. Sending update to Kafka.");
                    tjeneste.addBehandling(FintUtils.createLink(applicationProperties.getBaseUrl(), Endpoints.BEHANDLING, behandling.getId()));
                    kafkaProducer.sendEvent(OperationType.UPDATE, "tjeneste", orgName, tjeneste);
                },
                () -> log.warn("unexpected result. Did not find tjeneste with id: " + tjenesteId)
        );
    }
}
