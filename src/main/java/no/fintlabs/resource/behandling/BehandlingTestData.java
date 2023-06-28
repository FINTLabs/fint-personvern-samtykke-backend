package no.fintlabs.resource.behandling;

import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class BehandlingTestData {

    private final List<Behandling> behandlingList;

    public BehandlingTestData() {
        this.behandlingList = new ArrayList<>();
        addTestData();

    }

    private void addTestData() {
        for (int i = 1; i < 4; i++) {
            Behandling behandling = createBehandling(i);
            behandlingList.add(behandling);
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

    public List<Behandling> getBehandlingList() {
        return behandlingList;
    }

    public void addBehandling(Behandling behandling) {
        behandlingList.add(behandling);
    }

}
