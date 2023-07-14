package no.fintlabs.resource.behandling;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import no.fint.model.resource.personvern.samtykke.BehandlingResource;
import no.fintlabs.kafka.common.topic.pattern.FormattedTopicComponentPattern;
import no.fintlabs.kafka.entity.EntityConsumerFactoryService;
import no.fintlabs.kafka.entity.topic.EntityTopicNamePatternParameters;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;

@RequiredArgsConstructor
@Getter
@Service
public class BehandlingKafkaConsumer {

    private final BehandlingService behandlingService;
    private final EntityConsumerFactoryService entityConsumerFactoryService;

    @PostConstruct
    private void setupConsumer() {
        entityConsumerFactoryService.createFactory(
                BehandlingResource.class,
                consumerRecord -> processEntity(consumerRecord.value())
        ).createContainer(
                EntityTopicNamePatternParameters
                        .builder()
                        .orgId(FormattedTopicComponentPattern.any())
                        .domainContext(FormattedTopicComponentPattern.anyOf("fint-core"))
                        .resource(FormattedTopicComponentPattern.containing("personvern-samtykke-behandling"))
                        .build()
        );
    }

    private void processEntity(BehandlingResource resource) {
        behandlingService.addResource(resource);
    }

}
