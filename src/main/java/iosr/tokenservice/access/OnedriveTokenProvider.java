package iosr.tokenservice.access;

import iosr.tokenservice.config.Oauth2Configuration;
import org.json.JSONObject;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

public class OnedriveTokenProvider extends AbstractPlainRESTTokenProvider {

    private static final String GRANT_TYPE = "authorization_code";
    private static final String REDIRECT_URI = "https://login.live.com/oauth20_desktop.srf";

    private final Client client;
    private final Oauth2Configuration oauth2Configuration;
    private String refreshToken;

    public OnedriveTokenProvider(Client client, Oauth2Configuration oauth2Configuration) {
        this.client = client;
        this.oauth2Configuration = oauth2Configuration;
    }

    @Override
    protected String obtainAccessToken(String code) throws AuthorizationException {
        WebTarget webTarget = queryObtainTokenResource(code);
        Response response = webTarget.request(MediaType.APPLICATION_JSON).get();
        String rawResponse = response.readEntity(String.class);

        this.refreshToken = getRefreshTokenFromResponse(rawResponse);

        return getTokenFromResponse(rawResponse);
    }

    @Override
    protected String getRefreshedToken() throws AuthorizationException {
        WebTarget webTarget = queryRefreshToken(refreshToken);
        Response response = webTarget.request(MediaType.APPLICATION_JSON).post(null);
        String rawResponse = response.readEntity(String.class);

        this.refreshToken = getRefreshTokenFromResponse(rawResponse);

        return getTokenFromResponse(rawResponse);
    }

    private WebTarget queryRefreshToken(String refreshToken) {
        return client.target("https://login.live.com/oauth20_token.srf")
                .queryParam("client_id", oauth2Configuration.getAppKey())
                .queryParam("client_secret", oauth2Configuration.getAppKeySecret())
                .queryParam("redirect_uri", REDIRECT_URI)
                .queryParam("refresh_token", refreshToken);
    }

    private String getRefreshTokenFromResponse(String rawResponse) throws AuthorizationException {
        JSONObject responseJson = new JSONObject(rawResponse);

        if (responseJson.has("refresh_token")) {
            return responseJson.getString("refresh_token");
        }

        throw new AuthorizationException(rawResponse);
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
