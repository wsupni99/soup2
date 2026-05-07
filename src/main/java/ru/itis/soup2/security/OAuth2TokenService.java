package ru.itis.soup2.security;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClient;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClientService;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class OAuth2TokenService {

    private final OAuth2AuthorizedClientService authorizedClientService;

    public String getGoogleAccessToken(String email) {
        OAuth2AuthorizedClient client = authorizedClientService.loadAuthorizedClient("google", email);

        if (client != null && client.getAccessToken() != null) {
            return client.getAccessToken().getTokenValue();
        }

        log.debug("No Google Access Token found for user: {}", email);
        return null;
    }
}