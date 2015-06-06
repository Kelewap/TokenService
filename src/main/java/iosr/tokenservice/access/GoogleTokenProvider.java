package iosr.tokenservice.access;

import com.google.api.client.auth.oauth2.Credential;
import com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeFlow;
import com.google.api.client.googleapis.auth.oauth2.GoogleClientSecrets;
import com.google.api.client.googleapis.auth.oauth2.GoogleTokenResponse;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.services.drive.DriveScopes;
import iosr.tokenservice.config.Oauth2Configuration;
import org.json.JSONObject;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.io.IOException;
import java.util.Collections;

public class GoogleTokenProvider extends AbstractTokenProvider {

    private static final String REDIRECT_URI = "urn:ietf:wg:oauth:2.0:oob";
    private static final HttpTransport HTTP_TRANSPORT = new NetHttpTransport();
    private static final JsonFactory JSON_FACTORY = JacksonFactory.getDefaultInstance();
    private final Oauth2Configuration oauth2Configuration;

    public GoogleTokenProvider(Oauth2Configuration oauth2Configuration) {
        this.oauth2Configuration = oauth2Configuration;
    }

    @Override
    protected String obtainAccessToken(String code) throws AuthorizationException {
        try {
            //TODO: extract flow creation to builder
            GoogleAuthorizationCodeFlow flow = getFlow();
            GoogleTokenResponse response = flow
                    .newTokenRequest(code)
                    .setRedirectUri(REDIRECT_URI)
                    .execute();
            Credential credential = flow.createAndStoreCredential(response, null);

            return credential.getAccessToken();
        } catch (IOException e) {
            throw new AuthorizationException(e);
        }
    }

    @Override
    protected String getRefreshedToken() {
        //FIXME
        return "bubu";
    }

    private GoogleAuthorizationCodeFlow getFlow() throws IOException {
        GoogleClientSecrets.Details details = new GoogleClientSecrets.Details();
        details.setClientId(oauth2Configuration.getAppKey());
        details.setClientSecret(oauth2Configuration.getAppKeySecret());
        GoogleClientSecrets googleClientSecrets = new GoogleClientSecrets();
        googleClientSecrets.setInstalled(details);

        return new GoogleAuthorizationCodeFlow.Builder(
                HTTP_TRANSPORT, JSON_FACTORY, googleClientSecrets, Collections.singleton(DriveScopes.DRIVE))
                .setAccessType("online")
                .setApprovalPrompt("force")
                .build();
    }
}
