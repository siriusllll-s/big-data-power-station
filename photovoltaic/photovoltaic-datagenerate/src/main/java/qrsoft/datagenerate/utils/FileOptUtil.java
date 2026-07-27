package qrsoft.datagenerate.utils;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
public class FileOptUtil {
	public static void fileWrite(String path, String content, boolean isAppend) {
		PrintWriter pw = null;
		try {
			File file = new File(path);
			File parent = file.getParentFile();
			if (parent != null && !parent.exists()) parent.mkdirs();
			FileWriter fw = new FileWriter(file, isAppend);
			pw = new PrintWriter(fw);
			if (!file.exists()) file.createNewFile();
			pw.println(content);
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (pw != null) pw.close();
		}
	}
}
