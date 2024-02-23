package no.fintlabs.resource.behandling;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import no.fintlabs.utils.LocationHeader;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
@RequestMapping("/behandling")
@RestController
@Slf4j
public class BehandlingController {

    private final BehandlingService behandlingService;

    @GetMapping("/{orgName}")
    public ResponseEntity<List<Behandling>> getBehandlinger(@PathVariable String orgName) {
        return ResponseEntity.ok(behandlingService.getBehandlinger(orgName));
    }

    @PostMapping("/{orgName}")
    public ResponseEntity<Void> createBehandling(ServerHttpRequest request, @PathVariable String orgName, @RequestBody Behandling behandling) {
        String corrId = behandlingService.create(orgName, behandling);
        log.info("Creating behandling with corr-id: {}", corrId);
        return ResponseEntity.created(LocationHeader.get(corrId, request)).build();
    }

    @PutMapping("/{orgName}/{id}/{aktiv}")
    public ResponseEntity<Void> updateState(ServerHttpRequest request, @PathVariable String orgName, @PathVariable String id, @PathVariable boolean aktiv){
        String corrId = behandlingService.updateState(orgName, id, aktiv);
        return ResponseEntity.created(LocationHeader.get(corrId, request)).build();
    }

    @GetMapping("/status/{corrId}")
    public ResponseEntity<Void> status(ServerHttpRequest request, @PathVariable String corrId) {
        boolean status = behandlingService.status(corrId);
        log.info("Corr-id status: {} - {}", corrId, status);
        return status ? ResponseEntity.created(LocationHeader.get(corrId, request)).build() : ResponseEntity.accepted().build();
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<String> handleIllegalArgumentException(Exception e){
        return ResponseEntity.badRequest().body(e.getMessage());
    }
}
