package org.example.demo;

import org.quartz.*;
import org.quartz.impl.StdSchedulerFactory;

import java.util.Date;
import java.util.concurrent.TimeUnit;

/**
 * 定时任务框架Quartz-(一)Quartz入门与Demo搭建 https://blog.csdn.net/noaman_wgs/article/details/80984873
 * 定时任务quartz相关cron表达式详解  https://blog.csdn.net/Z_Ascll/article/details/124602226
 */
public class MyScheduler2 {
    public static void main(String[] args) throws SchedulerException, InterruptedException {
        // 1、创建调度器Scheduler
        SchedulerFactory schedulerFactory = new StdSchedulerFactory();
        Scheduler scheduler = schedulerFactory.getScheduler();
        // 2、创建JobDetail实例，并与PrintWordsJob类绑定(Job执行内容)
        JobDetail jobDetail = JobBuilder.newJob(PrintWordsJob.class)
                .usingJobData("jobDetail1", "这个Job用来测试的")
                .withIdentity("job1", "group1").build();
        // 3、构建Trigger实例,每隔1s执行一次
        Date startDate = new Date();
        startDate.setTime(startDate.getTime() + 3000); //3秒之后

        Date endDate = new Date();
        endDate.setTime(startDate.getTime() + 9000);

        CronTrigger cronTrigger = TriggerBuilder.newTrigger().withIdentity("trigger1", "triggerGroup1")
                .usingJobData("trigger1", "这是jobDetail1的trigger")
                .startNow()//立即生效
                .startAt(startDate) //设置开始时间
                .endAt(endDate) //结束时间
                .withSchedule(CronScheduleBuilder.cronSchedule("*/3 * * * * ? *").withMisfireHandlingInstructionDoNothing())
                .build();

        //4、执行
        scheduler.scheduleJob(jobDetail, cronTrigger);
        System.out.println("--------scheduler start ! ------------");
        scheduler.start();
        //睡眠
        TimeUnit.MINUTES.sleep(1);
        scheduler.shutdown();
        System.out.println("--------scheduler shutdown ! ------------");

    }
}
