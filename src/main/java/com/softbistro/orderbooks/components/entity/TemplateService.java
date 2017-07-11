package com.softbistro.orderbooks.components.entity;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.github.messenger4j.exceptions.MessengerApiException;
import com.github.messenger4j.exceptions.MessengerIOException;
import com.github.messenger4j.send.QuickReply;
import com.github.messenger4j.send.templates.ListTemplate;
import com.github.messenger4j.send.templates.ReceiptTemplate;
import com.softbistro.orderbooks.CallBackHandler;
import com.github.messenger4j.send.templates.ListTemplate.TopElementStyle;

@Service
public class TemplateService {

	@Autowired
	private CallBackHandler callBackHandler;

	public void sendListBooks(String recipientId, String keyword)
			throws MessengerApiException, MessengerIOException, IOException {

		List<SearchResult> searchResults = new ArrayList<>();
		searchResults = new ArrayList<>();
		SearchResult searchResult = new SearchResult("Biology",
				"http://www.chegg.com/textbooks/biology-12th-edition-9780078024269-0078024269?trackid=1f854400&strackid=4a41bf08&ii=1",
				"12th edition", "$19.49");
		searchResults.add(searchResult);
		searchResult = new SearchResult("Biology",
				"http://www.chegg.com/textbooks/biology-12th-edition-9780078024269-0078024269?trackid=1f854400&strackid=4a41bf08&ii=1",
				"12th edition", "$19.49");
		searchResults.add(searchResult);
		searchResult = new SearchResult("Biology",
				"http://www.chegg.com/textbooks/biology-12th-edition-9780078024269-0078024269?trackid=1f854400&strackid=4a41bf08&ii=1",
				"12th edition", "$19.49");
		searchResults.add(searchResult);
		searchResult = new SearchResult("Biology",
				"http://www.chegg.com/textbooks/biology-12th-edition-9780078024269-0078024269?trackid=1f854400&strackid=4a41bf08&ii=1",
				"12th edition", "$19.49");
		searchResults.add(searchResult);
		searchResult = new SearchResult("Biology",
				"http://www.chegg.com/textbooks/biology-12th-edition-9780078024269-0078024269?trackid=1f854400&strackid=4a41bf08&ii=1",
				"12th edition", "$19.49");
		searchResults.add(searchResult);
		searchResult = new SearchResult("Biology",
				"http://www.chegg.com/textbooks/biology-12th-edition-9780078024269-0078024269?trackid=1f854400&strackid=4a41bf08&ii=1",
				"12th edition", "$19.49");
		searchResults.add(searchResult);
		searchResult = new SearchResult("Biology",
				"http://www.chegg.com/textbooks/biology-12th-edition-9780078024269-0078024269?trackid=1f854400&strackid=4a41bf08&ii=1",
				"12th edition", "$19.49");
		searchResults.add(searchResult);
		searchResult = new SearchResult("Biology",
				"http://www.chegg.com/textbooks/biology-12th-edition-9780078024269-0078024269?trackid=1f854400&strackid=4a41bf08&ii=1",
				"12th edition", "$19.49");
		searchResults.add(searchResult);

		final ListTemplate genericTemplate2 = ListTemplate.newBuilder(TopElementStyle.LARGE).addElements()
				.addElement("Biology 12th edition").subtitle("Rent $19.49")
				.imageUrl("http://cs.cheggcdn.com/covers2/50310000/50318001_1484290068_Width288.jpg").toList()
				.addElement("Biology 12th edition").subtitle("Rent $19.49")
				.imageUrl("http://cs.cheggcdn.com/covers2/50310000/50318001_1484290068_Width288.jpg").toList()
				.addElement("Biology 12th edition").subtitle("Rent $19.49")
				.imageUrl("http://cs.cheggcdn.com/covers2/50310000/50318001_1484290068_Width288.jpg").toList()
				.addElement("Biology 12th edition").subtitle("Rent $19.49")
				.imageUrl("http://cs.cheggcdn.com/covers2/50310000/50318001_1484290068_Width288.jpg").toList().done()
				.build();

		callBackHandler.getSendClient().sendTemplate(recipientId, genericTemplate2);

	}

	public void showBook(String recipientId) throws MessengerApiException, MessengerIOException, IOException {
		final ReceiptTemplate genericTemplate3 = ReceiptTemplate.newBuilder("Stephane Crozatier", "", "USD", "")
				.orderUrl(
						"http://www.chegg.com/textbooks/biology-12th-edition-9780078024269-0078024269?trackid=0a17c4c9&strackid=3bac7b84&ii=1")
				.timestamp(1428444852L).addElements().addElement("Biology 12th edition", 50F).subtitle("Rent $19.49")
				.currency("USD").imageUrl("http://cs.cheggcdn.com/covers2/50310000/50318001_1484290068_Width288.jpg")
				.toList().done().addSummary(56.14F).subtotal(75.00F).shippingCost(4.95F).totalTax(6.19F).done()
				.addAdjustments().addAdjustment().name("New Customer Discount").amount(20.00F).toList().addAdjustment()
				.name("$10 Off Coupon").amount(10.00F).toList().done().build();

		callBackHandler.getSendClient().sendTemplate(recipientId, genericTemplate3);
	}

	public void sendQuickReply(String recipientId) throws MessengerApiException, MessengerIOException {
		final List<QuickReply> quickReplies = QuickReply.newListBuilder()
				.addTextQuickReply("Biology 12th edition", callBackHandler.getGoodAction()).toList()
				.addTextQuickReply("Biology 12th edition", callBackHandler.getGoodAction()).toList()
				.addTextQuickReply("Biology 12th edition", callBackHandler.getGoodAction()).toList()
				.addTextQuickReply("Biology 12th edition", callBackHandler.getGoodAction()).toList().build();
		callBackHandler.getSendClient().sendTextMessage(recipientId, "View each book", quickReplies);
	}
}
