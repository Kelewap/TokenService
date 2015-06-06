package iosr.tokenservice.config;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.dropwizard.Configuration;

public class TokenServiceConfiguration extends Configuration {
    private String dropboxCode;
    private Oauth2Configuration dropboxConfig;
    private Oauth2Configuration onedriveConfig;

    @JsonProperty
    public String getDropboxCode() {
        return dropboxCode;
    }

    @JsonProperty
    public void setDropboxCode(String dropboxCode) {
        this.dropboxCode = dropboxCode;
    }

    @JsonProperty
    public Oauth2Configuration getDropboxConfig() {
        return dropboxConfig;
    }

    @JsonProperty
    public void setDropboxConfig(Oauth2Configuration dropboxConfig) {
        this.dropboxConfig = dropboxConfig;
    }

    @JsonProperty
    public Oauth2Configuration getOnedriveConfig() {
        return onedriveConfig;
    }

    @JsonProperty
    public void setOnedriveConfig(Oauth2Configuration onedriveConfig) {
        this.onedriveConfig = onedriveConfig;
    }
}
