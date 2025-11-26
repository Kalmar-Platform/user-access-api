package com.visma.kalmar.api.role.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class RoleResponse {

    @Schema(description = "The unique identifier of the role")
    @JsonProperty("roleId")
    private String roleId;

    @Schema(description = "The name of the role")
    @JsonProperty("name")
    private String name;

    @Schema(description = "The invariant key for the role")
    @JsonProperty("invariantKey")
    private String invariantKey;

    @Schema(description = "The description of the role")
    @JsonProperty("description")
    private String description;
}
