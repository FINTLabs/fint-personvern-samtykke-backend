package no.fintlabs.resource.behandlingsgrunnlag;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import no.fint.model.resource.personvern.kodeverk.BehandlingsgrunnlagResource;
import no.fintlabs.kafka.common.topic.pattern.FormattedTopicComponentPattern;
import no.fintlabs.kafka.entity.EntityConsumerFactoryService;
import no.fintlabs.kafka.entity.topic.EntityTopicNamePatternParameters;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;

@Slf4j
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
                consumerRecord -> processEntity(consumerRecord)
        ).createContainer(
                EntityTopicNamePatternParameters
                        .builder()
                        .orgId(FormattedTopicComponentPattern.any())
                        .domainContext(FormattedTopicComponentPattern.anyOf("fint-core"))
                        .resource(FormattedTopicComponentPattern.anyOf("personvern-kodeverk-behandlingsgrunnlag"))
                        .build()
        );
    }

    private void processEntity(ConsumerRecord<String, BehandlingsgrunnlagResource> resource) {
        log.info("Headers:" + resource.headers().toString());
        behandlingsgrunnlagService.addResource(resource.value());
    }

}
