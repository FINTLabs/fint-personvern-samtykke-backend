package no.fintlabs.resource.tjeneste;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import no.fint.model.resource.personvern.samtykke.TjenesteResource;
import no.fintlabs.kafka.common.topic.pattern.FormattedTopicComponentPattern;
import no.fintlabs.kafka.entity.EntityConsumerFactoryService;
import no.fintlabs.kafka.entity.topic.EntityTopicNamePatternParameters;
import no.fintlabs.utils.OrgIdUtil;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.nio.charset.StandardCharsets;

@Slf4j
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
                        .resource(FormattedTopicComponentPattern.anyOf("personvern-samtykke-tjeneste"))
                        .build()
        );
    }

    private void processEntity(ConsumerRecord<String, TjenesteResource> resource) {
        String corrId = null;
        if (resource.headers().lastHeader("event-corr-id") != null){
            corrId = new String(resource.headers().lastHeader("event-corr-id").value(), StandardCharsets.UTF_8);
            log.debug("Adding corrId to EntityResponseCache: {}", corrId);
        }
        tjenesteService.addResource(OrgIdUtil.getFromTopic(resource.topic()), resource.value(), corrId);
    }

}
