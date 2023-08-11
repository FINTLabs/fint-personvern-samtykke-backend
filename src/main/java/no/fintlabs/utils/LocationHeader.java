package no.fintlabs.utils;

import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;

public class LocationHeader {
    public static URI get(String corrId, ServerHttpRequest request) {

        String url = request.getURI().toString();

        return UriComponentsBuilder.fromUriString(url.substring(0, url.lastIndexOf('/')))
                .path("/status/{corrId}")
                .buildAndExpand(corrId)
                .toUri();
    }
}
