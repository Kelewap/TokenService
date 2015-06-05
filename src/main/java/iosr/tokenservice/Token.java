package iosr.tokenservice;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class Token {
    private final String token;

    @JsonCreator
    public Token(@JsonProperty("token") String token) {
        this.token = token;
    }

    @JsonProperty
    public String getToken() {
        return token;
    }
}
