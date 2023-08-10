package no.fintlabs.resource.tjeneste;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequestMapping("/tjeneste")
@RestController
public class TjenesteController {

    private final TjenesteService tjenesteService;

    public TjenesteController(TjenesteService tjenesteService) {
        this.tjenesteService = tjenesteService;
    }

    @GetMapping("/{orgName}")
    public ResponseEntity<List<Tjeneste>> getTjenesteResources(@PathVariable String orgName) {
        return ResponseEntity.ok(tjenesteService.getTjenester(orgName));
    }

    @PostMapping("/{orgName}")
    public ResponseEntity<Tjeneste> createTjeneste(@PathVariable String orgName, @RequestBody Tjeneste tjeneste){
        return ResponseEntity.ok(tjenesteService.create(orgName, tjeneste));
    }

    @GetMapping("status/{corrId}")
    public ResponseEntity status (@PathVariable String corrId){
        return tjenesteService.status(corrId);
    }
}
