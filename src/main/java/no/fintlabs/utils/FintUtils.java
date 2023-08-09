package no.fintlabs.utils;

import no.fint.model.resource.FintLinks;
import no.fint.model.resource.Link;
import org.springframework.stereotype.Service;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
public class FintUtils {

    public <T extends FintLinks & Serializable> List<String> getRelationIdsFromLinks(T resource, String linkName) {
        List<String> listOfIds = new ArrayList<>();
        Map<String, List<Link>> links = resource.getLinks();
        if (!links.containsKey(linkName)) {
            return new ArrayList<>();
        }

        links
                .get(linkName)
                .forEach(link -> {
                    String href = link.getHref();
                    String[] pathSegments = href.split("/");
                    String id = pathSegments[pathSegments.length - 1];
                    listOfIds.add(id);
                });
        return listOfIds;
    }

}
