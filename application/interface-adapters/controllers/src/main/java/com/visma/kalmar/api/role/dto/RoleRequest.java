package com.visma.kalmar.api.role.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class RoleRequest {

    @Schema(description = "The name of the role", example = "Administrator")
    @NotNull
    @Size(max = 255)
    @JsonProperty("name")
    private String name;

    @Schema(description = "The invariant key for the role", example = "ADMIN")
    @NotNull
    @Size(max = 50)
    @JsonProperty("invariantKey")
    private String invariantKey;

    @Schema(description = "The description of the role", example = "Full system access")
    @JsonProperty("description")
    private String description;
}
