package qrsoft.information.shared.utils;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.hbase.HBaseConfiguration;
import org.apache.hadoop.hbase.TableName;
import org.apache.hadoop.hbase.client.*;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicBoolean;

public class HBaseUtil {

	private static volatile Connection connection;
	private static final AtomicBoolean AVAILABLE = new AtomicBoolean(true);

	public static boolean isAvailable() {
		return AVAILABLE.get();
	}

	public static void markUnavailable() {
		AVAILABLE.set(false);
	}

	public static synchronized Connection getConnection() throws IOException {
		if (!AVAILABLE.get()) {
			throw new IOException("HBase marked unavailable");
		}
		if (connection == null || connection.isClosed()) {
			Configuration conf = HBaseConfiguration.create();
			conf.set("hbase.zookeeper.quorum", "master");
			conf.set("hbase.zookeeper.property.clientPort", "2181");
			conf.set("zookeeper.znode.parent", "/hbase");
			conf.set("hbase.client.retries.number", "1");
			conf.set("hbase.client.pause", "100");
			conf.set("hbase.rpc.timeout", "2000");
			conf.set("hbase.client.operation.timeout", "3000");
			conf.set("hbase.client.scanner.timeout.period", "3000");
			conf.set("zookeeper.recovery.retry", "0");
			conf.set("zookeeper.session.timeout", "3000");
			connection = ConnectionFactory.createConnection(conf);
		}
		return connection;
	}

	public static void insertRowData(String tableName, String rowKey, String family, Map<String, String> data) throws Exception {
		Table table = getConnection().getTable(TableName.valueOf(tableName));
		List<Put> puts = new ArrayList<>();
		for (Map.Entry<String, String> entry : data.entrySet()) {
			if (entry.getValue() == null) {
				continue;
			}
			Put put = new Put(rowKey.getBytes(StandardCharsets.UTF_8));
			put.addColumn(family.getBytes(StandardCharsets.UTF_8),
					entry.getKey().getBytes(StandardCharsets.UTF_8),
					entry.getValue().getBytes(StandardCharsets.UTF_8));
			puts.add(put);
		}
		if (!puts.isEmpty()) {
			table.put(puts);
		}
		table.close();
	}

	/**
	 * 通过rowKey获取某一行数据
	 */
	public static Result getRow(String tableName, String rowKey) throws Exception {
		if (!AVAILABLE.get()) {
			throw new IOException("HBase marked unavailable");
		}
		Table table = getConnection().getTable(TableName.valueOf(tableName));
		try {
			Get get = new Get(rowKey.getBytes(StandardCharsets.UTF_8));
			return table.get(get);
		} finally {
			table.close();
		}
	}
}
