package no.fintlabs.resource.behandlingsgrunnlag;

import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class BehandlingsgrunnlagTestData {

    private final List<Behandlingsgrunnlag> behandlingsgrunnlagList;

    public BehandlingsgrunnlagTestData() {
        this.behandlingsgrunnlagList = new ArrayList<>();
        addTestData();

    }

    private void addTestData() {
        Behandlingsgrunnlag behandlingsgrunnlag = createBehandlingsgrunnlag();
        behandlingsgrunnlagList.add(behandlingsgrunnlag);
    }

    private Behandlingsgrunnlag createBehandlingsgrunnlag() {
        Behandlingsgrunnlag behandlingsgrunnlag = new Behandlingsgrunnlag();
        behandlingsgrunnlag.setId(1);
        behandlingsgrunnlag.setNavn("testNavn");
        behandlingsgrunnlag.setKode("TEST");
        return behandlingsgrunnlag;
    }

    public List<Behandlingsgrunnlag> getList() {
        return behandlingsgrunnlagList;
    }

    public void addResource(Behandlingsgrunnlag resource) {
        behandlingsgrunnlagList.add(resource);
    }

}
