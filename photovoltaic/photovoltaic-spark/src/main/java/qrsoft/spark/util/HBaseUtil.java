package qrsoft.spark.util;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.hbase.HBaseConfiguration;
import org.apache.hadoop.hbase.TableName;
import org.apache.hadoop.hbase.client.*;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
public class HBaseUtil {
	private static Connection connection;
	static {
		try {
			Configuration configuration = HBaseConfiguration.create();
			configuration.set("hbase.zookeeper.quorum", "master");
			configuration.set("hbase.zookeeper.property.clientPort", "2181");
			configuration.set("zookeeper.znode.parent", "/hbase");
			connection = ConnectionFactory.createConnection(configuration);
		} catch (IOException e) {
			System.err.println("HBase connection init failed: " + e.getMessage());
		}
	}
	public static void insertRowData(String tableName, String rowKey, String columnFamily, Map<String, String> kv) throws Exception {
		if (connection == null || connection.isClosed()) {
			Configuration configuration = HBaseConfiguration.create();
			configuration.set("hbase.zookeeper.quorum", "master");
			configuration.set("hbase.zookeeper.property.clientPort", "2181");
			connection = ConnectionFactory.createConnection(configuration);
		}
		TableName tName = TableName.valueOf(tableName);
		Table table = connection.getTable(tName);
		List<Put> list = new ArrayList<Put>();
		for (Map.Entry<String, String> entry : kv.entrySet()) {
			if (entry.getValue() == null) continue;
			Put put = new Put(rowKey.getBytes(StandardCharsets.UTF_8));
			put.addColumn(columnFamily.getBytes(StandardCharsets.UTF_8), entry.getKey().getBytes(StandardCharsets.UTF_8), entry.getValue().getBytes(StandardCharsets.UTF_8));
			list.add(put);
		}
		if (!list.isEmpty()) table.put(list);
		table.close();
	}
	public Result getRow(String tableName, String rowKey) throws Exception {
		Table table = connection.getTable(TableName.valueOf(tableName));
		Result result = table.get(new Get(rowKey.getBytes(StandardCharsets.UTF_8)));
		table.close();
		return result;
	}
}
