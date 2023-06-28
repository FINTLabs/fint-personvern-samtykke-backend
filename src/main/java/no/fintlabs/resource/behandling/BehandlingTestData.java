package no.fintlabs.resource.behandling;

import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class BehandlingTestData {

    private final Map<Integer, Behandling> behandlingMap;

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
        behandling.setId(id);
        behandling.setAktiv(setAktiv(id));
        behandling.setFormal("test formal");
        behandling.setBehandlingsgrunnlagIds(List.of(1));
        behandling.setPersonopplysningIds(List.of(1, 2, 3));
        behandling.setTjenesteIds(List.of(1, 2, 3));
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

    public boolean exists(int systemId) {
        return behandlingMap.containsKey(systemId);
    }

    public Behandling updateBehandling(int systemId, boolean aktiv) {
        Behandling behandling = behandlingMap.get(systemId);
        behandling.setAktiv(aktiv);
        return behandling;
    }
}
