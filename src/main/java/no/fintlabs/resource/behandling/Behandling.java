package no.fintlabs.resource.behandling;

import lombok.Data;

import java.util.List;

@Data
public class Behandling {

    private int id;
    private Boolean aktiv;
    private String formal;
    private List<Integer> behandlingsgrunnlagIds;
    private List<Integer> personopplysningIds;
    private List<Integer> tjenesteIds;

}
