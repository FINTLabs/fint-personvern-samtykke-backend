package no.fintlabs.resource.tjeneste;

import lombok.Data;

import java.util.List;

@Data
public class Tjeneste {

    private String id;
    private String orgId;
    private String navn;
    private List<String> behandlingIds;

}
