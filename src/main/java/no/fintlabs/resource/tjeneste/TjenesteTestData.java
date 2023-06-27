package no.fintlabs.resource.tjeneste;

import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class TjenesteTestData {

    private final List<Tjeneste> tjenesteList;

    public TjenesteTestData() {
        this.tjenesteList = new ArrayList<>();
        addTestData();

    }

    private void addTestData() {
        for (int i = 1; i < 3; i++) {
            Tjeneste tjeneste = createTjeneste(i);
            tjenesteList.add(tjeneste);
        }
    }

    private Tjeneste createTjeneste(int id) {
        Tjeneste tjeneste = new Tjeneste();
        tjeneste.setId(id);
        tjeneste.setNavn(setName(id));
        tjeneste.setBehandlingIds(setBehandlingIds(id));
        return tjeneste;
    }

    private List<Integer> setBehandlingIds(int id) {
        return switch (id) {
            case 1 -> List.of(1, 2);
            case 2 -> List.of(3);
            default -> List.of(1);
        };
    }

    private String setName(int id) {
        return switch (id) {
            case 1 -> "Lånekassen";
            case 2 -> "FPVS";
            default -> "test";
        };
    }

    public List<Tjeneste> getList() {
        return tjenesteList;
    }

    public void addResource(Tjeneste resource) {
        tjenesteList.add(resource);
    }

}
