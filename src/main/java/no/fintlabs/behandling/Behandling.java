package no.fintlabs.behandling;

import lombok.Data;

import java.util.List;

@Data
public class Behandling {

    private String id;
    private Boolean aktiv;
    private String formal;
    private List<String> behandlingsgrunnlagIds;
    private List<String> personopplysningIds;
    private List<String> tjenesteIds;

}
