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

import java.io.IOException;
import java.util.Collections;

public class GoogleTokenProvider extends AbstractTokenProvider {

    private static final String REDIRECT_URI = "urn:ietf:wg:oauth:2.0:oob";

    private final GoogleAuthorizationCodeFlow flow;
    private Credential credential;

    private GoogleTokenProvider(GoogleAuthorizationCodeFlow flow) {
        this.flow = flow;
    }

    public static GoogleTokenProvider createWithDefaultFlow(Oauth2Configuration oauth2Configuration) {
        return new GoogleTokenProvider(getFlow(oauth2Configuration));
    }

    @Override
    protected String obtainAccessToken(String code) throws AuthorizationException {
        try {
            GoogleTokenResponse response = flow
                    .newTokenRequest(code)
                    .setRedirectUri(REDIRECT_URI)
                    .execute();

            this.credential = flow.createAndStoreCredential(response, null);

            return credential.getAccessToken();
        } catch (IOException e) {
            throw new AuthorizationException(e);
        }
    }

    @Override
    protected String getRefreshedToken() {
        return credential.getRefreshToken();
    }

    private static GoogleAuthorizationCodeFlow getFlow(Oauth2Configuration oauth2Configuration) {
        final HttpTransport HTTP_TRANSPORT = new NetHttpTransport();
        final JsonFactory JSON_FACTORY = JacksonFactory.getDefaultInstance();

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
