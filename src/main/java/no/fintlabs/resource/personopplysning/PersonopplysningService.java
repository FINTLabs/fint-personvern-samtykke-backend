package no.fintlabs.resource.personopplysning;

import lombok.extern.slf4j.Slf4j;
import no.fint.model.resource.personvern.kodeverk.PersonopplysningResource;
import org.springframework.stereotype.Service;

import java.util.*;

@Slf4j
@Service
public class PersonopplysningService {

    private final List<Personopplysning> personopplysnings;
    private final Map<String, PersonopplysningResource> personopplysningResources;

    public PersonopplysningService() {
        personopplysningResources = new HashMap<>();
        personopplysnings = new ArrayList<>();
    }


    public List<Personopplysning> getPersonopplysning() {
        //TODO: Remove filter (CT-386)
        personopplysningResources.values()
                .stream()
                .filter(p -> p.getSystemId().getIdentifikatorverdi().contains("utdanning/elev/elev"))
                .distinct()
                .forEach(resource -> {
            Personopplysning personopplysning = createPersonopplysning(resource);
            personopplysnings.add(personopplysning);
        });

        return personopplysnings;
    }

    private Personopplysning createPersonopplysning(PersonopplysningResource personopplysningResource) {
        Personopplysning personopplysning = new Personopplysning();

        personopplysning.setId(personopplysningResource.getSystemId().getIdentifikatorverdi());
        personopplysning.setKode(personopplysningResource.getKode());
        personopplysning.setNavn(personopplysningResource.getNavn());

        return personopplysning;
    }

    public void addResource(PersonopplysningResource resource) {
        log.info("Received personopplysnings for: " + resource.getSystemId().getIdentifikatorverdi());
        personopplysningResources.put(resource.getSystemId().getIdentifikatorverdi(), resource);
    }
}
