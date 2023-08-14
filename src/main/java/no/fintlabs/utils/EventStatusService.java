package no.fintlabs.utils;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
@Slf4j
public class EventStatusService {
    private final Map<String, Boolean> status;

    public EventStatusService() {
        status = new HashMap<>();
    }

    public void add(String corrId) {
        log.info("Adding a corrId to event status map: " + corrId);
        status.put(corrId, false);
    }

    public void update(String corrId) {
        status.put(corrId, true);
    }

    public boolean get(String corrId) {
        return status.getOrDefault(corrId,false);
    }
}
