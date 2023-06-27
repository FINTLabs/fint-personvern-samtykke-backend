package no.fintlabs.resource.tjeneste;

import lombok.Data;

import java.util.List;

@Data
public class Tjeneste {

    private int id;
    private String navn;
    private List<Integer> behandlingIds;

}
