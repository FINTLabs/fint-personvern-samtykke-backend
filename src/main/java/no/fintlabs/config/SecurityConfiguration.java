package no.fintlabs.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authorization.AuthorizationDecision;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.server.SecurityWebFilterChain;
import org.springframework.security.web.server.authorization.AuthorizationContext;
import reactor.core.publisher.Mono;

@Configuration
public class SecurityConfiguration {

    private static final String PERSONVERN_SAMTYKKE = "personvern_samtykke";
    private static final String PERSONVERN_KODEVERK = "personvern_kodeverk";

    @Bean
    SecurityWebFilterChain springSecurityFilterChain(ServerHttpSecurity http) {
        http
                .authorizeExchange((authorize) -> authorize
                        .pathMatchers("/**")
                        .access(this::hasRequiredOrgIdAndRole)
                        .anyExchange()
                        .authenticated()
                )
                .oauth2ResourceServer(ServerHttpSecurity.OAuth2ResourceServerSpec::jwt);
        return http.build();
    }

    private Mono<AuthorizationDecision> hasRequiredOrgIdAndRole(Mono<Authentication> authentication, AuthorizationContext context) {
        String firstRole = String.format("ROLE_FINT_Client_%s", PERSONVERN_SAMTYKKE);
        String secondRole = String.format("ROLE_FINT_Client_%s", PERSONVERN_KODEVERK);
        return authentication.map(auth -> {
            boolean hasFirstRole = auth.getAuthorities().stream()
                    .anyMatch(a -> a.getAuthority().equals(firstRole));
            boolean hasSecondRole = auth.getAuthorities().stream()
                    .anyMatch(a -> a.getAuthority().equals(secondRole));
            return new AuthorizationDecision(hasFirstRole && hasSecondRole);
        });
    }

}
