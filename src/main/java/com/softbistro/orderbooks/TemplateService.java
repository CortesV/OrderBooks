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

				

		/*List<String> authors = new ArrayList<>();
		authors.add("Author1");
		authors.add("Author2");
		authors.add("Author3");
		authors.add("Author4");*/

		/*Book searchResult = new Book("1", "Biology1", "akfdgdygaihfsd",
				"http://cs.cheggcdn.com/covers2/50310000/50318001_1484290068_Width288.jpg", authors);
		searchResults.add(searchResult);

		searchResult = new Book("2", "Biology2", "11111213123123",
				"http://cs.cheggcdn.com/covers2/42040000/42044766_1388990605.jpg", authors);
		searchResults.add(searchResult);

		searchResult = new Book("3", "Biolog3", "456985746",
				"http://cs.cheggcdn.com/covers2/21660000/21660265_1467822671.jpg", authors);
		searchResults.add(searchResult);

		searchResult = new Book("4", "Biology4", "7987806",
				"http://cs.cheggcdn.com/covers2/20210000/20218127_1389004426.jpg", authors);
		searchResults.add(searchResult);*/

		
		/*
		ListBuilder builder = ListTemplate.newBuilder(TopElementStyle.LARGE).addElements();
		for(Book book : searchResults){
				builder.addElement(book.getTitle())
				.subtitle("Author " + authors.get(0) + "\n" + "ISBN " + book.getIsbn()).imageUrl(book.getImageUrl()).toList();
		}
		
		final ListTemplate genericTemplate2 = builder.done().build();*/
		
		
		
		List<Book> searchResults = new ArrayList<>();
		Book  book = new Book("id", "title", "isbn", "ean", "image", Arrays.asList("url"));
		searchResults.add(book);
//				templateController.getCatalog();
		
		CardBooks.searchResults = searchResults;
		
		final ListTemplate genericTemplate2 = ListTemplate.newBuilder(TopElementStyle.LARGE).addElements()
				.addElement(searchResults.get(0).getTitle())
				.subtitle("Author " + searchResults.get(0).getAuthors().get(0) + "\n" + "ISBN " + searchResults.get(0).getIsbn())
				.imageUrl(searchResults.get(0).getImageUrl()).toList().done().build();
		
		
		
		
		
		
		
		
		/*.addElement(searchResults.get(1).getTitle())
				.subtitle("Author " + searchResults.get(1).getAuthors().get(0) + "\n" + "ISBN " + searchResults.get(1).getIsbn())
				.imageUrl(searchResults.get(1).getImageUrl()).toList().addElement(searchResults.get(2).getTitle())
				.subtitle("Author " + searchResults.get(2).getAuthors().get(0) + "\n" + "ISBN " + searchResults.get(2).getIsbn())
				.imageUrl(searchResults.get(2).getImageUrl()).toList().addElement(searchResults.get(3).getTitle())
				.subtitle("Chegg search " + authors.get(3) + "\n" + "ISBN " + searchResults.get(3).getIsbn())
				.imageUrl(searchResults.get(3).getImageUrl()).toList()*/

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
		final List<QuickReply> quickReplies = QuickReply.newListBuilder()
				.addTextQuickReply(CardBooks.searchResults.get(0).getTitle(), callBackHandler.getGoodAction()).toList()
				.addTextQuickReply(CardBooks.searchResults.get(1).getTitle(), callBackHandler.getGoodAction()).toList()
				.addTextQuickReply(CardBooks.searchResults.get(2).getTitle(), callBackHandler.getGoodAction()).toList()
				.addTextQuickReply(CardBooks.searchResults.get(3).getTitle(), callBackHandler.getGoodAction()).toList().build();
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
