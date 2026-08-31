package com.himal.dto;

import jakarta.validation.constraints.NotNull;

/*
    @author: mihdjo
*/

public class UpdateMojPlanStatusRequest {

    @NotNull
    private Boolean status;

    public UpdateMojPlanStatusRequest() {
    }

    public Boolean getStatus() {
        return status;
    }

    public void setStatus(Boolean status) {
        this.status = status;
    }
}