package qrsoft.spark;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
@SpringBootApplication(scanBasePackages = {"qrsoft.spark", "qrsoft.common"})
@MapperScan("qrsoft.spark.mapper")
public class SparkApplication {
	public static void main(String[] args) {
		SpringApplication.run(SparkApplication.class, args);
	}
}
