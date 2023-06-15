package no.fintlabs.config;

import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.oauth2.client.*;
import org.springframework.security.oauth2.client.registration.ReactiveClientRegistrationRepository;
import org.springframework.security.oauth2.client.web.reactive.function.client.ServerOAuth2AuthorizedClientExchangeFilterFunction;
import org.springframework.web.reactive.function.client.ExchangeStrategies;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.util.Map;

@Slf4j
@Getter
@Setter
@Configuration
public class WebClientConfig {

    @Value("${fint.org-id}")
    private String orgId;

    @Value("${fint.registration-id}")
    private String registrationId;

    @Value("${fint.base-url}")
    private String baseUrl;

    @Value("${fint.core.oauth2.username}")
    private String username;

    @Value("${fint.core.oauth2.password}")
    private String password;

    @Bean
    public ReactiveOAuth2AuthorizedClientManager authorizedClientManager(
            ReactiveClientRegistrationRepository clientRegistrationRepository,
            ReactiveOAuth2AuthorizedClientService authorizedClientService) {

        ReactiveOAuth2AuthorizedClientProvider authorizedClientProvider = ReactiveOAuth2AuthorizedClientProviderBuilder.builder()
                .password()
                .refreshToken()
                .build();

        AuthorizedClientServiceReactiveOAuth2AuthorizedClientManager authorizedClientManager = new AuthorizedClientServiceReactiveOAuth2AuthorizedClientManager(
                clientRegistrationRepository,
                authorizedClientService
        );

        authorizedClientManager.setAuthorizedClientProvider(authorizedClientProvider);
        authorizedClientManager.setContextAttributesMapper(oAuth2AuthorizeRequest -> Mono.just(Map.of(
                OAuth2AuthorizationContext.USERNAME_ATTRIBUTE_NAME, username,
                OAuth2AuthorizationContext.PASSWORD_ATTRIBUTE_NAME, password
        )));

        return authorizedClientManager;
    }

    @Bean
    public WebClient webClient(WebClient.Builder builder, ReactiveOAuth2AuthorizedClientManager authorizedClientManager) {
        ExchangeStrategies exchangeStrategies = ExchangeStrategies.builder()
                .codecs(configurer -> configurer.defaultCodecs().maxInMemorySize(-1))
                .build();

        ServerOAuth2AuthorizedClientExchangeFilterFunction oauth2Client = new ServerOAuth2AuthorizedClientExchangeFilterFunction(authorizedClientManager);
        oauth2Client.setDefaultClientRegistrationId(registrationId);

        return builder
                .exchangeStrategies(exchangeStrategies)
                .filter(oauth2Client)
                .baseUrl(baseUrl)
                .defaultHeader("x-org-id", orgId)
                .build();
    }

}
