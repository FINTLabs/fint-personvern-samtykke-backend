package no.fintlabs.resource.personopplysning;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/personopplysning")
@RequiredArgsConstructor
public class PersonopplysningController {

    private final PersonopplysningService personopplysningService;

    @GetMapping
    public ResponseEntity<List<Personopplysning>> getPersonopplysning() {
        return ResponseEntity.ok(personopplysningService.getPersonopplysning());
    }

}
