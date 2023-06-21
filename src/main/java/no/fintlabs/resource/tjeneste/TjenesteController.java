package no.fintlabs.resource.tjeneste;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping("/tjeneste")
@RestController
public class TjenesteController {

    private final TjenesteService tjenesteService;


    public TjenesteController(TjenesteService tjenesteService) {
        this.tjenesteService = tjenesteService;
    }

    @GetMapping
    public ResponseEntity<?> getTjenesteResources() {
        return ResponseEntity.ok(tjenesteService.getTjenester());
    }

}
