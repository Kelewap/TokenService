package iosr.tokenservice;

import io.dropwizard.Application;
import io.dropwizard.setup.Environment;
import iosr.tokenservice.access.AuthorizationException;
import iosr.tokenservice.access.DropboxTokenProvider;
import iosr.tokenservice.config.TokenServiceConfiguration;

import javax.ws.rs.client.ClientBuilder;

public class TokenServiceApplication extends Application<TokenServiceConfiguration> {

    private static final String DROPBOX_CODE = System.getProperty("dropbox.code");

    public static void main(String[] args) throws Exception {
        new TokenServiceApplication().run(args);
    }

    @Override
    public String getName() {
        return "Token Service";
    }

    @Override
    public void run(TokenServiceConfiguration tokenServiceConfiguration, Environment environment) throws AuthorizationException {
        DropboxTokenProvider tokenProvider = new DropboxTokenProvider(ClientBuilder.newClient(), tokenServiceConfiguration.getOauth2Configuration());
        tokenProvider.redeemCode(DROPBOX_CODE);

        environment.jersey().register(new TokenResource(tokenProvider));
    }
}
