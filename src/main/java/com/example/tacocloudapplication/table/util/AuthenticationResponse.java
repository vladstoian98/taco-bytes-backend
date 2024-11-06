package com.example.tacocloudapplication.table.util;

import lombok.Data;

@Data
public class AuthenticationResponse {
    private final String jwt;

    public AuthenticationResponse(String jwt) {
        this.jwt = jwt;
    }


    public String getJwt() {
        return jwt;
    }

}