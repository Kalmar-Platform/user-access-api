package com.visma.kalmar.api.entities.role;

import java.util.Date;
import java.util.UUID;

public record Role(
        UUID idRole,
        String name,
        String invariantKey,
        String description,
        Long recordVersion,
        Date whenEdited
) {

}
