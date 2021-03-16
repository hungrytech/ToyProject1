package ToyProject1.hungrytech;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@EnableJpaAuditing
@SpringBootApplication
public class HungrytechApplication {

	public static void main(String[] args) {
		SpringApplication.run(HungrytechApplication.class, args);
	}

}
