package no.fintlabs.resource.behandling;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import no.fint.model.resource.personvern.samtykke.BehandlingResource;
import no.fintlabs.kafka.common.topic.pattern.FormattedTopicComponentPattern;
import no.fintlabs.kafka.entity.EntityConsumerFactoryService;
import no.fintlabs.kafka.entity.topic.EntityTopicNamePatternParameters;
import no.fintlabs.utils.OrgIdUtil;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;

@Slf4j
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
                consumerRecord -> processEntity(consumerRecord)
        ).createContainer(
                EntityTopicNamePatternParameters
                        .builder()
                        .orgId(FormattedTopicComponentPattern.any())
                        .domainContext(FormattedTopicComponentPattern.anyOf("fint-core"))
                        .resource(FormattedTopicComponentPattern.anyOf("personvern-samtykke-behandling"))
                        .build()
        );
    }

    private void processEntity(ConsumerRecord<String, BehandlingResource> resource) {
        behandlingService.addResource(OrgIdUtil.getFromTopic(resource.topic()), resource.value());
    }
}
