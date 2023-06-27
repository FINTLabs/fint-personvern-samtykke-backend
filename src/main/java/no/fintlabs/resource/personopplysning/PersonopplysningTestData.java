package no.fintlabs.resource.personopplysning;

import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class PersonopplysningTestData {

    private final List<Personopplysning> personopplysningList;

    public PersonopplysningTestData() {
        this.personopplysningList = new ArrayList<>();
        addTestData();

    }

    private void addTestData() {
        for (int i = 0; i < 3; i++) {
            Personopplysning personopplysning = createPersonopplysning(i);
            personopplysningList.add(personopplysning);
        }
    }

    private Personopplysning createPersonopplysning(int id) {
        Personopplysning personopplysning = new Personopplysning();
        personopplysning.setId(id);
        personopplysning.setKode(setKode(id));
        personopplysning.setNavn("test data");
        return personopplysning;
    }

    private String setKode(int id) {
        return switch (id) {
            case 1 -> "email";
            case 2 -> "phone";
            case 3 -> "picture";
            default -> "test";
        };
    }

    public List<Personopplysning> getList() {
        return personopplysningList;
    }

    public void addResource(Personopplysning resource) {
        personopplysningList.add(resource);
    }

}
