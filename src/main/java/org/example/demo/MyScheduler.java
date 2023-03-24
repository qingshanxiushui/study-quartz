package org.example.demo;

import org.quartz.*;
import org.quartz.impl.StdSchedulerFactory;

import java.util.Date;
import java.util.concurrent.TimeUnit;

/**
 * 定时任务框架Quartz-(一)Quartz入门与Demo搭建 https://blog.csdn.net/noaman_wgs/article/details/80984873
 */
public class MyScheduler {
    public static void main(String[] args) throws SchedulerException, InterruptedException, SchedulerException {
        // 1、创建调度器Scheduler
        SchedulerFactory schedulerFactory = new StdSchedulerFactory();
        Scheduler scheduler = schedulerFactory.getScheduler();
        // 2、创建JobDetail实例，并与PrintWordsJob类绑定(Job执行内容)
        JobDetail jobDetail = JobBuilder.newJob(PrintWordsJob.class)
                .usingJobData("jobDetail1", "这个Job用来测试的") //传入job信息执行job //通过usingJobData可传入不同参数，调用不同参数指job
                .withIdentity("job1", "group1").build();
        // 3、构建Trigger实例,每隔1s执行一次
        Trigger trigger = TriggerBuilder.newTrigger().withIdentity("trigger1", "triggerGroup1")
                .usingJobData("trigger1", "这是jobDetail1的trigger") //传入trigger数据给执行job
                .startNow()//开始时间,立即生效
                .withSchedule(SimpleScheduleBuilder.simpleSchedule()
                        .withIntervalInSeconds(1) //每隔1s执行一次
                        .withMisfireHandlingInstructionNextWithExistingCount()
                        .repeatForever())//一直执行
                //.withSchedule(SimpleScheduleBuilder.simpleSchedule()) //仅立刻执行一次
                .build();
        //4、执行
        scheduler.scheduleJob(jobDetail, trigger);
        System.out.println("--------scheduler start ! ------------");
        scheduler.start();

        //睡眠
        TimeUnit.SECONDS.sleep(5);
        scheduler.shutdown();
        System.out.println("--------scheduler shutdown ! ------------");

    }
}
