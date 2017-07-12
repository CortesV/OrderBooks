package com.softbistro.orderbooks.service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.ws.rs.core.MediaType;

import org.springframework.stereotype.Service;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.type.TypeFactory;
import com.github.messenger4j.exceptions.MessengerApiException;
import com.github.messenger4j.exceptions.MessengerIOException;
import com.github.messenger4j.send.QuickReply;
import com.github.messenger4j.send.templates.ListTemplate;
import com.github.messenger4j.send.templates.ListTemplate.Builder;
import com.github.messenger4j.send.templates.ListTemplate.TopElementStyle;
import com.github.messenger4j.send.templates.ReceiptTemplate;
import com.github.messenger4j.send.templates.ReceiptTemplate.Element.ListBuilder;
import com.github.messenger4j.send.templates.Template;
import com.softbistro.orderbooks.components.entity.Book;
import com.softbistro.orderbooks.components.entity.BookForOrder;
import com.softbistro.orderbooks.components.entity.OrderCart;
import com.softbistro.orderbooks.components.entity.PriceItem;
import com.softbistro.orderbooks.components.entity.CatalogItem;
import com.softbistro.orderbooks.handlers.CallBackHandler;
import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.WebResource;
import com.sun.jersey.api.client.config.ClientConfig;
import com.sun.jersey.api.client.config.DefaultClientConfig;
import com.sun.jersey.api.client.filter.GZIPContentEncodingFilter;

@Service
public class TemplateService {

	@PostConstruct
	public void setup() {
		OrderCart.booksInCard = new ArrayList<>();
	}

	public Template sendListBooks(String keyword) throws MessengerApiException, MessengerIOException, IOException {

		List<Book> searchResults = readAll(keyword);

		OrderCart.searchBooks = searchResults;

		com.github.messenger4j.send.templates.ListTemplate.Element.ListBuilder builder = ListTemplate
				.newBuilder(TopElementStyle.LARGE).addElements();
		for (Book book : searchResults) {
			builder = builder.addElement(book.getTitle())
					.subtitle("Author " + book.getAuthors().get(0) + "\nISBN " + book.getIsbn())
					.imageUrl(book.getImageUrl()).toList();
		}

		return builder.done().build();
	}

	public Template showBook() throws MessengerApiException, MessengerIOException, IOException {
		return ReceiptTemplate.newBuilder("Customer", "12345678902", "USD", "Credit card")
				.orderUrl(OrderCart.chooseBook.getImageUrl()).addElements()
				.addElement(OrderCart.chooseBook.getTitle() + " " + OrderCart.chooseBook.getIsbn(), 0F)
				.subtitle("Author " + OrderCart.chooseBook.getAuthors().get(0)).quantity(1).currency("USD")
				.imageUrl(OrderCart.chooseBook.getImageUrl()).toList().done().addSummary(0F).done().build();
	}

	public Template showChoosedBook() throws MessengerApiException, MessengerIOException, IOException {
		return ReceiptTemplate.newBuilder("Customer", "12345678902", "USD", "Credit card")
				.orderUrl(OrderCart.chooseBook.getImageUrl()).addElements()
				.addElement(OrderCart.chooseBook.getTitle() + " " + OrderCart.chooseBook.getIsbn(),
						Float.valueOf(OrderCart.chooseBook.getPrice()))
				.subtitle("Author " + OrderCart.chooseBook.getAuthors().get(0)).quantity(1).currency("USD")
				.imageUrl(OrderCart.chooseBook.getImageUrl()).toList().done()
				.addSummary(Float.valueOf(OrderCart.chooseBook.getPrice())).done().build();
	}

