package com.visma.kalmar.api.config;

import com.visma.kalmar.api.VismaConnectUserGatewayAdapter;
import com.visma.kalmar.api.adapters.language.LanguageGatewayAdapter;
import com.visma.kalmar.api.adapters.user.UserGatewayAdapter;
import com.visma.kalmar.api.language.LanguageGateway;
import com.visma.kalmar.api.user.*;
import com.visma.kalmar.api.vismaconnect.VismaConnectUserGateway;
import com.visma.feature.kalmar.api.language.LanguageRepository;
import com.visma.feature.kalmar.api.user.UserRepository;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class UserConfig {

    private LanguageRepository languageRepository;

    @Bean
    public UserGateway userGateway(UserRepository userRepository) {
        return new UserGatewayAdapter(userRepository);
    }

    @Bean
    public LanguageGateway languageGateway(
            LanguageRepository languageRepository) {
        this.languageRepository = languageRepository;
        return new LanguageGatewayAdapter(languageRepository);
    }

    @Bean
    public VismaConnectUserGateway vismaConnectUserGateway(
            org.springframework.web.reactive.function.client.WebClient webClient,
            LanguageGateway languageGateway) {
        return new VismaConnectUserGatewayAdapter(webClient, languageGateway);
    }

    @Bean
    public CreateUserInputPort createUserInputPort(
            UserGateway userGateway,
            LanguageGateway languageGateway,
            VismaConnectUserGateway vismaConnectUserGateway) {
        return new CreateUserUseCase(userGateway, languageGateway, vismaConnectUserGateway);
    }

    @Bean
    public UpdateUserInputPort updateUserInputPort(
            UserGateway userGateway,
            LanguageGateway languageGateway,
            VismaConnectUserGateway vismaConnectUserGateway) {
        return new UpdateUserUseCase(userGateway, languageGateway, vismaConnectUserGateway);
    }

    @Bean
    public DeleteUserInputPort deleteUserInputPort(
            UserGateway userGateway, VismaConnectUserGateway vismaConnectUserGateway) {
        return new DeleteUserUseCase(userGateway, vismaConnectUserGateway);
    }

    @Bean
    public GetUserInputPort getUserInputPort(
            UserGateway userGateway, LanguageGateway languageGateway) {
        return new GetUserUseCase(userGateway, languageGateway);
    }
}
