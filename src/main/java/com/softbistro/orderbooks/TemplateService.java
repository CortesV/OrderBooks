package com.softbistro.orderbooks;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.type.TypeFactory;
import com.github.messenger4j.exceptions.MessengerApiException;
import com.github.messenger4j.exceptions.MessengerIOException;
import com.github.messenger4j.send.QuickReply;
import com.github.messenger4j.send.templates.ListTemplate;
import com.github.messenger4j.send.templates.ListTemplate.Element.ListBuilder;
import com.github.messenger4j.send.templates.ListTemplate.TopElementStyle;
import com.github.messenger4j.send.templates.ReceiptTemplate;
import com.softbistro.orderbooks.components.entity.Book;
import com.softbistro.orderbooks.components.entity.CardBooks;
import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.WebResource;
import com.sun.jersey.api.client.config.ClientConfig;
import com.sun.jersey.api.client.config.DefaultClientConfig;
import com.sun.jersey.api.client.filter.GZIPContentEncodingFilter;

@Service
public class TemplateService {

	@Autowired
	private CallBackHandler callBackHandler;
	
	@Autowired
	private TemplateController templateController;

	public void sendListBooks(String recipientId, String keyword)
			throws MessengerApiException, MessengerIOException, IOException {
		
		List<Book> searchResults = templateController.getCatalog();
		ListBuilder builder = ListTemplate.newBuilder(TopElementStyle.LARGE).addElements();
		for(Book book : searchResults){
				builder.addElement(book.getTitle())
				.subtitle("Author " + book.getAuthors().get(0) + "\n" + "ISBN " + book.getIsbn()).imageUrl(book.getImageUrl()).toList();
		}
		
		final ListTemplate genericTemplate2 = builder.done().build();
		callBackHandler.getSendClient().sendTemplate(recipientId, genericTemplate2);

	}

	public void showBook(String recipientId) throws MessengerApiException, MessengerIOException, IOException {

		final ReceiptTemplate genericTemplate3 = ReceiptTemplate
				.newBuilder("Stephane Crozatier", "12345678902", "USD", "Visa 2345")
				.orderUrl(
						"http://www.chegg.com/textbooks/biology-12th-edition-9780078024269-0078024269?trackid=0a17c4c9&strackid=3bac7b84&ii=1")
				.timestamp(1428444852L).addElements().addElement(CardBooks.chooseBook.getTitle(), 50F).subtitle("Rent $19.49")
				.quantity(2).currency("USD")
				.imageUrl("http://cs.cheggcdn.com/covers2/50310000/50318001_1484290068_Width288.jpg").toList().done()
				.addAddress("1 Hacker Way", "Menlo Park", "94025", "CA", "US").street2("Central Park").done()
				.addSummary(56.14F).subtotal(75.00F).shippingCost(4.95F).totalTax(6.19F).done().addAdjustments()
				.addAdjustment().name("New Customer Discount").amount(20.00F).toList().addAdjustment()
				.name("$10 Off Coupon").amount(10.00F).toList().done().build();

		callBackHandler.getSendClient().sendTemplate(recipientId, genericTemplate3);
	}

	public void showChooseBooks(String recipientId) throws MessengerApiException, MessengerIOException, IOException {

		final ReceiptTemplate genericTemplate3 = ReceiptTemplate
				.newBuilder("Stephane Crozatier", "12345678902", "USD", "Visa 2345")
				.orderUrl(
						"http://www.chegg.com/textbooks/biology-12th-edition-9780078024269-0078024269?trackid=0a17c4c9&strackid=3bac7b84&ii=1")
				.timestamp(1428444852L).addElements().addElement(CardBooks.chooseBook.getTitle(), 50F)
				.subtitle(CardBooks.choosePrice).quantity(2).currency("USD")
				.imageUrl("http://cs.cheggcdn.com/covers2/50310000/50318001_1484290068_Width288.jpg").toList().done()
				.addAddress("1 Hacker Way", "Menlo Park", "94025", "CA", "US").street2("Central Park").done()
				.addSummary(56.14F).subtotal(75.00F).shippingCost(4.95F).totalTax(6.19F).done().addAdjustments()
				.addAdjustment().name("New Customer Discount").amount(20.00F).toList().addAdjustment()
				.name("$10 Off Coupon").amount(10.00F).toList().done().build();

		callBackHandler.getSendClient().sendTemplate(recipientId, genericTemplate3);
	}

	public void sendQuickReplyListBooks(String recipientId) throws MessengerApiException, MessengerIOException {
		com.github.messenger4j.send.QuickReply.ListBuilder builder = QuickReply.newListBuilder();
		for (Book book : CardBooks.searchResults) {
			builder.addTextQuickReply(book.getTitle(), callBackHandler.getGoodAction()).toList();
		}
		final List<QuickReply> quickReplies = builder.build();
		callBackHandler.getSendClient().sendTextMessage(recipientId, "You can watch details each of books",
				quickReplies);
	}

	public void sendQuickReplyPrice(String recipientId) throws MessengerApiException, MessengerIOException {
		final List<QuickReply> quickReplies = QuickReply.newListBuilder()
				.addTextQuickReply("111", callBackHandler.getGoodActionPrice()).toList()
				.addTextQuickReply("222", callBackHandler.getGoodActionPrice()).toList()
				.addTextQuickReply("333", callBackHandler.getGoodActionPrice()).toList()
				.addTextQuickReply("444", callBackHandler.getGoodActionPrice()).toList()
				.addTextQuickReply("No, thank's", callBackHandler.getNotGoodAction()).toList().build();
		callBackHandler.getSendClient().sendTextMessage(recipientId, "Choose price of books", quickReplies);
	}

	public void saveCheckedBook(String title) {
		Book checkedBook = null;
		String first = CardBooks.searchResults.get(0).getTitle();
		String second = CardBooks.searchResults.get(1).getTitle();
		String third = CardBooks.searchResults.get(2).getTitle();
		String fourth = CardBooks.searchResults.get(3).getTitle();
		if (title.equals(first)) {
			checkedBook = CardBooks.searchResults.get(0);
		} else if (title.equals(second)) {
			checkedBook = CardBooks.searchResults.get(1);
		} else if (title.equals(third)) {
			checkedBook = CardBooks.searchResults.get(2);
		} else if (title.equals(fourth)) {
			checkedBook = CardBooks.searchResults.get(3);
		}
		CardBooks.chooseBook = checkedBook;
	}
	
	public void saveCardBooks(String price) {
		Book checkedBook = CardBooks.chooseBook;
		CardBooks.booksInCard.add(checkedBook);
		CardBooks.chooseBook = checkedBook;
		CardBooks.choosePrice = price;
	}
	
	public List<Book> readAll(String keyword) throws JsonParseException, JsonMappingException, IOException {
		String jsonText = null;
		ClientConfig config = new DefaultClientConfig();
		Client client = Client.create(config);
		client.addFilter(new GZIPContentEncodingFilter(false));

		WebResource wr = client.resource("http://80.91.191.79:19200/catalog/" + keyword);
		ClientResponse response = null;
		response = wr.get(ClientResponse.class);
		jsonText = response.getEntity(String.class);
		
		ObjectMapper objectMapper = new ObjectMapper();

		return objectMapper.readValue(jsonText, TypeFactory.defaultInstance().constructCollectionType(List.class, Book.class));
	}

}
