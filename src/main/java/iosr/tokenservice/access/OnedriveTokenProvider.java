package iosr.tokenservice.access;

import iosr.tokenservice.config.Oauth2Configuration;
import org.json.JSONObject;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

public class OnedriveTokenProvider extends AbstractTokenProvider {

    private static final String GRANT_TYPE = "authorization_code";
    private static final String REDIRECT_URI = "https://login.live.com/oauth20_desktop.srf";

    private final Client client;
    private final Oauth2Configuration oauth2Configuration;

    public OnedriveTokenProvider(Client client, Oauth2Configuration oauth2Configuration) {
        this.client = client;
        this.oauth2Configuration = oauth2Configuration;
    }

    @Override
    protected JSONObject requestForAccessToken(String code) {
        WebTarget webTarget = queryObtainTokenResource(code);
        Response response = webTarget.request(MediaType.APPLICATION_JSON).get();
        String rawResponse = response.readEntity(String.class);

        return new JSONObject(rawResponse);
    }

    private WebTarget queryObtainTokenResource(String code) {
        return client.target("https://login.live.com/oauth20_token.srf")
                .queryParam("client_id", oauth2Configuration.getAppKey())
                .queryParam("client_secret", oauth2Configuration.getAppKeySecret())
                .queryParam("grant_type", GRANT_TYPE)
                .queryParam("redirect_uri", REDIRECT_URI)
                .queryParam("code", code);
    }
}
