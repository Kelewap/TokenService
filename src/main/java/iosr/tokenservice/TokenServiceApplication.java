package iosr.tokenservice;

import io.dropwizard.Application;
import io.dropwizard.setup.Environment;
import iosr.tokenservice.access.*;
import iosr.tokenservice.config.TokenServiceConfiguration;
import jersey.repackaged.com.google.common.collect.ImmutableMap;

import javax.ws.rs.client.ClientBuilder;

public class TokenServiceApplication extends Application<TokenServiceConfiguration> {

    static {
        System.setProperty("dropbox.code", "s9KNb-J7l-AAAAAAAAAAuftS_Ap_xBGe1x3UddNexkY");
        System.setProperty("onedrive.code", "Mc36eba25-65bf-21b1-c2d9-4ef449500aff");
        System.setProperty("google.code", "4/Rz7p4DhmN0-qLuznorb2IFyY1oUtErnecL_WGyLpUwo.co8t9W8N4e8RrjMoGjtSfTqikOeTmwI");
    }

    private static final String DROPBOX_CODE = System.getProperty("dropbox.code");
    private static final String ONEDRIVE_CODE = System.getProperty("onedrive.code");
    private static final String GOOGLE_CODE = System.getProperty("google.code");

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
        GoogleTokenProvider googleTokenProvider = GoogleTokenProvider.createWithDefaultFlow(tokenServiceConfiguration.getGoogleConfig());

        googleTokenProvider.redeemCode(GOOGLE_CODE);
        onedriveTokenProvider.redeemCode(ONEDRIVE_CODE);
        dropboxTokenProvider.redeemCode(DROPBOX_CODE);

        ImmutableMap<String, TokenProvider> tokenProviders = ImmutableMap.<String, TokenProvider>builder()
                .put("dropbox", dropboxTokenProvider)
                .put("onedrive", onedriveTokenProvider)
                .put("google", googleTokenProvider)
                .build();

        environment.jersey().register(new TokenResource(tokenProviders));
    }
}
