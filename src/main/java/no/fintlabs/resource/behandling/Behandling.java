package no.fintlabs.resource.behandling;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class Behandling {

    private String id;
    private Boolean aktiv;
    private String formal;
    private String orgId;
    private List<String> behandlingsgrunnlagIds = new ArrayList<>();
    private List<String> personopplysningIds = new ArrayList<>();
    private List<String> tjenesteIds = new ArrayList<>();

}
