package no.fintlabs.resource.tjeneste;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RequestMapping("/tjeneste")
@RestController
public class TjenesteController {

    private final TjenesteService tjenesteService;

    public TjenesteController(TjenesteService tjenesteService) {
        this.tjenesteService = tjenesteService;
    }

    @GetMapping
    public ResponseEntity<List<Tjeneste>> getTjenesteResources(@PathVariable String orgName) {
        return ResponseEntity.ok(tjenesteService.getTjenester(orgName));
    }
}
