package no.fintlabs.resource.behandling;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.util.UriComponents;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
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

    @PostMapping("/{orgName}")
    public ResponseEntity<Behandling> createBehandling(ServerHttpRequest request, @PathVariable String orgName, @RequestBody Behandling behandling) {
        String corrId = behandlingService.create(orgName, behandling);
        URI location = UriComponentsBuilder.fromUri(request.getURI())
                .replacePath(orgName)
                .path("/status/{corrId}")
                .buildAndExpand(corrId)
                .toUri();
        return ResponseEntity.created(location).build();
    }

    @GetMapping("/status/corrId")
    public ResponseEntity<Void> status(@PathVariable String corrId){
        return behandlingService.status(corrId) ? ResponseEntity.ok().build() : ResponseEntity.status(HttpStatus.PROCESSING).build();
    }

}
