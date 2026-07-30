package qrsoft.common.constant;

public class ScreenConstant {
	/** 标准煤折算系数 t/万kWh */
	public static final double COAL_RATIO = 0.4;
	/** 二氧化碳减排系数 t/万kWh */
	public static final double CO2_RATIO = 0.997;
	/** 电价 元/kWh（综合收益估算） */
	public static final double PRICE = 0.6;

	/** 节煤折算系数 kg/kWh（对应 REDUCE_COAL_FORMAT：g/kWh → ÷1000 得 kg/kWh 数量级） */
	public static final double REDUCE_COAL_FORMAT = 400D;
	/** CO2 减排折算系数 kg/kWh（对应 REDUCE_CO2_FORMAT：g/kWh → ÷1000） */
	public static final double REDUCE_CO2_FORMAT = 997D;
	/** 电价收益系数 元/MWh → ÷10000 用于万元单位展示（参考元/kWh = 0.6） */
	public static final double MONEY_FORMAT = 6000D;
}
