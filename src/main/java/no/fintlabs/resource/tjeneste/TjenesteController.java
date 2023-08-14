package no.fintlabs.resource.tjeneste;

import lombok.extern.slf4j.Slf4j;
import no.fintlabs.utils.LocationHeader;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequestMapping("/tjeneste")
@RestController
@Slf4j
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
    public ResponseEntity<Tjeneste> createTjeneste(ServerHttpRequest request, @PathVariable String orgName, @RequestBody Tjeneste tjeneste) {
        String corrId = tjenesteService.create(orgName, tjeneste);
        log.info("Reached controller with corrId: " + corrId);
        return ResponseEntity.created(LocationHeader.get(corrId, request)).build();
    }

    @GetMapping("/status/{corrId}")
    public ResponseEntity<?> status(@PathVariable String corrId) {
        log.info("Reached controller finding status for corrId: " + corrId);

        boolean status = tjenesteService.status(corrId);

        log.info("Status return from tjeneste service: " + status);

        return status ? ResponseEntity.ok().build() : ResponseEntity.status(HttpStatus.TOO_EARLY).build();
    }
}
