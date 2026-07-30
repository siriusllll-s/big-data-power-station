package com.example.demo.task;

import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;

@Configuration
@EnableScheduling
public class StaticScheduleTask {
	@Scheduled(cron = "0/5 * * * * ?")
	private void testTask() {
		System.out.println("定时任务");
	}
}
