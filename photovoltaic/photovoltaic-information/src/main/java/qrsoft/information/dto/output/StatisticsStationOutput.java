package qrsoft.information.dto.output;

import lombok.Data;
import qrsoft.common.entity.KWhStation;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;

@Data
public class StatisticsStationOutput {
	private Double kwh;
	private Double radiation;
	private Double powerRatio;
	private Date powerDate;
	private String powerDateDay;
	private String powerDateMonth;

	public static StatisticsStationOutput entityToOutputDay(KWhStation e) {
		StatisticsStationOutput o = new StatisticsStationOutput();
		if (e == null) {
			return o;
		}
		o.setKwh(scale(e.getKwh()));
		o.setRadiation(scale(e.getRadiation()));
		o.setPowerRatio(scale(e.getPowerRatio()));
		o.setPowerDate(e.getPowerDate());
		if (e.getPowerDate() != null) {
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
			sdf.setTimeZone(java.util.TimeZone.getTimeZone("Asia/Shanghai"));
			o.setPowerDateDay(sdf.format(e.getPowerDate()));
		}
		return o;
	}

	public static List<StatisticsStationOutput> entityToOutputMonth(Map<String, List<KWhStation>> map) {
		List<StatisticsStationOutput> list = new ArrayList<>();
		if (map == null) {
			return list;
		}
		List<String> keys = new ArrayList<>(map.keySet());
		keys.sort(String::compareTo);
		for (String key : keys) {
			List<KWhStation> stations = map.get(key);
			if (stations == null || stations.isEmpty()) {
				continue;
			}
			double kwh = 0, radiation = 0, ratio = 0;
			int n = 0;
			Date any = null;
			for (KWhStation s : stations) {
				if (s.getKwh() != null) {
					kwh += s.getKwh();
				}
				if (s.getRadiation() != null) {
					radiation += s.getRadiation();
				}
				if (s.getPowerRatio() != null) {
					ratio += s.getPowerRatio();
					n++;
				}
				if (any == null) {
					any = s.getPowerDate();
				}
			}
			StatisticsStationOutput o = new StatisticsStationOutput();
			o.setKwh(scale(kwh));
			o.setRadiation(scale(radiation));
			o.setPowerRatio(n == 0 ? 0D : scale(ratio / n));
			o.setPowerDate(any);
			// normalize key yyyy-M -> yyyy-MM
			try {
				String[] p = key.split("-");
				int y = Integer.parseInt(p[0]);
				int m = Integer.parseInt(p[1]);
				o.setPowerDateMonth(String.format("%04d-%02d", y, m));
			} catch (Exception ex) {
				o.setPowerDateMonth(key);
			}
			list.add(o);
		}
		return list;
	}

	private static Double scale(Double v) {
		if (v == null) {
			return 0D;
		}
		return BigDecimal.valueOf(v).setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
	}
}
