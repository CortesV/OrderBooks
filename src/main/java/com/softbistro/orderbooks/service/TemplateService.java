package com.softbistro.orderbooks.service;

import java.io.IOException;
import java.util.List;

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
import com.github.messenger4j.send.templates.Template;
import com.softbistro.orderbooks.components.entity.Book;
import com.softbistro.orderbooks.components.entity.OrderCart;
import com.softbistro.orderbooks.handlers.CallBackHandler;
import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.WebResource;
import com.sun.jersey.api.client.config.ClientConfig;
import com.sun.jersey.api.client.config.DefaultClientConfig;
import com.sun.jersey.api.client.filter.GZIPContentEncodingFilter;

@Service
public class TemplateService {

	public Template sendListBooks(String keyword)
			throws MessengerApiException, MessengerIOException, IOException {
		
		List<Book> searchResults = readAll(keyword);
		
		OrderCart.searchBooks = searchResults;
		
		com.github.messenger4j.send.templates.ListTemplate.Element.ListBuilder builder = ListTemplate.newBuilder(TopElementStyle.LARGE).addElements();
		for(Book book : searchResults){
				builder = builder
						.addElement(book.getTitle())	
						.subtitle("Author " + book.getAuthors().get(0) + "\nISBN " + book.getIsbn())
						.imageUrl(book.getImageUrl())
						.toList();
		}
		
		return builder.done().build();
	}

	public Template showBook() throws MessengerApiException, MessengerIOException, IOException {
		return ReceiptTemplate
				.newBuilder("Stephane Crozatier", "12345678902", "USD", "Visa 2345")
				.orderUrl(
						"http://www.chegg.com/textbooks/biology-12th-edition-9780078024269-0078024269?trackid=0a17c4c9&strackid=3bac7b84&ii=1")
				.timestamp(1428444852L).addElements().addElement(OrderCart.chooseBook.getTitle() + " " + OrderCart.chooseBook.getIsbn(), 50F).subtitle("Rent $19.49")
				.quantity(2).currency("USD")
				.imageUrl("http://cs.cheggcdn.com/covers2/50310000/50318001_1484290068_Width288.jpg").toList().done()
				.addAddress("1 Hacker Way", "Menlo Park", "94025", "CA", "US").street2("Central Park").done()
				.addSummary(56.14F).subtotal(75.00F).shippingCost(4.95F).totalTax(6.19F).done().addAdjustments()
				.addAdjustment().name("New Customer Discount").amount(20.00F).toList().addAdjustment()
				.name("$10 Off Coupon").amount(10.00F).toList().done().build();
	}

	public Template showChooseBooks() throws MessengerApiException, MessengerIOException, IOException {

		return ReceiptTemplate
				.newBuilder("Stephane Crozatier", "12345678902", "USD", "Visa 2345")
				.orderUrl(
						"http://www.chegg.com/textbooks/biology-12th-edition-9780078024269-0078024269?trackid=0a17c4c9&strackid=3bac7b84&ii=1")
				.timestamp(1428444852L).addElements().addElement(OrderCart.chooseBook.getTitle(), 50F)
				.subtitle(OrderCart.choosePrice).quantity(2).currency("USD")
				.imageUrl("http://cs.cheggcdn.com/covers2/50310000/50318001_1484290068_Width288.jpg").toList().done()
				.addAddress("1 Hacker Way", "Menlo Park", "94025", "CA", "US").street2("Central Park").done()
				.addSummary(56.14F).subtotal(75.00F).shippingCost(4.95F).totalTax(6.19F).done().addAdjustments()
				.addAdjustment().name("New Customer Discount").amount(20.00F).toList().addAdjustment()
				.name("$10 Off Coupon").amount(10.00F).toList().done().build();
	}

	public List<QuickReply> sendQuickReplyListBooks() throws MessengerApiException, MessengerIOException {
		com.github.messenger4j.send.QuickReply.ListBuilder builder = QuickReply.newListBuilder();
		for (Book book : OrderCart.searchBooks) {
			builder = builder.addTextQuickReply(book.getTitle() + " " + book.getIsbn(), CallBackHandler.GOOD_ACTION).toList();
		}
		return builder.build();
	}

	public List<QuickReply> sendQuickReplyPrice() throws MessengerApiException, MessengerIOException {
		return QuickReply.newListBuilder()
				.addTextQuickReply("111", CallBackHandler.GOOD_ACTION_PRICE).toList()
				.addTextQuickReply("222", CallBackHandler.GOOD_ACTION_PRICE).toList()
				.addTextQuickReply("333", CallBackHandler.GOOD_ACTION_PRICE).toList()
				.addTextQuickReply("No, thank's", CallBackHandler.NOT_GOOD_ACTION).toList().build();
	}

	public void saveCheckedBook(String title) {
		for(Book book : OrderCart.searchBooks){
			if(title.equals(book.getTitle() + " " + book.getIsbn())){
				OrderCart.chooseBook = book;
			}
		}
	}
	
	public void saveCardBooks(String price) {
		Book checkedBook = OrderCart.chooseBook;
		OrderCart.booksInCard.add(checkedBook);
		OrderCart.chooseBook = checkedBook;
		OrderCart.choosePrice = price;
	}
	
	public List<Book> readAll(String keyword) throws JsonParseException, JsonMappingException, IOException {
		String jsonText = null;
		ClientConfig config = new DefaultClientConfig();
		Client client = Client.create(config);
		client.addFilter(new GZIPContentEncodingFilter(false));

		//TODO rewrite keyword
		WebResource wr = client.resource("http://demo2668900.mockable.io/catalog/" + keyword);
		ClientResponse response = null;
		response = wr.get(ClientResponse.class);
		jsonText = response.getEntity(String.class);
		
		ObjectMapper objectMapper = new ObjectMapper();

		return objectMapper.readValue(jsonText, TypeFactory.defaultInstance().constructCollectionType(List.class, Book.class));
	}

}
