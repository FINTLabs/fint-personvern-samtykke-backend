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

    private static final String COMPONENT = "personvern_samtykke";

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
        String role = String.format("ROLE_FINT_Client_%s", COMPONENT);
        return authentication.map(auth -> {
            boolean hasRole = auth.getAuthorities().stream()
                    .anyMatch(a -> a.getAuthority().equals(role));
            return new AuthorizationDecision(hasRole);
        });
    }

}
