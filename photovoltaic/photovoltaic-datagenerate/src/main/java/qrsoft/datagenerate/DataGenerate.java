package qrsoft.datagenerate;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;
@EnableScheduling
@SpringBootApplication(scanBasePackages = {"qrsoft.datagenerate", "qrsoft.common"})
public class DataGenerate {
	public static void main(String[] args) {
		SpringApplication.run(DataGenerate.class, args);
	}
}
