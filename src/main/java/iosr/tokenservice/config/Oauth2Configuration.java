package iosr.tokenservice.config;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class Oauth2Configuration {

    private final String appName;
    private final String appKey;
    private final String appKeySecret;

    @JsonCreator
    public Oauth2Configuration(@JsonProperty("appName") String appName, @JsonProperty("appKey") String appKey, @JsonProperty("appKeySecret") String appKeySecret) {
        this.appName = appName;
        this.appKey = appKey;
        this.appKeySecret = appKeySecret;
    }

    @JsonProperty
    public String getAppName() {
        return appName;
    }

    @JsonProperty
    public String getAppKey() {
        return appKey;
    }

    @JsonProperty
    public String getAppKeySecret() {
        return appKeySecret;
    }
}
