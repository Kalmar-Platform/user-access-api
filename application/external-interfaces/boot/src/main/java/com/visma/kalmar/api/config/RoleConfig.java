package com.visma.kalmar.api.config;

import com.visma.kalmar.api.adapters.role.RoleGatewayAdapter;
import com.visma.kalmar.api.role.*;
import com.visma.feature.kalmar.api.role.RoleRepository;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RoleConfig {

    @Bean
    public RoleGateway roleGateway(RoleRepository roleRepository) {
        return new RoleGatewayAdapter(roleRepository);
    }

    @Bean
    public CreateRoleInputPort createRoleInputPort(RoleGateway roleGateway) {
        return new CreateRoleUseCase(roleGateway);
    }

    @Bean
    public GetRoleInputPort getRoleInputPort(RoleGateway roleGateway) {
        return new GetRoleUseCase(roleGateway);
    }

    @Bean
    public UpdateRoleInputPort updateRoleInputPort(RoleGateway roleGateway) {
        return new UpdateRoleUseCase(roleGateway);
    }

    @Bean
    public DeleteRoleInputPort deleteRoleInputPort(RoleGateway roleGateway) {
        return new DeleteRoleUseCase(roleGateway);
    }
}
