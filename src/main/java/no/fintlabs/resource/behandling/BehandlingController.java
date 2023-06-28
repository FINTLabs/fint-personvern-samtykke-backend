package no.fintlabs.resource.behandling;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
@RequestMapping("/behandling")
@RestController
public class BehandlingController {

    private final BehandlingService behandlingService;
    private final BehandlingTestData behandlingTestData;

    @GetMapping
    public ResponseEntity<List<Behandling>> getBehandlinger() {
        return ResponseEntity.ok(behandlingTestData.getBehandlingMap());
    }

    @PutMapping("/{systemId}/{aktiv}")
    public ResponseEntity<Behandling> updateBehandling(@RequestParam String systemId,
                                                       @RequestParam boolean aktiv) {
        if (behandlingTestData.exists(systemId)) {
            return ResponseEntity.ok(behandlingTestData.updateBehandling(systemId, aktiv));
        }
        return ResponseEntity.notFound().build();
    }

    @PostMapping
    public ResponseEntity<Behandling> createBehandling(@RequestBody BehandlingRequestPayload requestPayload) {
        return ResponseEntity.ok(behandlingTestData.createBehandling(requestPayload));
    }

}
