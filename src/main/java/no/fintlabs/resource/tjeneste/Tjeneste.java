package no.fintlabs.tjeneste;

import lombok.Data;

import java.util.List;

@Data
public class Tjeneste {

    private String id;
    private String name;
    private List<String> behandlingIds;

}
