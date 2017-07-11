package com.softbistro.orderbooks;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.github.messenger4j.exceptions.MessengerApiException;
import com.github.messenger4j.exceptions.MessengerIOException;
import com.github.messenger4j.send.QuickReply;
import com.github.messenger4j.send.templates.ListTemplate;
import com.github.messenger4j.send.templates.ListTemplate.TopElementStyle;
import com.github.messenger4j.send.templates.ReceiptTemplate;
import com.softbistro.orderbooks.components.entity.Book;
import com.softbistro.orderbooks.components.entity.CardBooks;

@Service
public class TemplateService {

	@Autowired
	private CallBackHandler callBackHandler;

	public void sendListBooks(String recipientId, String keyword)
			throws MessengerApiException, MessengerIOException, IOException {

		List<Book> searchResults = new ArrayList<>();
		searchResults = new ArrayList<>();

		List<String> authors = new ArrayList<>();
		authors.add("Author1");
		authors.add("Author2");
		authors.add("Author3");
		authors.add("Author4");

		Book searchResult = new Book("1", "Biology1", "akfdgdygaihfsd",
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
		searchResults.add(searchResult);
		
		Book.setSearchResults(searchResults);

		final ListTemplate genericTemplate2 = ListTemplate.newBuilder(TopElementStyle.LARGE).addElements()
				.addElement(searchResults.get(0).getTitle())
				.subtitle("Author " + authors.get(0) + "\n" + "ISBN " + searchResults.get(0).getIsbn())
				.imageUrl(searchResults.get(0).getImageUrl()).toList().addElement(searchResults.get(1).getTitle())
				.subtitle("Author " + authors.get(1) + "\n" + "ISBN " + searchResults.get(1).getIsbn())
				.imageUrl(searchResults.get(1).getImageUrl()).toList().addElement(searchResults.get(2).getTitle())
				.subtitle("Author " + authors.get(2) + "\n" + "ISBN " + searchResults.get(2).getIsbn())
				.imageUrl(searchResults.get(2).getImageUrl()).toList().addElement(searchResults.get(3).getTitle())
				.subtitle("Author " + authors.get(3) + "\n" + "ISBN " + searchResults.get(3).getIsbn())
				.imageUrl(searchResults.get(3).getImageUrl()).toList().done().build();

		callBackHandler.getSendClient().sendTemplate(recipientId, genericTemplate2);

	}

	public void showBook(String recipientId) throws MessengerApiException, MessengerIOException, IOException {

		/*final ReceiptTemplate genericTemplate3 = ReceiptTemplate
				.newBuilder("Stephane Crozatier", "12345678902", "USD", "Visa 2345")
				.orderUrl(
						"http://www.chegg.com/textbooks/biology-12th-edition-9780078024269-0078024269?trackid=0a17c4c9&strackid=3bac7b84&ii=1")
				.timestamp(1428444852L).addElements().addElement("Biology 12th edition", 50F).subtitle("Rent $19.49")
				.quantity(2).currency("USD")
				.imageUrl("http://cs.cheggcdn.com/covers2/50310000/50318001_1484290068_Width288.jpg").toList().done()
				.addAddress("1 Hacker Way", "Menlo Park", "94025", "CA", "US").street2("Central Park").done()
				.addSummary(56.14F).subtotal(75.00F).shippingCost(4.95F).totalTax(6.19F).done().addAdjustments()
				.addAdjustment().name("New Customer Discount").amount(20.00F).toList().addAdjustment()
				.name("$10 Off Coupon").amount(10.00F).toList().done().build();*/
		
		final ReceiptTemplate genericTemplate3 = ReceiptTemplate
				.newBuilder("Stephane Crozatier", "12345678902", "USD", "Visa 2345")
				.orderUrl(
						"http://www.chegg.com/textbooks/biology-12th-edition-9780078024269-0078024269?trackid=0a17c4c9&strackid=3bac7b84&ii=1")
				.timestamp(1428444852L).addElements().addElement("Biology 12th edition", 50F).subtitle("Rent $19.49")
				.quantity(2).currency("USD")
				.imageUrl("http://cs.cheggcdn.com/covers2/50310000/50318001_1484290068_Width288.jpg").toList().done()
				.addAddress("1 Hacker Way", "Menlo Park", "94025", "CA", "US").street2("Central Park").done()
				.addSummary(56.14F).subtotal(75.00F).shippingCost(4.95F).totalTax(6.19F).done().addAdjustments()
				.addAdjustment().name("New Customer Discount").amount(20.00F).toList().addAdjustment()
				.name("$10 Off Coupon").amount(10.00F).toList().done().build();

		callBackHandler.getSendClient().sendTemplate(recipientId, genericTemplate3);
	}
	
	public void showChooseBooks(String recipientId) throws MessengerApiException, MessengerIOException, IOException {

		/*final ReceiptTemplate genericTemplate3 = ReceiptTemplate
				.newBuilder("Stephane Crozatier", "12345678902", "USD", "Visa 2345")
				.orderUrl(
						"http://www.chegg.com/textbooks/biology-12th-edition-9780078024269-0078024269?trackid=0a17c4c9&strackid=3bac7b84&ii=1")
				.timestamp(1428444852L).addElements().addElement("Biology 12th edition", 50F).subtitle("Rent $19.49")
				.quantity(2).currency("USD")
				.imageUrl("http://cs.cheggcdn.com/covers2/50310000/50318001_1484290068_Width288.jpg").toList().done()
				.addAddress("1 Hacker Way", "Menlo Park", "94025", "CA", "US").street2("Central Park").done()
				.addSummary(56.14F).subtotal(75.00F).shippingCost(4.95F).totalTax(6.19F).done().addAdjustments()
				.addAdjustment().name("New Customer Discount").amount(20.00F).toList().addAdjustment()
				.name("$10 Off Coupon").amount(10.00F).toList().done().build();*/
		
		final ReceiptTemplate genericTemplate3 = ReceiptTemplate
				.newBuilder("Stephane Crozatier", "12345678902", "USD", "Visa 2345")
				.orderUrl(
						"http://www.chegg.com/textbooks/biology-12th-edition-9780078024269-0078024269?trackid=0a17c4c9&strackid=3bac7b84&ii=1")
				.timestamp(1428444852L).addElements().addElement(CardBooks.getBooksInCard().get(0).getTitle(), 50F).subtitle(CardBooks.getChoosePrice())
				.quantity(2).currency("USD")
				.imageUrl("http://cs.cheggcdn.com/covers2/50310000/50318001_1484290068_Width288.jpg").toList().done()
				.addAddress("1 Hacker Way", "Menlo Park", "94025", "CA", "US").street2("Central Park").done()
				.addSummary(56.14F).subtotal(75.00F).shippingCost(4.95F).totalTax(6.19F).done().addAdjustments()
				.addAdjustment().name("New Customer Discount").amount(20.00F).toList().addAdjustment()
				.name("$10 Off Coupon").amount(10.00F).toList().done().build();

		callBackHandler.getSendClient().sendTemplate(recipientId, genericTemplate3);
	}

	public void sendQuickReplyListBooks(String recipientId) throws MessengerApiException, MessengerIOException {
		final List<QuickReply> quickReplies = QuickReply.newListBuilder()
				.addTextQuickReply("Biology1", callBackHandler.getGoodAction()).toList()
				.addTextQuickReply("Biology2", callBackHandler.getGoodAction()).toList()
				.addTextQuickReply("Biology3", callBackHandler.getGoodAction()).toList()
				.addTextQuickReply("Biology4", callBackHandler.getGoodAction()).toList().build();
		callBackHandler.getSendClient().sendTextMessage(recipientId, "You can watch details each of books", quickReplies);
	}

	public void sendQuickReplyPrice(String recipientId) throws MessengerApiException, MessengerIOException {
		final List<QuickReply> quickReplies = QuickReply.newListBuilder()
				.addTextQuickReply("Price1", callBackHandler.getGoodActionPrice()).toList()
				.addTextQuickReply("Price2", callBackHandler.getGoodActionPrice()).toList()
				.addTextQuickReply("Price3", callBackHandler.getGoodActionPrice()).toList()
				.addTextQuickReply("Price4", callBackHandler.getGoodActionPrice()).toList()
				.addTextQuickReply("No, thank's", callBackHandler.getNotGoodAction()).toList().build();
		callBackHandler.getSendClient().sendTextMessage(recipientId, "Choose price of books", quickReplies);
	}

}
