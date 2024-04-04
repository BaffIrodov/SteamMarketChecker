package net.smc;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class SteamMarketCheckerApplication {

	public static void main(String[] args) {
		SpringApplication.run(SteamMarketCheckerApplication.class, args);
	}
}
