package no.fintlabs.resource.behandling;

import lombok.Data;

@Data
public class BehandlingRequestPayload {

    private Boolean aktiv;
    private String formal;

    private String behandlingsgrunnlagId;
    private String tjenesteId;
    private String personopplysningId;
}
