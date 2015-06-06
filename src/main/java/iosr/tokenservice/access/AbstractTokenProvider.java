package iosr.tokenservice.access;

import iosr.tokenservice.Token;
import org.json.JSONObject;

public abstract class AbstractTokenProvider implements TokenProvider {

    private Token token;

    @Override
    public void redeemCode(String authorizationCode) throws AuthorizationException {
        String accessToken = obtainAccessToken(authorizationCode);
        this.token = new Token(accessToken);
    }

    protected abstract String obtainAccessToken(String code) throws AuthorizationException;

    @Override
    public Token getToken() {
        return token;
    }
}
