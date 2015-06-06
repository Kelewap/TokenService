package iosr.tokenservice;

import io.dropwizard.Application;
import io.dropwizard.setup.Environment;
import iosr.tokenservice.access.AuthorizationException;
import iosr.tokenservice.access.DropboxTokenProvider;
import iosr.tokenservice.access.OnedriveTokenProvider;
import iosr.tokenservice.access.TokenProvider;
import iosr.tokenservice.config.TokenServiceConfiguration;
import jersey.repackaged.com.google.common.collect.ImmutableMap;

import javax.ws.rs.client.ClientBuilder;

public class TokenServiceApplication extends Application<TokenServiceConfiguration> {

    static {
        System.setProperty("dropbox.code", "s9KNb-J7l-AAAAAAAAAAt4ygAdzbEpqxoZT0XJ8i4qw");
        System.setProperty("onedrive.code", "Mec167884-4cd0-094a-ef1f-06b194ecde25");
    }

    private static final String DROPBOX_CODE = System.getProperty("dropbox.code");
    private static final String ONEDRIVE_CODE = System.getProperty("onedrive.code");

    public static void main(String[] args) throws Exception {
        new TokenServiceApplication().run(args);
    }

    @Override
    public String getName() {
        return "Token Service";
    }

    @Override
    public void run(TokenServiceConfiguration tokenServiceConfiguration, Environment environment) throws AuthorizationException {
        DropboxTokenProvider dropboxTokenProvider = new DropboxTokenProvider(ClientBuilder.newClient(), tokenServiceConfiguration.getDropboxConfig());
        OnedriveTokenProvider onedriveTokenProvider = new OnedriveTokenProvider(ClientBuilder.newClient(), tokenServiceConfiguration.getOnedriveConfig());
        
        onedriveTokenProvider.redeemCode(ONEDRIVE_CODE);
        dropboxTokenProvider.redeemCode(DROPBOX_CODE);

        ImmutableMap<String, TokenProvider> tokenProviders = ImmutableMap.<String, TokenProvider>builder()
                .put("dropbox", dropboxTokenProvider)
                .put("onedrive", onedriveTokenProvider)
                .build();

        environment.jersey().register(new TokenResource(tokenProviders));
    }
}