	public Template showChoosedBooks() throws MessengerApiException, MessengerIOException, IOException {

		ListBuilder builder = ReceiptTemplate.newBuilder("Stephane Crozatier", "12345678902", "USD", "Visa 2345")
				.orderUrl(
						"http://www.chegg.com/textbooks/biology-12th-edition-9780078024269-0078024269?trackid=0a17c4c9&strackid=3bac7b84&ii=1")
				.timestamp(1428444852L).addElements();
		Float summary = 0F;
		for (Book book : OrderCart.booksInCard) {
			builder.addElement(book.getTitle() + " " + book.getIsbn(), Float.valueOf(book.getPrice())).quantity(1)
					.currency("USD").imageUrl(book.getImageUrl()).toList();
			summary += Float.valueOf(book.getPrice());
		}
		return builder.done().addAddress("1 Hacker Way", "Menlo Park", "94025", "CA", "US").street2("Central Park")
				.done().addSummary(summary).subtotal(75.00F).shippingCost(4.95F).totalTax(6.19F).done().addAdjustments()
				.addAdjustment().name("New Customer Discount").amount(20.00F).toList().addAdjustment()
				.name("$10 Off Coupon").amount(10.00F).toList().done().build();
	}

	public Template showOrderedBooks() throws MessengerApiException, MessengerIOException, IOException {

		ListBuilder builder = ReceiptTemplate.newBuilder("Stephane Crozatier", "12345678902", "USD", "Visa 2345")
				.orderUrl(
						"http://www.chegg.com/textbooks/biology-12th-edition-9780078024269-0078024269?trackid=0a17c4c9&strackid=3bac7b84&ii=1")
				.timestamp(1428444852L).addElements();
		for (Book book : OrderCart.booksInCard) {
			builder.addElement(book.getTitle() + " " + book.getIsbn(), 50F).subtitle(OrderCart.choosePrice).quantity(2)
					.currency("USD").imageUrl(book.getImageUrl()).toList();
		}
		return builder.done().addAddress("1 Hacker Way", "Menlo Park", "94025", "CA", "US").street2("Central Park")
				.done().addSummary(56.14F).subtotal(75.00F).shippingCost(4.95F).totalTax(6.19F).done().addAdjustments()
				.addAdjustment().name("New Customer Discount").amount(20.00F).toList().addAdjustment()
				.name("$10 Off Coupon").amount(10.00F).toList().done().build();
	}

	public List<QuickReply> sendQuickReplyListBooks() throws MessengerApiException, MessengerIOException {
		com.github.messenger4j.send.QuickReply.ListBuilder builder = QuickReply.newListBuilder();
		for (Book book : OrderCart.searchBooks) {
			builder = builder.addTextQuickReply(book.getTitle() + " " + book.getIsbn(), CallBackHandler.GOOD_ACTION)
					.toList();
		}
		return builder.build();
	}

	public List<QuickReply> sendQuickReplyPrice()
			throws MessengerApiException, MessengerIOException, JsonParseException, JsonMappingException, IOException {
		OrderCart.prices = getPrices(OrderCart.chooseBook.getId()).getPrices();
		com.github.messenger4j.send.QuickReply.ListBuilder builder = QuickReply.newListBuilder();
		for (PriceItem price : OrderCart.prices) {
			builder = builder.addTextQuickReply(price.getPrice().toString(), CallBackHandler.GOOD_ACTION_PRICE)
					.toList();
		}
		return builder.addTextQuickReply("No, thank's", CallBackHandler.NOT_GOOD_ACTION).toList().build();
	}

	public List<QuickReply> sendQuickReplyUser() throws MessengerApiException, MessengerIOException {
		return QuickReply.newListBuilder().addTextQuickReply("Checkout", CallBackHandler.GOOD_ACTION_CHECKOUT).toList()
				.addTextQuickReply("No, thank's", CallBackHandler.NOT_GOOD_ACTION).toList().build();
	}

	public List<QuickReply> sendQuickReplyConfirmBuy() throws MessengerApiException, MessengerIOException {
		return QuickReply.newListBuilder().addTextQuickReply("Confirm buy", CallBackHandler.GOOD_ACTION_CONFIRM_BUY)
				.toList().addTextQuickReply("No, thank's", CallBackHandler.NOT_GOOD_ACTION).toList().build();
	}

