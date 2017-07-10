package com.softbistro.orderbooks;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import com.github.messenger4j.MessengerPlatform;
import com.github.messenger4j.send.MessengerSendClient;

@SpringBootApplication
public class OrderBooksApplication {

	private static final Logger LOGGER = Logger.getLogger(OrderBooksApplication.class);

	/**
	 * Initializes the {@code MessengerSendClient}.
	 *
	 * @param pageAccessToken
	 *            the generated {@code Page Access Token}
	 */
	@Bean
	public MessengerSendClient messengerSendClient(@Value("${messenger4j.pageAccessToken}") String pageAccessToken) {
		LOGGER.info("Initializing MessengerSendClient - pageAccessToken: " + pageAccessToken);
		return MessengerPlatform.newSendClientBuilder(pageAccessToken).build();
	}

	public static void main(String[] args) {
		SpringApplication.run(OrderBooksApplication.class, args);
	}
}
