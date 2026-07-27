package qrsoft.information.utils;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

public class DateUtils {

	private static SimpleDateFormat dayFmt() {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		sdf.setTimeZone(TimeZone.getTimeZone("Asia/Shanghai"));
		return sdf;
	}

	/**
	 * 近30天区间（含今天），[0]=开始 [1]=结束
	 */
	public static String[] lastThityDayBetween() {
		Calendar cal = Calendar.getInstance(TimeZone.getTimeZone("Asia/Shanghai"));
		cal.set(Calendar.HOUR_OF_DAY, 0);
		cal.set(Calendar.MINUTE, 0);
		cal.set(Calendar.SECOND, 0);
		cal.set(Calendar.MILLISECOND, 0);
		Date end = cal.getTime();
		cal.add(Calendar.DAY_OF_YEAR, -29);
		Date start = cal.getTime();
		SimpleDateFormat sdf = dayFmt();
		return new String[]{sdf.format(start), sdf.format(end)};
	}

	/**
	 * 未来30天区间（从明天起）
	 */
	public static String[] nextThityDayBetween() {
		Calendar cal = Calendar.getInstance(TimeZone.getTimeZone("Asia/Shanghai"));
		cal.set(Calendar.HOUR_OF_DAY, 0);
		cal.set(Calendar.MINUTE, 0);
		cal.set(Calendar.SECOND, 0);
		cal.set(Calendar.MILLISECOND, 0);
		cal.add(Calendar.DAY_OF_YEAR, 1);
		Date start = cal.getTime();
		cal.add(Calendar.DAY_OF_YEAR, 29);
		Date end = cal.getTime();
		SimpleDateFormat sdf = dayFmt();
		return new String[]{sdf.format(start), sdf.format(end)};
	}
}
