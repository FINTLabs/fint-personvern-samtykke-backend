package no.fintlabs.resource.behandling;

import lombok.extern.slf4j.Slf4j;
import no.fint.model.felles.kompleksedatatyper.Identifikator;
import no.fint.model.resource.personvern.samtykke.BehandlingResource;
import no.fintlabs.adapter.models.OperationType;
import no.fintlabs.adapter.models.RequestFintEvent;
import no.fintlabs.utils.*;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
public class BehandlingService {

    private final ResourceCollection<BehandlingResource> behandlingResources;
    private final FintUtils fintUtils;
    private final KafkaProducer kafkaProducer;
    private final EventStatusService eventStatusService;

    public BehandlingService(FintUtils fintUtils, KafkaProducer kafkaProducer, EventStatusService eventStatusService) {
        this.fintUtils = fintUtils;
        this.kafkaProducer = kafkaProducer;
        this.eventStatusService = eventStatusService;
        behandlingResources = new ResourceCollection<>();
    }

    public List<Behandling> getBehandlinger(String orgName) {
        List<Behandling> behandlinger = new ArrayList<>();

        behandlingResources.getResources(OrgIdUtil.uniform(orgName)).forEach(behandlingResource -> {
            Behandling behandling = createBehandling(behandlingResource);
            behandlinger.add(behandling);
        });

        return behandlinger;
    }

    private Behandling createBehandling(BehandlingResource behandlingResource) {
        Behandling behandling = new Behandling();

        behandling.setId(behandlingResource.getSystemId().getIdentifikatorverdi());
        behandling.setAktiv(behandlingResource.getAktiv());
        behandling.setFormal(behandlingResource.getFormal());

        behandling.setTjenesteIds(fintUtils.getRelationIdsFromLinks(behandlingResource, "tjeneste"));
        behandling.setPersonopplysningIds(fintUtils.getRelationIdsFromLinks(behandlingResource, "personopplysning"));
        behandling.setBehandlingsgrunnlagIds(fintUtils.getRelationIdsFromLinks(behandlingResource, "behandlingsgrunnlag"));

        return behandling;
    }

    private BehandlingResource createBehandlingResource(Behandling behandling) {
        BehandlingResource behandlingResource = new BehandlingResource();
        Identifikator identifikator = new Identifikator();
        identifikator.setIdentifikatorverdi(behandling.getId());
        behandlingResource.setFormal(behandling.getFormal());

        return behandlingResource;
    }

    public void addResource(String orgId, BehandlingResource resource) {

        log.info("Received behandling for: " + resource.getSystemId().getIdentifikatorverdi());
        behandlingResources.put(OrgIdUtil.uniform(orgId), resource.getSystemId().getIdentifikatorverdi(), resource);
    }

    public String create(String orgName, Behandling behandling) {
        RequestFintEvent requestFintEvent = kafkaProducer.sendEvent(OperationType.CREATE, "behandling", orgName, createBehandlingResource(behandling));
        eventStatusService.add(requestFintEvent.getCorrId());
        return requestFintEvent.getCorrId();
    }

    public boolean status(String corrId) {
        return eventStatusService.get(corrId);
    }

}
