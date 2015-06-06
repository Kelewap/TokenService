package iosr.tokenservice;

import io.dropwizard.Application;
import io.dropwizard.setup.Environment;
import iosr.tokenservice.access.*;
import iosr.tokenservice.config.TokenServiceConfiguration;
import iosr.tokenservice.refresh.RefreshJobScheduler;
import jersey.repackaged.com.google.common.collect.ImmutableMap;
import org.quartz.SchedulerException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.ws.rs.client.ClientBuilder;

public class TokenServiceApplication extends Application<TokenServiceConfiguration> {

    private static final Logger LOGGER = LoggerFactory.getLogger(TokenServiceApplication.class);
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

        try {
            RefreshJobScheduler refreshJobScheduler = RefreshJobScheduler.createWithDefaultTrigger(tokenServiceConfiguration.getRefreshSchedule());
            refreshJobScheduler.launchRefreshJobFor(onedriveTokenProvider, "onedrive");
        } catch (SchedulerException e) {
            LOGGER.error("Error with token refreshing jobs", e);
        }

        ImmutableMap<String, TokenProvider> tokenProviders = ImmutableMap.<String, TokenProvider>builder()
                .put("dropbox", dropboxTokenProvider)
                .put("onedrive", onedriveTokenProvider)
                .put("google", googleTokenProvider)
                .build();

        environment.jersey().register(new TokenResource(tokenProviders));
    }

}
