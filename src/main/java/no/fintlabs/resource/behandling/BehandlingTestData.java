package no.fintlabs.resource.behandling;

import no.fintlabs.resource.tjeneste.Tjeneste;
import no.fintlabs.resource.tjeneste.TjenesteTestData;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class BehandlingTestData {

    private final Map<String, Behandling> behandlingMap;
    private final TjenesteTestData tjenesteTestData;

    public BehandlingTestData(TjenesteTestData tjenesteTestData) {
        this.tjenesteTestData = tjenesteTestData;
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
        return Behandling.builder()
                .id(String.valueOf(id))
                .aktiv(setAktiv(id))
                .formal("test")
                .behandlingsgrunnlagId("1")
                .personopplysningId("1")
                .tjenesteId("1")
                .build();
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
        String behandlingId = UUID.randomUUID().toString();
        String tjenesteId = requestPayload.getTjenesteId();

        Behandling behandling = createBehandling(requestPayload, behandlingId);
        updateTjenesteId(tjenesteId, behandlingId);
        behandlingMap.put(behandlingId, behandling);

        return behandling;
    }

    private Behandling createBehandling(BehandlingRequestPayload requestPayload, String behandlingId) {
        return Behandling.builder()
                .id(behandlingId)
                .aktiv(requestPayload.getAktiv())
                .formal(requestPayload.getFormal())
                .behandlingsgrunnlagId(requestPayload.getBehandlingsgrunnlagId())
                .personopplysningId(requestPayload.getPersonopplysningId())
                .tjenesteId(requestPayload.getTjenesteId())
                .build();
    }

    private void updateTjenesteId(String tjenesteId, String behandlingId) {
        Tjeneste tjeneste = tjenesteTestData.getTjeneste(tjenesteId);
        tjeneste.addBehandlingId(behandlingId);
    }
}
