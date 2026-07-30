package qrsoft.common.util;

import java.util.Map;

public class MapUtils {
	public static String getString(Map<?, ?> map, String key, String defaultVal) {
		if (map == null || key == null) {
			return defaultVal;
		}
		Object val = map.get(key);
		if (val == null) {
			return defaultVal;
		}
		return String.valueOf(val);
	}

	public static Integer getInteger(Map<?, ?> map, String key, Integer defaultVal) {
		if (map == null || key == null) {
			return defaultVal;
		}
		Object val = map.get(key);
		if (val == null) {
			return defaultVal;
		}
		if (val instanceof Number) {
			return ((Number) val).intValue();
		}
		try {
			return Integer.parseInt(String.valueOf(val));
		} catch (Exception e) {
			return defaultVal;
		}
	}
}
