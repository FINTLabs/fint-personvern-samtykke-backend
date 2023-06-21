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

    public PersonopplysningController(PersonopplysningService personopplysningService) {
        this.personopplysningService = personopplysningService;
    }

    @GetMapping
    public ResponseEntity<List<Personopplysning>> getPersonopplysning() {
        return ResponseEntity.ok(personopplysningService.getPersonopplysning());
    }

}
