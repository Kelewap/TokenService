package iosr.tokenservice.refresh;

import iosr.tokenservice.access.AuthorizationException;
import iosr.tokenservice.access.TokenProvider;
import org.quartz.Job;
import org.quartz.JobDataMap;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class RefreshJob implements Job {

    private static final Logger LOGGER = LoggerFactory.getLogger(RefreshJob.class);

    @Override
    public void execute(JobExecutionContext jobExecutionContext) throws JobExecutionException {
        //FIXME this quartz sucks as a vacuum cleaner
        JobDataMap jobDataMap = jobExecutionContext.getJobDetail().getJobDataMap();
        TokenProvider tokenProvider = (TokenProvider) jobDataMap.get("tokenProvider");

        try {
            tokenProvider.refreshToken();
        } catch (AuthorizationException e) {
            //TODO: add provider instance information in exceptions and anywhere - some name property somewhere
            LOGGER.error("Error when refreshing token", e);
        }
    }
}
