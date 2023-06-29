package no.fintlabs.resource.tjeneste;

import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class TjenesteTestData {

    private final Map<String, Tjeneste> tjenesteMap;

    public TjenesteTestData() {
        this.tjenesteMap = new HashMap<>();
        addTestData();

    }

    private void addTestData() {
        for (int i = 1; i < 3; i++) {
            Tjeneste tjeneste = createTjeneste(i);
            tjenesteMap.put(tjeneste.getId(), tjeneste);
        }
    }

    private Tjeneste createTjeneste(int id) {
        Tjeneste tjeneste = new Tjeneste();

        tjeneste.setId(String.valueOf(id));
        tjeneste.setNavn(setName(id));
        tjeneste.setBehandlingIds(setBehandlingIds(id));

        return tjeneste;
    }

    private List<String> setBehandlingIds(int id) {
        return switch (id) {
            case 1 -> List.of("1", "2");
            case 2 -> List.of("3");
            default -> List.of("1");
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
        return new ArrayList<>(tjenesteMap.values());
    }

    public void addResource(Tjeneste resource) {
        tjenesteMap.put(resource.getId(), resource);
    }

    public Tjeneste createTjeneste(String name) {
        Tjeneste tjeneste = new Tjeneste();

        tjeneste.setId(UUID.randomUUID().toString());
        tjeneste.setNavn(name);
        tjeneste.setBehandlingIds(new ArrayList<>());

        tjenesteMap.put(tjeneste.getId(), tjeneste);
        return tjeneste;
    }

    public Tjeneste getTjeneste(String systemId) {
        return tjenesteMap.get(systemId);
    }

    public void updateTjeneste(String systemId, Tjeneste tjeneste) {
        tjenesteMap.put(systemId, tjeneste);
    }
}
