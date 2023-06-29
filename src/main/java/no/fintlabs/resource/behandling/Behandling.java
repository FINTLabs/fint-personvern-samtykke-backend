package no.fintlabs.resource.behandling;

import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class Behandling {

    private String id;
    private Boolean aktiv;
    private String formal;
    private String behandlingsgrunnlagId;
    private String personopplysningId;
    private String tjenesteId;

}
