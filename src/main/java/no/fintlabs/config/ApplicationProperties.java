package no.fintlabs.config;

import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@Data
@Configuration
public class ApplicationProperties {
    @Value("fint.samtykke-admin.base-url")
    private String baseUrl;
}
