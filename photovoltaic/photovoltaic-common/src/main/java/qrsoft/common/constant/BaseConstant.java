package qrsoft.common.constant;
public class BaseConstant {
    public static final Integer STATION = 1;
    public static final Integer ORDER_STATUS_NEW = 1;
    public static final Integer ORDER_STATUS_PROCESSING = 2;
    public static final Integer ORDER_STATUS_SOLVED = 3;
    public static final Integer ORDER_STATUS_CLOSED = 4;
    public static final Integer INSPECT_NOT = 0;
    public static final Integer INSPECTING = 1;
    public static final Integer INSPECTED = 2;
    public static final Integer INSPECTUN_DONE = 3;
    public static final Integer DATE_DAY = 0;
    public static final Integer DATE_MONTH = 1;
    public static final Integer DATE_YEAR = 2;
    public static final int DEVICE_INVERTER = 0;
    public static final int DEVICE_COMBINER_BOX = 1;
    public static final int DEVICE_DC_CABINET = 2;
    public static final int DEVICE_WEATHER = 3;
    public static final int DEVICE_AMMETER = 4;
    public static final String[] ammeterList = {"01号电表", "02号电表"};
    public static final String[] inverterList = {"01号逆变器", "02号逆变器"};
    public static final String[] dcCabinetList = {"01号直流柜", "02号直流柜"};
    public static final String[][] combinerBoxList = {{"01号汇流箱", "02号汇流箱"}, {"03号汇流箱", "04号汇流箱", "05号汇流箱"}};
    public static final String VOLTAGE = "220";
}
