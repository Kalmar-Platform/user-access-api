package com.visma.kalmar.api.adapters.context;

import com.visma.kalmar.api.context.ContextGateway;
import com.visma.kalmar.api.entities.context.Context;
import com.visma.kalmar.api.exception.ResourceNotFoundException;
import com.visma.feature.kalmar.api.context.ContextRepository;

import java.util.UUID;

public class ContextGatewayAdapter implements ContextGateway {

    private final ContextRepository contextRepository;

    public ContextGatewayAdapter(ContextRepository contextRepository) {
        this.contextRepository = contextRepository;
    }

    @Override
    public Context findById(UUID idContext) {
        var jpaEntity = contextRepository.findById(idContext)
                .orElseThrow(() -> new ResourceNotFoundException("Context", "Context not found with id: " + idContext));
        
        return toDomainEntity(jpaEntity);
    }

    @Override
    public boolean existsById(UUID idContext) {
        return contextRepository.existsById(idContext);
    }

    private Context toDomainEntity(com.visma.feature.kalmar.api.context.Context jpaEntity) {
        return new Context(
                jpaEntity.getIdContext(),
                jpaEntity.getIdContextType(),
                jpaEntity.getIdContextParent(),
                jpaEntity.getIdCountry(),
                jpaEntity.getName(),
                jpaEntity.getOrganizationNumber()
        );
    }
}
