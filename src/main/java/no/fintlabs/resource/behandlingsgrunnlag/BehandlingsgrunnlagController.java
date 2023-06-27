package no.fintlabs.resource.behandlingsgrunnlag;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RequiredArgsConstructor
@RequestMapping("/behandlingsgrunnlag")
@RestController
public class BehandlingsgrunnlagController {

    private final BehandlingsgrunnlagService behandlingService;
    private final BehandlingsgrunnlagTestData behandlingsgrunnlagTestData;

    @GetMapping
    public ResponseEntity<List<Behandlingsgrunnlag>> getBehandlinger() {
        return ResponseEntity.ok(behandlingsgrunnlagTestData.getList());
    }

}
