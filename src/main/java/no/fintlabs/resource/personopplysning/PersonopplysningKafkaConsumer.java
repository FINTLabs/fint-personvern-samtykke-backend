package no.fintlabs.resource.personopplysning;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import no.fint.model.resource.personvern.kodeverk.PersonopplysningResource;
import no.fintlabs.kafka.common.topic.pattern.FormattedTopicComponentPattern;
import no.fintlabs.kafka.entity.EntityConsumerFactoryService;
import no.fintlabs.kafka.entity.topic.EntityTopicNamePatternParameters;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;

@RequiredArgsConstructor
@Getter
@Service
public class PersonopplysningKafkaConsumer {

    private final PersonopplysningService personopplysningService;
    private final EntityConsumerFactoryService entityConsumerFactoryService;

    @PostConstruct
    private void setupConsumer() {
        entityConsumerFactoryService.createFactory(
                PersonopplysningResource.class,
                consumerRecord -> processEntity(consumerRecord.value())
        ).createContainer(
                EntityTopicNamePatternParameters
                        .builder()
                        .orgId(FormattedTopicComponentPattern.any())
                        .resource(FormattedTopicComponentPattern.anyOf("personvern.kodeverk.personopplysning"))
                        .build()
        );
    }

    private void processEntity(PersonopplysningResource resource) {
        personopplysningService.addResource(resource);
    }

}
