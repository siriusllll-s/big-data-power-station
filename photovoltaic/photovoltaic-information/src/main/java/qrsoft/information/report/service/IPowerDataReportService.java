package qrsoft.information.report.service;

import qrsoft.information.dto.input.PowerDataReportInput;
import qrsoft.information.dto.output.PowerDataReportOutput;
import qrsoft.information.dto.page.PowerDataReportPage;
import qrsoft.information.shared.dto.vo.ResultPage;

public interface IPowerDataReportService {

	/**
	 * 电站运行日报修改
	 */
	void update(PowerDataReportInput input);

	/**
	 * 电站运行日报分页查询
	 */
	ResultPage<PowerDataReportOutput> pageByParam(PowerDataReportPage input);

	/**
	 * 电站运行日报详情
	 */
	PowerDataReportOutput detail(Integer id);

	/**
	 * 按日生成/刷新日报（定时任务调用）
	 *
	 * @param station 电站 id
	 * @param day     日期 0 点
	 * @return 是否写入成功
	 */
	boolean generateForDay(Integer station, java.util.Date day);
}
