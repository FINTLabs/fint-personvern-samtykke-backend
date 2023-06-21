package no.fintlabs.resource.tjeneste;

import lombok.Getter;
import no.fint.model.resource.personvern.samtykke.TjenesteResource;
import no.fintlabs.kafka.common.topic.pattern.FormattedTopicComponentPattern;
import no.fintlabs.kafka.entity.EntityConsumerFactoryService;
import no.fintlabs.kafka.entity.topic.EntityTopicNamePatternParameters;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;

@Getter
@Service
public class TjenesteKafkaConsumer {

    private final TjenesteService tjenesteService;

    private final EntityConsumerFactoryService entityConsumerFactoryService;

    public TjenesteKafkaConsumer(TjenesteService tjenesteService, EntityConsumerFactoryService entityConsumerFactoryService) {
        this.tjenesteService = tjenesteService;
        this.entityConsumerFactoryService = entityConsumerFactoryService;
    }

    @PostConstruct
    private void setupConsumer() {
        entityConsumerFactoryService.createFactory(
                TjenesteResource.class,
                consumerRecord -> processEntity(consumerRecord.value())
        ).createContainer(
                EntityTopicNamePatternParameters
                        .builder()
                        .orgId(FormattedTopicComponentPattern.any())
                        .resource(FormattedTopicComponentPattern.anyOf("personvern.samtykke.tjeneste"))
                        .build()
        );
    }

    private void processEntity(TjenesteResource resource) {
        tjenesteService.addTjeneste(resource);
    }

}
