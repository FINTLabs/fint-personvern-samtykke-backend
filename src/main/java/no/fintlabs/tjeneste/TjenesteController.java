package no.fintlabs.tjeneste;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TjenesteController {

    private final TjenesteService tjenesteService;


    public TjenesteController(TjenesteService tjenesteService) {
        this.tjenesteService = tjenesteService;
    }

    public ResponseEntity<?> getTjenesteResources() {
        return null;
    }

}
