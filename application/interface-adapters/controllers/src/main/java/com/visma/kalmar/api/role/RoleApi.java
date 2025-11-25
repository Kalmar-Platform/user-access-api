package com.visma.kalmar.api.role;

import com.visma.kalmar.api.role.dto.RoleRequest;
import com.visma.kalmar.api.role.dto.RoleResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@Validated
@Tag(name = "Role API", description = "Role management operations")
@SecurityRequirement(name = "jwt")
public interface RoleApi {

    @GetMapping("/roles/{roleId}")
    @Operation(
            summary = "Get role by ID",
            description = "Retrieve a role by its unique identifier.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Role retrieved successfully"),
            @ApiResponse(responseCode = "404", description = "Role not found"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    ResponseEntity<RoleResponse> getRoleById(@PathVariable("roleId") String roleId);

    @PostMapping("/roles")
    @Operation(
            summary = "Create a new role",
            description = "Create a new role with the provided details.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Role created successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid request data"),
            @ApiResponse(responseCode = "409", description = "Role with the same name or invariantKey already exists"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    ResponseEntity<RoleResponse> createRole(@Valid @RequestBody RoleRequest roleRequest);

    @PutMapping("/roles/{roleId}")
    @Operation(
            summary = "Update an existing role",
            description = "Update an existing role with the provided details.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Role updated successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid request data"),
            @ApiResponse(responseCode = "404", description = "Role not found"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    ResponseEntity<RoleResponse> updateRole(
            @PathVariable("roleId") String roleId,
            @Valid @RequestBody RoleRequest roleRequest);

    @DeleteMapping("/roles/{roleId}")
    @Operation(
            summary = "Delete an existing role",
            description = "Delete an existing role by ID.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Role deleted successfully"),
            @ApiResponse(responseCode = "404", description = "Role not found"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @ResponseStatus(HttpStatus.NO_CONTENT)
    ResponseEntity<Void> deleteRole(@PathVariable("roleId") String roleId);
}
