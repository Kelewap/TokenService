package iosr.tokenservice.config;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.dropwizard.Configuration;

public class TokenServiceConfiguration extends Configuration {
    private String dropboxCode;
    private Oauth2Configuration oauth2Configuration;

    @JsonProperty
    public String getDropboxCode() {
        return dropboxCode;
    }

    @JsonProperty
    public void setDropboxCode(String dropboxCode) {
        this.dropboxCode = dropboxCode;
    }

    @JsonProperty
    public Oauth2Configuration getOauth2Configuration() {
        return oauth2Configuration;
    }

    @JsonProperty
    public void setOauth2Configuration(Oauth2Configuration oauth2Configuration) {
        this.oauth2Configuration = oauth2Configuration;
    }
}
