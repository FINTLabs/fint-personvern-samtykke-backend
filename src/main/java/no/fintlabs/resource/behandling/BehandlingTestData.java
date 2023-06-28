package no.fintlabs.resource.behandling;

import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class BehandlingTestData {

    private final Map<String, Behandling> behandlingMap;

    public BehandlingTestData() {
        this.behandlingMap = new HashMap<>();
        addTestData();

    }

    private void addTestData() {
        for (int i = 1; i < 4; i++) {
            Behandling behandling = createBehandling(i);
            behandlingMap.put(behandling.getId(), behandling);
        }
    }

    private Behandling createBehandling(int id) {
        Behandling behandling = new Behandling();
        behandling.setId(String.valueOf(id));
        behandling.setAktiv(setAktiv(id));
        behandling.setFormal("test formal");
        behandling.setBehandlingsgrunnlagIds(List.of("1"));
        behandling.setPersonopplysningIds(List.of("1", "2", "3"));
        behandling.setTjenesteIds(List.of("1", "2", "3"));
        return behandling;
    }

    private Boolean setAktiv(int id) {
        return id == 2;
    }

    public List<Behandling> getBehandlingMap() {
        return new ArrayList<>(behandlingMap.values());
    }

    public void addBehandling(Behandling behandling) {
        behandlingMap.put(behandling.getId(), behandling);
    }

    public boolean exists(String systemId) {
        return behandlingMap.containsKey(systemId);
    }

    public Behandling updateBehandling(String systemId, boolean aktiv) {
        Behandling behandling = behandlingMap.get(systemId);
        behandling.setAktiv(aktiv);
        return behandling;
    }

    public Behandling createBehandling(BehandlingRequestPayload requestPayload) {
        Behandling behandling = new Behandling();
        String systemId = UUID.randomUUID().toString();

        behandling.setId(systemId);
        behandling.setAktiv(requestPayload.getAktiv());
        behandling.setFormal(requestPayload.getFormal());
        behandling.setTjenesteIds(List.of(requestPayload.getTjenesteId()));
        behandling.setBehandlingsgrunnlagIds(List.of(requestPayload.getBehandlingsgrunnlagId()));
        behandling.setPersonopplysningIds(List.of(requestPayload.getPersonopplysningId()));

        behandlingMap.put(systemId, behandling);
        return behandling;
    }
}
