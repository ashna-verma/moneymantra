package in.ashna.moneymantra;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@EnableScheduling
@SpringBootApplication
public class MoneymantraApplication {

	public static void main(String[] args) {
		SpringApplication.run(MoneymantraApplication.class, args);
	}

}
