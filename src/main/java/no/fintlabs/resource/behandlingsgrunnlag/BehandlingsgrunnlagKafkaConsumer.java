package no.fintlabs.resource.behandlingsgrunnlag;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import no.fint.model.resource.personvern.kodeverk.BehandlingsgrunnlagResource;
import no.fintlabs.kafka.common.topic.pattern.FormattedTopicComponentPattern;
import no.fintlabs.kafka.entity.EntityConsumerFactoryService;
import no.fintlabs.kafka.entity.topic.EntityTopicNamePatternParameters;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;

@RequiredArgsConstructor
@Getter
@Service
public class BehandlingsgrunnlagKafkaConsumer {

    private final BehandlingsgrunnlagService behandlingsgrunnlagService;
    private final EntityConsumerFactoryService entityConsumerFactoryService;

    @PostConstruct
    private void setupConsumer() {
        entityConsumerFactoryService.createFactory(
                BehandlingsgrunnlagResource.class,
                consumerRecord -> processEntity(consumerRecord.value())
        ).createContainer(
                EntityTopicNamePatternParameters
                        .builder()
                        .orgId(FormattedTopicComponentPattern.any())
                        .domainContext(FormattedTopicComponentPattern.anyOf("fint-core"))
                        .resource(FormattedTopicComponentPattern.anyOf("personvern-kodeverk-behandlingsgrunnlag"))
                        .build()
        );
    }

    private void processEntity(BehandlingsgrunnlagResource resource) {
        behandlingsgrunnlagService.addResource(resource);
    }

}
