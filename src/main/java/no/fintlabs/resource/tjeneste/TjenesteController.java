package no.fintlabs.resource.tjeneste;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.util.List;

@RequestMapping("/tjeneste")
@RestController
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
    public ResponseEntity<Tjeneste> createTjeneste(ServerHttpRequest request, @PathVariable String orgName, @RequestBody Tjeneste tjeneste){
        String corrId = tjenesteService.create(orgName, tjeneste);
        URI location = UriComponentsBuilder.fromUri(request.getURI())
                .replacePath(orgName)
                .path("/status/{corrId}")
                .buildAndExpand(corrId)
                .toUri();
        return ResponseEntity.created(location).build();
    }

    @GetMapping("status/{corrId}")
    public ResponseEntity<Void> status (@PathVariable String corrId){
        return tjenesteService.status(corrId) ? ResponseEntity.ok().build() : ResponseEntity.status(HttpStatus.PROCESSING).build();
    }
}
