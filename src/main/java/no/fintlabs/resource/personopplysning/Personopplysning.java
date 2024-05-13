package no.fintlabs.resource.personopplysning;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class Personopplysning {

    @JsonIgnore
    private String id;
    private String kode;
    private String navn;

}
