package com.groupware.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class MenuPermissionMap {
    @JsonProperty("name")
    private String name;
    @JsonProperty("permissionName")
    private String permissionName;
}
