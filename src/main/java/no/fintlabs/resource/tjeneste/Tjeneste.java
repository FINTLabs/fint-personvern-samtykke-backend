package no.fintlabs.resource.tjeneste;

import lombok.Data;

import java.util.List;

@Data
public class Tjeneste {

    private String id;
    private String navn;
    private List<Integer> behandlingIds;

}
