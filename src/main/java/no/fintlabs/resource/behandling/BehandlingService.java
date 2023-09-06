package no.fintlabs.resource.behandling;

import lombok.extern.slf4j.Slf4j;
import no.fint.model.resource.personvern.samtykke.BehandlingResource;
import no.fint.model.resource.personvern.samtykke.BehandlingResources;
import no.fintlabs.adapter.models.OperationType;
import no.fintlabs.adapter.models.RequestFintEvent;
import no.fintlabs.resource.tjeneste.TjenesteService;
import no.fintlabs.utils.EventStatusService;
import no.fintlabs.utils.KafkaProducer;
import no.fintlabs.utils.OrgIdUtil;
import no.fintlabs.utils.ResourceCollection;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.UUID;

@Slf4j
@Service
public class BehandlingService {

    private final ResourceCollection<BehandlingResource> behandlingResources;
    private final KafkaProducer kafkaProducer;
    private final EventStatusService eventStatusService;
    private final BehandlingMapper behandlingMapper;
    private final TjenesteService tjenesteService;

    public BehandlingService(KafkaProducer kafkaProducer, EventStatusService eventStatusService, BehandlingMapper behandlingMapper, TjenesteService tjenesteService, ResourceCollection<BehandlingResource> behandlingResources){
        this.kafkaProducer = kafkaProducer;
        this.eventStatusService = eventStatusService;
        this.behandlingMapper = behandlingMapper;
        this.tjenesteService = tjenesteService;
        this.behandlingResources = behandlingResources;
    }

    @Autowired
    public BehandlingService(KafkaProducer kafkaProducer, EventStatusService eventStatusService, BehandlingMapper behandlingMapper, TjenesteService tjenesteService) {
        this(kafkaProducer, eventStatusService, behandlingMapper, tjenesteService, new ResourceCollection<>());
    }

    public List<Behandling> getBehandlinger(String orgName) {
        return behandlingResources
                .getResources(OrgIdUtil.uniform(orgName))
                .stream()
                .map(behandlingResource -> behandlingMapper.toBehandling(behandlingResource, orgName))
                .toList();
    }

    public void addResource(String orgId, BehandlingResource resource) {
        log.info("Received behandling for: " + resource.getSystemId().getIdentifikatorverdi());
        behandlingResources.put(OrgIdUtil.uniform(orgId), resource.getSystemId().getIdentifikatorverdi(), resource);
    }

    public String create(String orgName, Behandling behandling) {
        if (!StringUtils.hasText(behandling.getFormal())) throw new IllegalArgumentException("Formal required");

        validate(behandling.getTjenesteIds(), "TjenesteIds");
        validate(behandling.getBehandlingsgrunnlagIds(), "BehandlingsgrunnlagIds");
        validate(behandling.getPersonopplysningIds(), "PersonopplysningIds");
        createIdIfNotPresent(behandling);

        RequestFintEvent requestFintEvent = kafkaProducer.sendEvent(OperationType.CREATE, "behandling", orgName, behandlingMapper.toBehandlingResource(behandling));

        eventStatusService.add(requestFintEvent.getCorrId());
        tjenesteService.updateTjeneste(orgName, behandling);

        return requestFintEvent.getCorrId();
    }

    private void createIdIfNotPresent(Behandling behandling) {
        if (!StringUtils.hasText(behandling.getId())) {
            behandling.setId(UUID.randomUUID().toString());
        }
    }

    public void validate(List<String> list, String fieldName) {
        if (list == null || list.isEmpty()) throw new IllegalArgumentException(fieldName + " required");
    }

    public boolean status(String corrId) {
        return eventStatusService.get(corrId);
    }

    public String updateState(String orgName, String id, boolean aktiv) {
        Optional<BehandlingResource> result = behandlingResources.getResource(orgName, id);
        if (result.isEmpty()) throw new NoSuchElementException("Element not found: " + id);

        BehandlingResource behandlingResource = result.get();
        behandlingResource.setAktiv(aktiv);
        RequestFintEvent requestFintEvent = kafkaProducer.sendEvent(OperationType.CREATE, "behandling", orgName, behandlingResource);
        eventStatusService.add(requestFintEvent.getCorrId());
        return requestFintEvent.getCorrId();
    }
}
