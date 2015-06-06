package iosr.tokenservice.access;

import iosr.tokenservice.config.Oauth2Configuration;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

public class DropboxTokenProvider extends AbstractPlainRESTTokenProvider {

    private static final String GRANT_TYPE = "authorization_code";

    private final Client client;
    private final Oauth2Configuration oauth2Configuration;

    public DropboxTokenProvider(Client client, Oauth2Configuration oauth2Configuration) {
        this.client = client;
        this.oauth2Configuration = oauth2Configuration;
    }

    @Override
    protected String obtainAccessToken(String code) throws AuthorizationException {
        WebTarget webTarget = queryObtainTokenResource(code);
        Response response = webTarget.request(MediaType.APPLICATION_JSON).post(null);
        String rawResponse = response.readEntity(String.class);

        return getTokenFromResponse(rawResponse);
    }

    @Override
    protected String getRefreshedToken() {
        return this.getToken().getToken();
    }

    private WebTarget queryObtainTokenResource(String code) {
        return client.target("https://api.dropbox.com/1/oauth2/token")
                .queryParam("client_id", oauth2Configuration.getAppKey())
                .queryParam("client_secret", oauth2Configuration.getAppKeySecret())
                .queryParam("grant_type", GRANT_TYPE)
                .queryParam("code", code);
    }
}
