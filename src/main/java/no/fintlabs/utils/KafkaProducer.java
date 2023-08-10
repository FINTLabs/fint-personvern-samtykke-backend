package no.fintlabs.utils;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import no.fintlabs.adapter.models.OperationType;
import no.fintlabs.adapter.models.RequestFintEvent;
import no.fintlabs.kafka.event.EventProducer;
import no.fintlabs.kafka.event.EventProducerFactory;
import no.fintlabs.kafka.event.EventProducerRecord;
import no.fintlabs.kafka.event.topic.EventTopicNameParameters;
import no.fintlabs.kafka.event.topic.EventTopicService;

import java.util.UUID;

public abstract class KafkaProducer {

    private static final int RETENTION_TIME_MS = 172800000;

    private final EventProducer<Object> eventProducer;


    private final EventTopicService eventTopicService;

    public KafkaProducer(EventProducerFactory eventProducerFactory, EventTopicService eventTopicService, String resourceName) {
        this.eventProducer = eventProducerFactory.createProducer(Object.class);
        this.eventTopicService = eventTopicService;
    }

    public RequestFintEvent sendEvent(OperationType operationType, String resourceName, String orgId, Object value) {

        RequestFintEvent requestFintEvent = new RequestFintEvent();
        requestFintEvent.setCorrId(UUID.randomUUID().toString());
        requestFintEvent.setOrgId(OrgIdUtil.uniformForKafka(orgId));
        requestFintEvent.setDomainName("personvern");
        requestFintEvent.setPackageName("samtykke");
        requestFintEvent.setResourceName(resourceName);
        requestFintEvent.setOperationType(operationType);
        requestFintEvent.setCreated(System.currentTimeMillis());
        requestFintEvent.setValue(convertToJson(value));

        String eventName = String.format("%s-%s-%s-%s",
                "personvern-samtykke-",
                resourceName,
                operationType.equals(OperationType.CREATE) ? "create" : "update",
                "request");

        EventTopicNameParameters topicNameParameters = EventTopicNameParameters
                .builder()
                .orgId(OrgIdUtil.uniformForKafka(orgId))
                .domainContext("fint-core")
                .eventName(eventName)
                .build();

        eventTopicService.ensureTopic(topicNameParameters, RETENTION_TIME_MS);

        eventProducer.send(
                EventProducerRecord.builder()
                        .topicNameParameters(topicNameParameters)
                        .value(requestFintEvent)
                        .build()
        );

        return requestFintEvent;
    }

    private String convertToJson(Object body) {
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            return objectMapper.writer().writeValueAsString(body);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }
}
