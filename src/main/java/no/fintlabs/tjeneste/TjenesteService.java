package no.fintlabs.tjeneste;

import lombok.extern.slf4j.Slf4j;
import no.fint.model.resource.personvern.samtykke.TjenesteResource;
import no.fint.model.resource.personvern.samtykke.TjenesteResources;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
public class TjenesteService {

    private final WebClient webClient;

    public TjenesteService(WebClient webClient) {
        this.webClient = webClient;
    }

    @PostConstruct
    private List<Tjeneste> getTjenester() {
        List<Tjeneste> tjenester = new ArrayList<>();
        getTjenesteResources().subscribe(tjenesteResources ->
                tjenesteResources
                        .getContent()
                        .forEach(tjenesteResource -> {
                            Tjeneste tjeneste = new Tjeneste();
                            tjeneste.setId(tjenesteResource.getSystemId().getIdentifikatorverdi());
                            tjeneste.setName(tjenesteResource.getNavn());
                            tjeneste.setListOfRelationIds(getRelationIdsFromLinks(tjenesteResource));
                            tjenester.add(tjeneste);
                        }));
        return tjenester;
    }

    private List<String> getRelationIdsFromLinks(TjenesteResource tjenesteResource) {
        List<String> listOfIds = new ArrayList<>();
        tjenesteResource
                .getLinks()
                .get("behandlingsgrunnlag")
                .forEach(link -> {
                    String href = link.getHref();
                    String[] pathSegments = href.split("/");
                    String id = pathSegments[pathSegments.length - 1];
                    listOfIds.add(id);
                });
        return listOfIds;
    }

    private Mono<TjenesteResources> getTjenesteResources() {
        return webClient.get()
                .uri("/tjeneste")
                .retrieve()
                .bodyToMono(TjenesteResources.class);
    }
}
