package no.fintlabs.resource.tjeneste;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequestMapping("/tjeneste")
@RestController
public class TjenesteController {

    private final TjenesteService tjenesteService;
    private final TjenesteTestData tjenesteTestData;

    public TjenesteController(TjenesteService tjenesteService, TjenesteTestData tjenesteTestData) {
        this.tjenesteService = tjenesteService;
        this.tjenesteTestData = tjenesteTestData;
    }

    @GetMapping
    public ResponseEntity<List<Tjeneste>> getTjenesteResources() {
        return ResponseEntity.ok(tjenesteTestData.getList());
    }

    @PostMapping
    public ResponseEntity<Tjeneste> createTjeneste(@RequestBody String name) {
        return ResponseEntity.ok(tjenesteTestData.createTjeneste(name));
    }

}
