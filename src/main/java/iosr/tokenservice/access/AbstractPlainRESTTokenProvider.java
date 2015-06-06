package iosr.tokenservice.access;

import org.json.JSONObject;

public abstract class AbstractPlainRESTTokenProvider extends AbstractTokenProvider {

    protected String getTokenFromResponse(String rawResponse) throws AuthorizationException {
        JSONObject responseJson = new JSONObject(rawResponse);

        if (responseJson.has("access_token")) {
            return responseJson.getString("access_token");
        }

        throw new AuthorizationException(responseJson.toString());
    }
}