	public List<QuickReply> sendQuickReplyBuy() throws MessengerApiException, MessengerIOException {
		return QuickReply.newListBuilder().addTextQuickReply("Buy books", CallBackHandler.GOOD_ACTION_BUY).toList()
				.addTextQuickReply("No, thank's", CallBackHandler.NOT_GOOD_ACTION).toList().build();
	}

	public List<QuickReply> sendQuickReplyBuyEnd() throws MessengerApiException, MessengerIOException {
		return QuickReply.newListBuilder().addTextQuickReply("", CallBackHandler.GOOD_ACTION_BUY_END).toList()
				.addTextQuickReply("No, thank's", CallBackHandler.NOT_GOOD_ACTION).toList().build();
	}

	public List<QuickReply> sendQuickReplyUserInfo() throws MessengerApiException, MessengerIOException {
		return QuickReply.newListBuilder()
				.addTextQuickReply("Custommer information", CallBackHandler.GOOD_ACTION_USER_INFO).toList()
				.addTextQuickReply("No, thank's", CallBackHandler.NOT_GOOD_ACTION).toList().build();
	}

	public void saveCheckedBook(String title) {
		for (Book book : OrderCart.searchBooks) {
			if (title.equals(book.getTitle() + " " + book.getIsbn())) {
				OrderCart.chooseBook = book;
			}
		}
	}

	public void savePriceCheckedBook(String title) {
		OrderCart.chooseBook.setPrice(title);
	}

	public void resetStaticData() {
		OrderCart.chooseBook = null;
		OrderCart.choosePrice = null;
		OrderCart.booksInCard = new ArrayList<>();
	}

	public void checkoutBook() throws JsonProcessingException {
		//OrderCart.booksInCard.add(OrderCart.chooseBook);

		BookForOrder bookForOrder = null;
		for (PriceItem price : OrderCart.prices) {
			if (price.getPrice().equals(Double.valueOf(OrderCart.chooseBook.getPrice()))) {
				bookForOrder = new BookForOrder(OrderCart.chooseBook.getId(), price.getLogId());
			}
		}
		ObjectMapper mapper = new ObjectMapper();
		String jsonText = mapper.writeValueAsString(bookForOrder);
		Client client = Client.create();
		WebResource webResource = client.resource("http://80.91.191.79:19200/createOrder");
		ClientResponse response = webResource.accept(MediaType.APPLICATION_JSON).type(MediaType.APPLICATION_JSON)
				.post(ClientResponse.class, jsonText);
		OrderCart.orderId = response.getEntity(String.class);
	}

	public List<Book> readAll(String keyword) throws JsonParseException, JsonMappingException, IOException {
		String jsonText = null;
		ClientConfig config = new DefaultClientConfig();
		Client client = Client.create(config);
		client.addFilter(new GZIPContentEncodingFilter(false));

		// TODO rewrite keyword
		WebResource wr = client.resource("http://80.91.191.79:19200/catalog/" + keyword);
		ClientResponse response = null;
		response = wr.get(ClientResponse.class);
		jsonText = response.getEntity(String.class);

		ObjectMapper objectMapper = new ObjectMapper();

		return objectMapper.readValue(jsonText,
				TypeFactory.defaultInstance().constructCollectionType(List.class, Book.class));
	}

	public CatalogItem getPrices(String id) throws JsonParseException, JsonMappingException, IOException {
		String jsonText = null;
		ClientConfig config = new DefaultClientConfig();
		Client client = Client.create(config);
		client.addFilter(new GZIPContentEncodingFilter(false));

		// TODO rewrite keyword
		WebResource wr = client.resource("http://80.91.191.79:19200/prices/" + id);
		ClientResponse response = null;
		response = wr.get(ClientResponse.class);
		jsonText = response.getEntity(String.class);

		ObjectMapper objectMapper = new ObjectMapper();

		return objectMapper.readValue(jsonText, TypeFactory.defaultInstance().constructType(CatalogItem.class));
	}

}
