package no.fintlabs.resource.personopplysning;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class Personopplysning {

    private String id;
    private String kode;
    private String navn;

}
