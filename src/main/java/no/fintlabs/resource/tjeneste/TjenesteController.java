package no.fintlabs.resource.tjeneste;

import com.nimbusds.oauth2.sdk.ErrorResponse;
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
        boolean status = tjenesteService.status(corrId);
        log.info("Corr-id status: {} - {}", corrId, status);
        return status ? ResponseEntity.status(HttpStatus.CREATED).build() : ResponseEntity.status(HttpStatus.ACCEPTED).build();
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity handleIllegalArgumentException(Exception e){
        return ResponseEntity.badRequest().body(e.getMessage());
    }
}
