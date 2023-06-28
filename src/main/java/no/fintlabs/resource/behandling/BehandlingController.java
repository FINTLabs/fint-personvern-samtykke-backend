package no.fintlabs.resource.behandling;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
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
    public ResponseEntity<Behandling> updateBehandling(@RequestParam int systemId,
                                                       @RequestParam boolean aktiv) {
        if (behandlingTestData.exists(systemId)) {
            return ResponseEntity.ok(behandlingTestData.updateBehandling(systemId, aktiv));
        }
        return ResponseEntity.notFound().build();
    }

}
