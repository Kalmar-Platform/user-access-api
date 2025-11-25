package com.visma.kalmar.api.role;

import com.visma.kalmar.api.role.dto.RoleResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

public class RolePresenter implements RoleOutputPort {

    private ResponseEntity<RoleResponse> responseEntity;

    @Override
    public void present(RoleOutputData outputData) {
        var response = new RoleResponse();
        response.setRoleId(outputData.roleId());
        response.setName(outputData.name());
        response.setInvariantKey(outputData.invariantKey());
        response.setDescription(outputData.description());

        HttpStatus status = outputData.created() ? HttpStatus.CREATED : HttpStatus.OK;

        responseEntity = ResponseEntity.status(status).body(response);
    }

    public ResponseEntity<RoleResponse> getResponse() {
        return responseEntity;
    }
}
