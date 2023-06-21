package no.fintlabs.tjeneste;

import lombok.extern.slf4j.Slf4j;
import no.fint.model.resource.personvern.samtykke.TjenesteResources;
import no.fintlabs.utils.FintUtils;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
public class TjenesteService {

    private final FintUtils fintUtils;

    private final WebClient webClient;

    public TjenesteService(FintUtils fintUtils, WebClient webClient) {
        this.fintUtils = fintUtils;
        this.webClient = webClient;
    }

    public List<Tjeneste> getTjenester() {
        List<Tjeneste> tjenester = new ArrayList<>();
        getTjenesteResources().subscribe(tjenesteResources ->
                tjenesteResources
                        .getContent()
                        .forEach(tjenesteResource -> {
                            Tjeneste tjeneste = new Tjeneste();
                            tjeneste.setId(tjenesteResource.getSystemId().getIdentifikatorverdi());
                            tjeneste.setName(tjenesteResource.getNavn());
                            tjeneste.setBehandlingIds(fintUtils.getRelationIdsFromLinks(tjenesteResource, "behandling"));
                            tjenester.add(tjeneste);
                        }));
        return tjenester;
    }

    private Mono<TjenesteResources> getTjenesteResources() {
        return webClient.get()
                .uri("/tjeneste")
                .retrieve()
                .bodyToMono(TjenesteResources.class);
    }
}
