package iosr.tokenservice.access;

import iosr.tokenservice.Token;
import iosr.tokenservice.config.Oauth2Configuration;
import org.json.JSONObject;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

public abstract class AbstractTokenProvider implements TokenProvider {

    private Token token;

    @Override
    public void redeemCode(String authorizationCode) throws AuthorizationException {
        JSONObject responseJson = requestForAccessToken(authorizationCode);

        if (!responseJson.has("access_token")) {
            throw new AuthorizationException(responseJson.toString());
        }

        String accessToken = responseJson.get("access_token").toString();
        this.token = new Token(accessToken);
    }

    protected abstract JSONObject requestForAccessToken(String code);

    @Override
    public Token getToken() {
        return token;
    }
}
