package org.example.backend.requestModels;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class PasswordsRequest {

    @JsonProperty("oldPassword")
    private String oldPassword;

    @JsonProperty("newPassword")
    private String newPassword;
}

