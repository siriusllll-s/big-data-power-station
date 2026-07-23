package qrsoft.spark.spark;
import com.alibaba.fastjson.JSONObject;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.spark.SparkConf;
import org.apache.spark.streaming.Durations;
import org.apache.spark.streaming.api.java.JavaInputDStream;
import org.apache.spark.streaming.api.java.JavaStreamingContext;
import org.apache.spark.streaming.kafka010.ConsumerStrategies;
import org.apache.spark.streaming.kafka010.KafkaUtils;
import org.apache.spark.streaming.kafka010.LocationStrategies;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;
import qrsoft.common.constant.SparkConstant;
import qrsoft.common.entity.*;
import qrsoft.spark.service.*;
import java.util.*;
@Component
public class SparkStreamDirect implements ApplicationRunner {
	@Value("${spring.kafka.bootstrap-servers}")
	private String kafkaBootstrapServers;
	@Value("${spring.kafka.consumer.group-id}")
	private String kafkaGroupId;
	@Value("${spring.kafka.consumer.enable-auto-commit}")
	private Boolean kafkaEnableAutoCommit;
	@Value("${spring.kafka.consumer.key-deserializer}")
	private String kafkaKeyDeserializer;
	@Value("${spring.kafka.consumer.value-deserializer}")
	private String kafkaValueDeserializer;
	@Autowired private WeatherService weatherService;
	@Autowired private CombinerBoxService combinerBoxService;
	@Autowired private InverterService inverterService;
	@Autowired private DcCabinetService dcCabinetService;
	@Autowired private AmmeterService ammeterService;
	private final String[] topicArr = {SparkConstant.AMMETER, SparkConstant.COMBINER_BOX, SparkConstant.DC_CABINET, SparkConstant.INVERTER, SparkConstant.WEATHER};
	@Override
	public void run(ApplicationArguments args) {
		new Thread(new Runnable() {
			@Override
			public void run() {
				startStream();
			}
		}, "spark-stream-direct").start();
	}
	private void startStream() {
		SparkConf conf = new SparkConf().setAppName("qst-etl").setMaster("local[*]");
		conf.set("spark.serializer", "org.apache.spark.serializer.KryoSerializer");
		JavaStreamingContext streamingContext = new JavaStreamingContext(conf, Durations.seconds(5));
		Map<String, Object> kafkaParam = new HashMap<String, Object>();
		kafkaParam.put("bootstrap.servers", kafkaBootstrapServers);
		kafkaParam.put(ConsumerConfig.GROUP_ID_CONFIG, kafkaGroupId);
		kafkaParam.put(ConsumerConfig.ENABLE_AUTO_COMMIT_CONFIG, kafkaEnableAutoCommit);
		kafkaParam.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, kafkaKeyDeserializer);
		kafkaParam.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, kafkaValueDeserializer);
		kafkaParam.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "earliest");
		Collection<String> topics = Arrays.asList(topicArr);
		JavaInputDStream<ConsumerRecord<String, String>> stream = KafkaUtils.createDirectStream(
				streamingContext, LocationStrategies.PreferConsistent(),
				ConsumerStrategies.<String, String>Subscribe(topics, kafkaParam));
		stream.foreachRDD(rdd -> {
			List<ConsumerRecord<String, String>> collect = rdd.collect();
			for (ConsumerRecord<String, String> o : collect) {
				String topic = o.topic();
				try {
					switch (topic) {
						case SparkConstant.AMMETER:
							ammeterService.save(JSONObject.parseObject(o.value(), DataAmmeter.class));
							break;
						case SparkConstant.WEATHER:
							weatherService.save(JSONObject.parseObject(o.value(), DataWeather.class));
							break;
						case SparkConstant.DC_CABINET:
							dcCabinetService.save(JSONObject.parseObject(o.value(), DataDCCabinet.class));
							break;
						case SparkConstant.INVERTER:
							inverterService.save(JSONObject.parseObject(o.value(), DataInverter.class));
							break;
						case SparkConstant.COMBINER_BOX:
							combinerBoxService.save(JSONObject.parseObject(o.value(), DataCombinerBox.class));
							break;
						default:
							break;
					}
				} catch (Exception e) {
					System.err.println("处理消息失败 topic=" + topic + " err=" + e.getMessage());
				}
			}
		});
		streamingContext.start();
		try { streamingContext.awaitTermination(); } catch (Exception e) { e.printStackTrace(); }
	}
}
