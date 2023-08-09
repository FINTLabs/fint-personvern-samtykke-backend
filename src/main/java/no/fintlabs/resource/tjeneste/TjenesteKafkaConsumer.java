package no.fintlabs.resource.tjeneste;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import no.fint.model.resource.personvern.samtykke.TjenesteResource;
import no.fintlabs.kafka.common.topic.pattern.FormattedTopicComponentPattern;
import no.fintlabs.kafka.entity.EntityConsumerFactoryService;
import no.fintlabs.kafka.entity.topic.EntityTopicNamePatternParameters;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;

@RequiredArgsConstructor
@Getter
@Service
public class TjenesteKafkaConsumer {

    private final TjenesteService tjenesteService;
    private final EntityConsumerFactoryService entityConsumerFactoryService;

    @PostConstruct
    private void setupConsumer() {
        entityConsumerFactoryService.createFactory(
                TjenesteResource.class,
                consumerRecord -> processEntity(consumerRecord)
        ).createContainer(
                EntityTopicNamePatternParameters
                        .builder()
                        .orgId(FormattedTopicComponentPattern.any())
                        .domainContext(FormattedTopicComponentPattern.anyOf("fint-core"))
                        .resource(FormattedTopicComponentPattern.endingWith("tjeneste"))
                        .build()
        );
    }

    private void processEntity(ConsumerRecord<String, TjenesteResource> resource) {
        tjenesteService.addResource(resource.value());
    }

}
