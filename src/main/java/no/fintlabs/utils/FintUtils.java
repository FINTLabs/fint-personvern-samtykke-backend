package no.fintlabs.utils;

import no.fint.model.resource.FintLinks;
import org.springframework.stereotype.Service;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Service
public class FintUtils {

    public <T extends FintLinks & Serializable> List<String> getRelationIdsFromLinks(T resource, String linkName) {
        List<String> listOfIds = new ArrayList<>();
        resource
                .getLinks()
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
