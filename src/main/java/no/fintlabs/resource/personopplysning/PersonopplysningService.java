package no.fintlabs.resource.personopplysning;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import no.fint.model.resource.personvern.kodeverk.PersonopplysningResource;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@RequiredArgsConstructor
@Service
public class PersonopplysningService {

    private final List<PersonopplysningResource> personopplysningResources;

    public List<Personopplysning> getPersonopplysning() {
        List<Personopplysning> personopplysnings = new ArrayList<>();

        personopplysningResources.forEach(resource -> {
            Personopplysning personopplysning = createPersonopplysning(resource);
            personopplysnings.add(personopplysning);
        });

        return personopplysnings;
    }

    private Personopplysning createPersonopplysning(PersonopplysningResource personopplysningResource) {
        Personopplysning personopplysning = new Personopplysning();

//        personopplysning.setId(personopplysningResource.getSystemId().getIdentifikatorverdi());
        personopplysning.setKode(personopplysningResource.getKode());
        personopplysning.setNavn(personopplysningResource.getNavn());

        return personopplysning;
    }

    public void addResource(PersonopplysningResource resource) {
        log.info(resource.toString());
        personopplysningResources.add(resource);
    }
}
