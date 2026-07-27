package qrsoft.datagenerate.schedule;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import qrsoft.common.constant.BaseConstant;
import qrsoft.common.util.DateUtil;
import qrsoft.datagenerate.conf.StationLogConf;
@Component
public class StationDataTask {
	@Autowired
	private StationLogConf stationLogConf;
	@Scheduled(cron = "*/15 * * * * *")
	public void generateStationLog() {
		stationLogConf.generateStationData(BaseConstant.STATION, DateUtil.currentDateStr(DateUtil.YYMMDD_HHMMSS));
		System.out.println("有太阳，产生一次电站日志信息，时间：" + DateUtil.currentDateStr(DateUtil.YYMMDD_HHMMSS) + "。下次产生将在15秒后运行。");
	}
	@Scheduled(fixedDelay = 1000 * 60 * 60, initialDelay = 1000)
	public void generateStationWeatherLog() {
		stationLogConf.generateStationWeatherData(BaseConstant.STATION, DateUtil.currentDateStr(DateUtil.YYMMDD_HHMMSS));
		System.out.println("产生一次电站气象信息，时间：" + DateUtil.currentDateStr(DateUtil.YYMMDD_HHMMSS) + "。下次产生将在一小时后运行。");
	}
}
