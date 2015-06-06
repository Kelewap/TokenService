package iosr.tokenservice.refresh;

import iosr.tokenservice.access.TokenProvider;
import org.quartz.*;
import org.quartz.impl.StdSchedulerFactory;

public class RefreshJobScheduler {

    private final Trigger trigger;

    private RefreshJobScheduler(Trigger trigger) {
        this.trigger = trigger;
    }

    public static RefreshJobScheduler createWithDefaultTrigger(String cronSchedule) {
        Trigger trigger = TriggerBuilder
                .newTrigger()
                .withIdentity("defaultRefreshJobTrigger")
                .withSchedule(CronScheduleBuilder.cronSchedule(cronSchedule))
                .build();

        return new RefreshJobScheduler(trigger);
    }

    public void launchRefreshJobFor(TokenProvider tokenProvider, String jobName) throws SchedulerException {
        Scheduler quartzScheduler = new StdSchedulerFactory().getScheduler();
        quartzScheduler.start();
        //TODO: name from the tokenProvider
        JobDetail job = JobBuilder.newJob(RefreshJob.class).withIdentity(jobName).build();
        job.getJobDataMap().put("tokenProvider", tokenProvider);
        quartzScheduler.scheduleJob(job, trigger);
    }

}
