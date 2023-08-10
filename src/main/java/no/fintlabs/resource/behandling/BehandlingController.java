package no.fintlabs.resource.behandling;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RequiredArgsConstructor
@RequestMapping("/behandling")
@RestController
public class BehandlingController {

    private final BehandlingService behandlingService;

    @GetMapping("/{orgName}")
    public ResponseEntity<List<Behandling>> getBehandlinger(@PathVariable String orgName) {
        return ResponseEntity.ok(behandlingService.getBehandlinger(orgName));
    }

}
