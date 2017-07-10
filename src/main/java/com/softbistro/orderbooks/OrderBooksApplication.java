package com.softbistro.orderbooks;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import com.github.messenger4j.MessengerPlatform;
import com.github.messenger4j.send.MessengerSendClient;
import com.github.messenger4j.send.buttons.Button;
import com.github.messenger4j.send.templates.GenericTemplate;
import com.softbistro.orderbooks.components.entity.SearchResult;

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

	public static void main(String[] args) throws IOException {
		SpringApplication.run(OrderBooksApplication.class, args);	
		
		List<SearchResult> searchResults = new ArrayList<>();
		SearchResult searchResult = new SearchResult("Title1", "google.com", "Subtitle1", "Summary");
		searchResults.add(searchResult);
		searchResult = new SearchResult("Title2", "Link2", "Subtitle2", "Summary2");
		searchResults.add(searchResult);
		searchResult = new SearchResult("Title3", "Link3", "Subtitle3", "Summary3");
		searchResults.add(searchResult);
		searchResult = new SearchResult("Title4", "Link4", "Subtitle4", "Summary4");
		searchResults.add(searchResult);
		searchResults.stream().limit(3).collect(Collectors.toList());
		final List<Button> firstLink = Button.newListBuilder().addUrlButton("Open Link", searchResults.get(0).getLink())
				.toList().build();
		final GenericTemplate genericTemplate = GenericTemplate.newBuilder().addElements()
				.addElement(searchResults.get(0).getTitle()).subtitle(searchResults.get(0).getSubtitle())
				.itemUrl(searchResults.get(0).getLink())
				.imageUrl("https://upload.wikimedia.org/wikipedia/en/2/20/Pivotal_Java_Spring_Logo.png")
				.buttons(firstLink).toList().done().build();
		System.out.println(genericTemplate);
	}
}
