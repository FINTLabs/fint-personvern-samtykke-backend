package no.fintlabs.utils;

import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
public class EventStatusService {
    private final Map<String, Boolean> status;

    public EventStatusService() {
        status = new HashMap<>();
    }

    public void add(String corrId) {
        status.put(corrId, false);
    }

    public void update(String corrId) {
        status.put(corrId, true);
    }

    public boolean get(String corrId) {
        return status.getOrDefault(corrId,false);
    }
}
