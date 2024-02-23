package no.fintlabs.resource.tjeneste;

import lombok.extern.slf4j.Slf4j;
import no.fintlabs.utils.LocationHeader;
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
        log.info("Creating tjeneste with corr-id: {}", corrId);
        return ResponseEntity.created(LocationHeader.get(corrId, request)).build();
    }

    @GetMapping("/status/{corrId}")
    public ResponseEntity<?> status(ServerHttpRequest request, @PathVariable String corrId) {
        boolean status = tjenesteService.status(corrId);
        log.info("Corr-id status: {} - {}", corrId, status);
        return status ? ResponseEntity.created(LocationHeader.get(corrId, request)).build() : ResponseEntity.accepted().build();
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<String> handleIllegalArgumentException(Exception e) {
        return ResponseEntity.badRequest().body(e.getMessage());
    }
}
