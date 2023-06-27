package no.fintlabs.resource.personopplysning;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RequestMapping("/personopplysning")
@RestController
public class PersonopplysningController {

    private final PersonopplysningService personopplysningService;
    private final PersonopplysningTestData personopplysningTestData;

    public PersonopplysningController(PersonopplysningService personopplysningService, PersonopplysningTestData personopplysningTestData) {
        this.personopplysningService = personopplysningService;
        this.personopplysningTestData = personopplysningTestData;
    }

    @GetMapping
    public ResponseEntity<List<Personopplysning>> getPersonopplysning() {
        return ResponseEntity.ok(personopplysningTestData.getList());
    }

}
