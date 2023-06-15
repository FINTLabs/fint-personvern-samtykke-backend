package no.fintlabs.behandling;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RequestMapping("/behandling")
@RestController
public class BehandlingController {

    private final BehandlingService behandlingService;

    public BehandlingController(BehandlingService behandlingService) {
        this.behandlingService = behandlingService;
    }

    @GetMapping
    public ResponseEntity<List<Behandling>> getBehandlinger() {
        return ResponseEntity.ok(behandlingService.getBehandlinger());
    }

}
