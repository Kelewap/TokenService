package iosr.tokenservice.access;

import iosr.tokenservice.Token;
import iosr.tokenservice.config.Oauth2Configuration;
import org.json.JSONObject;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

public class DropboxTokenProvider implements TokenProvider {

    private static final String GRANT_TYPE = "authorization_code";

    private final Client client;
    private final Oauth2Configuration oauth2Configuration;
    private Token token;

    public DropboxTokenProvider(Client client, Oauth2Configuration oauth2Configuration) {
        this.client = client;
        this.oauth2Configuration = oauth2Configuration;
    }

    public void redeemCode(String authorizationCode) throws AuthorizationException {
        JSONObject responseJson = requestForAccessToken(authorizationCode);

        if (!responseJson.has("access_token")) {
            throw new AuthorizationException(responseJson.toString());
        }

        String accessToken = responseJson.get("access_token").toString();
        this.token = new Token(accessToken);
    }

    private JSONObject requestForAccessToken(String code) {
        WebTarget webTarget = queryObtainTokenResource(code);
        Response response = webTarget.request(MediaType.APPLICATION_JSON).post(null);
        int status = response.getStatus();
        String rawResponse = response.readEntity(String.class);

        return new JSONObject(rawResponse);
    }

    private WebTarget queryObtainTokenResource(String code) {
        return client.target("https://api.dropbox.com/1/oauth2/token")
                .queryParam("client_id", oauth2Configuration.getAppKey())
                .queryParam("client_secret", oauth2Configuration.getAppKeySecret())
                .queryParam("grant_type", GRANT_TYPE)
                .queryParam("code", code);
    }

    @Override
    public Token getToken() {
        return token;
    }
}
