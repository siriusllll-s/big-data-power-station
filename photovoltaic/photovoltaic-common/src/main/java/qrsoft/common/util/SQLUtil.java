package qrsoft.common.util;

public class SQLUtil {
	public static String filter(String value) {
		if (value == null) {
			return "";
		}
		return value.replace("\\", "\\\\")
				.replace("%", "\\%")
				.replace("_", "\\_")
				.replace("'", "''");
	}
}
