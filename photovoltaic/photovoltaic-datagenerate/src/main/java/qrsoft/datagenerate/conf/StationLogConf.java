package qrsoft.datagenerate.conf;
import com.alibaba.fastjson.JSONObject;
import org.springframework.stereotype.Component;
import qrsoft.common.constant.PathConstant;
import qrsoft.common.entity.*;
import qrsoft.datagenerate.utils.FileOptUtil;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
@Component
public class StationLogConf {
	private final String[] ammeterList = {"01号电表", "02号电表"};
	private final String[] ammeterLast = {"0.0", "0.0"};
	private final String[] inverterList = {"01号逆变器", "02号逆变器"};
	private final String[] dcCabinetList = {"01号直流柜", "02号直流柜"};
	private final String[][] combinerBoxList = {{"01号汇流箱", "02号汇流箱"}, {"03号汇流箱", "04号汇流箱", "05号汇流箱"}};
	private static final DecimalFormat df = new DecimalFormat("######0.000000");
	private int count = 1;
	public void generateStationData(Integer station, String date) {
		for (int i = 0; i < ammeterList.length; i++) {
			boolean noError = true;
			List<String> dcCabinet = new ArrayList<String>();
			for (int j = 0; j < combinerBoxList[i].length; j++) {
				List<String> amperesList = generateAmperes(8);
				double a = randomPercentage();
				if (a > 1.00) noError = false;
				double sum = 0;
				for (String s : amperesList) sum += Double.parseDouble(s);
				String ampereOut = String.format("%.6f", sum * a);
				dcCabinet.add(ampereOut);
				DataCombinerBox combinerBox = new DataCombinerBox();
				combinerBox.setStation(station);
				combinerBox.setAmmeterName(ammeterList[i]);
				combinerBox.setInverterName(inverterList[i]);
				combinerBox.setDcCabinetName(dcCabinetList[i]);
				combinerBox.setName(combinerBoxList[i][j]);
				combinerBox.setCreateTime(date);
				combinerBox.setCombinerBoxIns(amperesList);
				combinerBox.setCombinerBox(ampereOut);
				FileOptUtil.fileWrite(PathConstant.COMBINER_BOX, JSONObject.toJSONString(combinerBox), true);
			}
			double b = randomPercentage();
			if (b > 1.0) noError = false;
			double sum2 = 0;
			for (String s : dcCabinet) sum2 += Double.parseDouble(s);
			String dcCabinetOut = String.format("%.6f", sum2 * b);
			DataDCCabinet dataDCCabinet = new DataDCCabinet();
			dataDCCabinet.setStation(station);
			dataDCCabinet.setAmmeterName(ammeterList[i]);
			dataDCCabinet.setInverterName(inverterList[i]);
			dataDCCabinet.setName(dcCabinetList[i]);
			dataDCCabinet.setCreateTime(date);
			dataDCCabinet.setDcCabinetIns(dcCabinet);
			dataDCCabinet.setDcCabinet(dcCabinetOut);
			FileOptUtil.fileWrite(PathConstant.DC_CABINET, JSONObject.toJSONString(dataDCCabinet), true);
			double c = randomPercentage();
			if (c > 1.0) noError = false;
			String inverter = String.format("%.6f", (1000 / (Double.parseDouble(dcCabinetOut) * 15)) * c);
			DataInverter dataInverter = new DataInverter();
			dataInverter.setStation(station);
			dataInverter.setAmmeterName(ammeterList[i]);
			dataInverter.setName(inverterList[i]);
			dataInverter.setCreateTime(date);
			dataInverter.setDcCabinet(dcCabinetOut);
			dataInverter.setInverter(inverter);
			FileOptUtil.fileWrite(PathConstant.INVERTER, JSONObject.toJSONString(dataInverter), true);
			double d = randomPercentage();
			if (d > 1.0) noError = false;
			String ammeter = String.format("%.6f", Double.parseDouble(inverter) * d + Double.parseDouble(ammeterLast[i]));
			if (noError) ammeterLast[i] = ammeter;
			DataAmmeter dataAmmeter = new DataAmmeter();
			dataAmmeter.setStation(station);
			dataAmmeter.setName(ammeterList[i]);
			dataAmmeter.setCreateTime(date);
			dataAmmeter.setInverter(inverter);
			dataAmmeter.setAmmeter(ammeter);
			FileOptUtil.fileWrite(PathConstant.AMMETER, JSONObject.toJSONString(dataAmmeter), true);
		}
	}
	public void generateStationWeatherData(Integer station, String date) {
		Random random = new Random();
		DataWeather weather = new DataWeather();
		weather.setStation(station);
		weather.setName("气象仪");
		weather.setCreateTime(date);
		weather.setIrradiance(random.nextDouble() * 10 + 20);
		weather.setAmbientTemperature(random.nextDouble() * 10 + 20);
		weather.setBatteryPanelTemperature(random.nextDouble() * 10 + 20);
		weather.setWindSpeed((double) random.nextInt(25));
		weather.setWindDirection((double) random.nextInt(360));
		FileOptUtil.fileWrite(PathConstant.WEATHER, JSONObject.toJSONString(weather), true);
	}
	private double randomPercentage() {
		if (Math.random() > 0.7) count++;
		if (count == 1000) { count = 1; return 1.2; }
		return Math.random() * 0.50 + 0.50;
	}
	public static List<String> generateAmperes(int num) {
		List<String> list = new ArrayList<String>();
		for (int i = 0; i < num; i++) list.add(df.format(Math.random() * 2.0 + 1.0));
		return list;
	}
}
