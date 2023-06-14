package no.fintlabs.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.security.oauth2.client.endpoint.OAuth2PasswordGrantRequest;
import org.springframework.security.oauth2.client.endpoint.ReactiveOAuth2AccessTokenResponseClient;
import org.springframework.security.oauth2.client.endpoint.WebClientReactivePasswordTokenResponseClient;
import org.springframework.security.oauth2.client.registration.ClientRegistration;
import org.springframework.security.oauth2.core.AuthorizationGrantType;
import org.springframework.web.reactive.function.client.ClientRequest;
import org.springframework.web.reactive.function.client.ExchangeFilterFunction;
import org.springframework.web.reactive.function.client.WebClient;

public class WebClientConfig {

    @Value("${spring.security.oauth2.resourceserver.jwt.issuer-uri}")
    private String tokenUri;

    @Value("${fint.core.oauth2.client-id}")
    private String clientId;

    @Value("${fint.core.oauth2.client-secret}")
    private String clientSecret;

    @Value("${fint.core.oauth2.scope}")
    private String scope;

    @Value("${fint.core.oauth2.username}")
    private String username;

    @Value("${fint.core.oauth2.password}")
    private String password;

    @Bean
    public WebClient webClient() {
        return WebClient.builder()
                .filter(oauth2PasswordGrant())
                .build();
    }

    private ExchangeFilterFunction oauth2PasswordGrant() {
        return ExchangeFilterFunction.ofRequestProcessor(
                clientRequest -> {
                    ClientRegistration clientRegistration = ClientRegistration
                            .withRegistrationId("fint")
                            .tokenUri(tokenUri)
                            .clientId(clientId)
                            .clientSecret(clientSecret)
                            .authorizationGrantType(AuthorizationGrantType.PASSWORD)
                            .scope(scope)
                            .build();

                    ReactiveOAuth2AccessTokenResponseClient<OAuth2PasswordGrantRequest> accessTokenResponseClient = new WebClientReactivePasswordTokenResponseClient();
                    OAuth2PasswordGrantRequest passwordGrantRequest = new OAuth2PasswordGrantRequest(clientRegistration, username, password);

                    return accessTokenResponseClient.getTokenResponse(passwordGrantRequest)
                            .map(accessTokenResponse -> {
                                String accessTokenValue = accessTokenResponse.getAccessToken().getTokenValue();
                                return ClientRequest.from(clientRequest)
                                        .headers(headers -> headers.setBearerAuth(accessTokenValue))
                                        .build();
                            });
                });
    }

}
