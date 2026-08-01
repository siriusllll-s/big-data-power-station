package qrsoft.information.report.task;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import qrsoft.common.constant.BaseConstant;
import qrsoft.common.util.DateUtil;
import qrsoft.information.report.service.IPowerDataReportService;

import java.util.Date;

/**
 * 定时任务：根据 k_wh_station / weather 生成电站运行日报。
 * - 每天 00:10 生成「昨天」日报
 * - 启动时补齐近 30 天（缺则生成），便于实验环境立刻有数据
 */
@Component
public class PowerDataReportTask implements ApplicationRunner {

	private static final Logger log = LoggerFactory.getLogger(PowerDataReportTask.class);

	@Autowired
	private IPowerDataReportService powerDataReportService;

	/** 每天 0 点 10 分生成昨日日报 */
	@Scheduled(cron = "0 10 0 * * ?")
	public void generateYesterday() {
		Date yesterday = DateUtil.addDay(DateUtil.getToDayDate(), -1);
		boolean ok = powerDataReportService.generateForDay(BaseConstant.STATION, yesterday);
		log.info("generate power data report for {} result={}", DateUtil.dateToString(yesterday, DateUtil.YYMMDD), ok);
	}

	/** 可选：每小时刷新「今天」日报（实时看当天数据） */
	@Scheduled(cron = "0 15 * * * ?")
	public void refreshToday() {
		Date today = DateUtil.getToDayDate();
		boolean ok = powerDataReportService.generateForDay(BaseConstant.STATION, today);
		log.debug("refresh today report {} result={}", DateUtil.dateToString(today, DateUtil.YYMMDD), ok);
	}

	@Override
	public void run(ApplicationArguments args) {
		// 启动补数据：近 30 天
		Date today = DateUtil.getToDayDate();
		int success = 0;
		for (int i = 0; i < 30; i++) {
			Date day = DateUtil.addDay(today, -i);
			try {
				if (powerDataReportService.generateForDay(BaseConstant.STATION, day)) {
					success++;
				}
			} catch (Exception e) {
				log.warn("bootstrap report day {} failed: {}", DateUtil.dateToString(day, DateUtil.YYMMDD), e.getMessage());
			}
		}
		log.info("bootstrap power_data_report finished, successDays≈{}", success);
	}
}
