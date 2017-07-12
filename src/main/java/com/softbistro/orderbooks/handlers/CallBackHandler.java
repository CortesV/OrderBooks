package com.softbistro.orderbooks.handlers;

import java.io.IOException;
import java.util.Date;
import java.util.List;

import org.json.JSONException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.github.messenger4j.MessengerPlatform;
import com.github.messenger4j.exceptions.MessengerApiException;
import com.github.messenger4j.exceptions.MessengerIOException;
import com.github.messenger4j.exceptions.MessengerVerificationException;
import com.github.messenger4j.receive.MessengerReceiveClient;
import com.github.messenger4j.receive.events.AccountLinkingEvent;
import com.github.messenger4j.receive.events.AttachmentMessageEvent.Attachment;
import com.github.messenger4j.receive.handlers.AccountLinkingEventHandler;
import com.github.messenger4j.receive.handlers.AttachmentMessageEventHandler;
import com.github.messenger4j.receive.handlers.EchoMessageEventHandler;
import com.github.messenger4j.receive.handlers.FallbackEventHandler;
import com.github.messenger4j.receive.handlers.MessageDeliveredEventHandler;
import com.github.messenger4j.receive.handlers.MessageReadEventHandler;
import com.github.messenger4j.receive.handlers.OptInEventHandler;
import com.github.messenger4j.receive.handlers.PostbackEventHandler;
import com.github.messenger4j.receive.handlers.QuickReplyMessageEventHandler;
import com.github.messenger4j.receive.handlers.TextMessageEventHandler;
import com.github.messenger4j.send.MessengerSendClient;
import com.github.messenger4j.send.NotificationType;
import com.github.messenger4j.send.QuickReply;
import com.github.messenger4j.send.Recipient;
import com.github.messenger4j.send.SenderAction;
import com.github.messenger4j.send.templates.Template;
import com.softbistro.orderbooks.components.entity.Book;
import com.softbistro.orderbooks.components.entity.OrderCart;
import com.softbistro.orderbooks.controllers.TemplateController;
import com.softbistro.orderbooks.service.TemplateService;
import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientHandlerException;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.UniformInterfaceException;
import com.sun.jersey.api.client.WebResource;

@RestController
@RequestMapping("/callback")
public class CallBackHandler {

	private static final Logger logger = LoggerFactory.getLogger(CallBackHandler.class);

	public static final String GOOD_ACTION = "DEVELOPER_DEFINED_PAYLOAD_FOR_GOOD_ACTION";
	public static final String GOOD_ACTION_PRICE = "DEVELOPER_DEFINED_PAYLOAD_FOR_GOOD_ACTION_PRICE";
	public static final String GOOD_ACTION_CHECKOUT = "DEVELOPER_DEFINED_PAYLOAD_FOR_GOOD_ACTION_CHECKOUT";
	public static final String GOOD_ACTION_CONFIRM_BUY = "DEVELOPER_DEFINED_PAYLOAD_FOR_GOOD_ACTION_CONFIRM_BUY";
	public static final String GOOD_ACTION_BUY = "DEVELOPER_DEFINED_PAYLOAD_FOR_GOOD_ACTION_BUY";
	public static final String GOOD_ACTION_BUY_END = "DEVELOPER_DEFINED_PAYLOAD_FOR_GOOD_ACTION_BUY_END";
	public static final String GOOD_ACTION_INFO = "DEVELOPER_DEFINED_PAYLOAD_FOR_GOOD_ACTION_INFO";
	public static final String NOT_GOOD_ACTION = "DEVELOPER_DEFINED_PAYLOAD_FOR_NOT_GOOD_ACTION";

	private final MessengerReceiveClient receiveClient;
	private final MessengerSendClient sendClient;

	@Autowired
	private TemplateService templateService;

	@Autowired
	private TemplateController templateController;

 	private AttachmentMessageEventHandler newAttachmentMessageEventHandler() {
		return event -> {
			logger.debug("Received AttachmentMessageEvent: {}", event);

			final String messageId = event.getMid();
			final List<Attachment> attachment = event.getAttachments();
			final String senderId = event.getSender().getId();
			final Date timestamp = event.getTimestamp();

			logger.info("Received attachment '{}' with type '{}' from user '{}' at '{}'", messageId,
					attachment.get(0).getType().toString(), senderId, timestamp);
			try {
				switch (attachment.get(0).getType().toString()) {
				case "AUDIO":
					sendTextMessage(senderId, "audiofile was accepted");

					Client client = Client.create();
					WebResource webResource = client.resource("http://80.91.191.79:19099/");

					String voice = attachment.get(0).getPayload().asBinaryPayload().getUrl();

					ClientResponse response = webResource.type("application/json").header("url", voice)
							.get(ClientResponse.class);

					String parsedVoice = response.getEntity(String.class);

					sendAction(senderId, SenderAction.MARK_SEEN);
					sendAction(senderId, SenderAction.TYPING_ON);
					sendTemplate(senderId, templateService.sendListBooks(parsedVoice));
					sendQuickReply(senderId, "You can watch details each of books",
							templateService.sendQuickReplyListBooks());
					sendAction(senderId, SenderAction.TYPING_OFF);
					break;

				default:
					sendTextMessage(senderId, "please send audio");
				}

			} catch (MessengerApiException | MessengerIOException e) {
				handleSendException(e);
			} catch (IOException e) {
				handleIOException(e);
			}
		};

	}
 	
	private TextMessageEventHandler newTextMessageEventHandler() throws MessengerIOException, IOException {
		return event -> {
			logger.debug("Received TextMessageEvent: {}", event);

			final String messageId = event.getMid();
			final String messageText = event.getText();
			final String senderId = event.getSender().getId();
			final Date timestamp = event.getTimestamp();

			logger.info("Received message '{}' with text '{}' from user '{}' at '{}'", messageId, messageText, senderId,
					timestamp);
			try {
				switch (messageText.toLowerCase()) {

				case "yo":
					sendTextMessage(senderId, "Hello, What I can do for you ? Type the word you're looking for");
					break;

				case "y":
					List<Book> searchResults = OrderCart.booksInCard;
					sendTextMessage(senderId,
							searchResults.get(0).getIsbn() + " " + searchResults.get(1).getIsbn());
					break;

				default:
					sendAction(senderId, SenderAction.MARK_SEEN);
					sendAction(senderId, SenderAction.TYPING_ON);
					sendTemplate(senderId, templateService.sendListBooks(messageText));
					sendQuickReply(senderId, "You can watch details each of books",
							templateService.sendQuickReplyListBooks());
					sendAction(senderId, SenderAction.TYPING_OFF);
				}
			} catch (MessengerApiException | MessengerIOException e) {
				handleSendException(e);
			} catch (IOException e) {
				handleIOException(e);
			}
		};
	}

	private QuickReplyMessageEventHandler newQuickReplyMessageEventHandler() {
		return event -> {
			logger.debug("Received QuickReplyMessageEvent: {}", event);

			final String senderId = event.getSender().getId();
			final String messageId = event.getMid();
			final String quickReplyPayload = event.getQuickReply().getPayload();

			logger.info("Received quick reply for message '{}' with payload '{}'", messageId, quickReplyPayload);

			try {
				if (quickReplyPayload.equals(GOOD_ACTION)) {
					templateService.saveCheckedBook(event.getText());
					sendTemplate(senderId, templateService.showBook());
					sendQuickReply(senderId, "Choose price of books", templateService.sendQuickReplyPrice());
				}
				if (quickReplyPayload.equals(GOOD_ACTION_PRICE)) {
					templateService.savePriceCheckedBook(event.getText());
					sendTemplate(senderId, templateService.showChoosedBook());
					sendQuickReply(senderId, "Checkout", templateService.sendQuickReplyUser());
				}
				if (quickReplyPayload.equals(GOOD_ACTION_CHECKOUT)) {
					templateService.checkoutBook();
					sendTextMessage(senderId,"Checkout this book done");
					//sendTextMessage(senderId,OrderCart.orderKey);
					//sendTextMessage(senderId,OrderCart.booksInCard.get(0).getTitle());
					//sendTemplate(senderId, templateService.showOrderedBooks());
					//sendQuickReply(senderId, "Continue order", templateService.sendQuickReplyUserInfo());
					sendQuickReply(senderId, "Shipping", templateService.sendQuickReplyUserInfo());
				}
				if (quickReplyPayload.equals(GOOD_ACTION_INFO)) {
					sendTextMessage(senderId,"USER_INFO_HARD_CODING");
					sendQuickReply(senderId, "Buy", templateService.sendQuickReplyConfirmBuy());
				}
				if (quickReplyPayload.equals(GOOD_ACTION_CONFIRM_BUY)) {
					sendTemplate(senderId, templateService.showOrderedBooks());
					sendQuickReply(senderId, "Buy", templateService.sendQuickReplyBuy());
				}
				if (quickReplyPayload.equals(GOOD_ACTION_BUY)) {
					templateService.resetStaticData();
					sendGifMessage(senderId, "https://media.giphy.com/media/3oz8xPxTUeebQ8pL1e/giphy.gif");
					sendTextMessage(senderId, "Let's try another one :D!");
				}
				

			} catch (MessengerApiException e) {
				handleSendException(e);
			} catch (MessengerIOException e) {
				handleIOException(e);
			} catch (IOException e) {
				handleIOException(e);
			} catch (ClientHandlerException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (UniformInterfaceException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		};
	}

	private void sendGifMessage(String recipientId, String gif) throws MessengerApiException, MessengerIOException {
		this.sendClient.sendImageAttachment(recipientId, gif);
	}

	private void sendQuickReply(String recipientId, String message, List<QuickReply> quickReplies)
			throws MessengerApiException, MessengerIOException {
		this.sendClient.sendTextMessage(recipientId, message, quickReplies);
	}

	private void sendAction(String recipientId, SenderAction action)
			throws MessengerApiException, MessengerIOException {
		this.sendClient.sendSenderAction(recipientId, action);
	}

	private void sendTemplate(String recipientId, Template template)
			throws MessengerApiException, MessengerIOException {
		this.sendClient.sendTemplate(recipientId, template);
	}

	/**
	 * Constructs the {@code CallBackHandler} and initializes the
	 * {@code MessengerReceiveClient}.
	 *
	 * @param appSecret
	 *            the {@code Application Secret}
	 * @param verifyToken
	 *            the {@code Verification Token} that has been provided by you
	 *            during the setup of the {@code
	 *                    Webhook}
	 * @param sendClient
	 *            the initialized {@code MessengerSendClient}
	 * @throws IOException
	 * @throws MessengerIOException
	 */
	@Autowired
	public CallBackHandler(@Value("${messenger4j.appSecret}") final String appSecret,
			@Value("${messenger4j.verifyToken}") final String verifyToken, final MessengerSendClient sendClient)
			throws MessengerIOException, IOException {

		logger.debug("Initializing MessengerReceiveClient - appSecret: {} | verifyToken: {}", appSecret, verifyToken);
		this.receiveClient = MessengerPlatform.newReceiveClientBuilder(appSecret, verifyToken)
				.onTextMessageEvent(newTextMessageEventHandler())
				.onAttachmentMessageEvent(newAttachmentMessageEventHandler())
				.onQuickReplyMessageEvent(newQuickReplyMessageEventHandler()).onPostbackEvent(newPostbackEventHandler())
				.onAccountLinkingEvent(newAccountLinkingEventHandler()).onOptInEvent(newOptInEventHandler())
				.onEchoMessageEvent(newEchoMessageEventHandler())
				.onMessageDeliveredEvent(newMessageDeliveredEventHandler())
				.onMessageReadEvent(newMessageReadEventHandler()).fallbackEventHandler(newFallbackEventHandler())
				.build();
		this.sendClient = sendClient;
	}

	/**
	 * Webhook verification endpoint.
	 *
	 * The passed verification token (as query parameter) must match the
	 * configured verification token. In case this is true, the passed challenge
	 * string must be returned by this endpoint.
	 */
	@RequestMapping(method = RequestMethod.GET)
	public ResponseEntity<String> verifyWebhook(@RequestParam("hub.mode") final String mode,
			@RequestParam("hub.verify_token") final String verifyToken,
			@RequestParam("hub.challenge") final String challenge) {

		logger.debug("Received Webhook verification request - mode: {} | verifyToken: {} | challenge: {}", mode,
				verifyToken, challenge);
		try {
			return ResponseEntity.ok(this.receiveClient.verifyWebhook(mode, verifyToken, challenge));
		} catch (MessengerVerificationException e) {
			logger.warn("Webhook verification failed: {}", e.getMessage());
			return ResponseEntity.status(HttpStatus.FORBIDDEN).body(e.getMessage());
		}
	}

	/**
	 * Callback endpoint responsible for processing the inbound messages and
	 * events.
	 */
	@RequestMapping(method = RequestMethod.POST)
	public ResponseEntity<Void> handleCallback(@RequestBody final String payload,
			@RequestHeader("X-Hub-Signature") final String signature) {

		logger.debug("Received Messenger Platform callback - payload: {} | signature: {}", payload, signature);
		try {
			this.receiveClient.processCallbackPayload(payload, signature);
			logger.debug("Processed callback payload successfully");
			return ResponseEntity.status(HttpStatus.OK).build();
		} catch (MessengerVerificationException e) {
			logger.warn("Processing of callback payload failed: {}", e.getMessage());
			return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
		}
	}

	private PostbackEventHandler newPostbackEventHandler() {
		return event -> {
			logger.debug("Received PostbackEvent: {}", event);

			final String senderId = event.getSender().getId();
			final String recipientId = event.getRecipient().getId();
			final String payload = event.getPayload();
			final Date timestamp = event.getTimestamp();

			logger.info("Received postback for user '{}' and page '{}' with payload '{}' at '{}'", senderId,
					recipientId, payload, timestamp);

			sendTextMessage(senderId, "Postback called");
		};
	}

	private AccountLinkingEventHandler newAccountLinkingEventHandler() {
		return event -> {
			logger.debug("Received AccountLinkingEvent: {}", event);

			final String senderId = event.getSender().getId();
			final AccountLinkingEvent.AccountLinkingStatus accountLinkingStatus = event.getStatus();
			final String authorizationCode = event.getAuthorizationCode();

			logger.info("Received account linking event for user '{}' with status '{}' and auth code '{}'", senderId,
					accountLinkingStatus, authorizationCode);
		};
	}

	private OptInEventHandler newOptInEventHandler() {
		return event -> {
			logger.debug("Received OptInEvent: {}", event);

			final String senderId = event.getSender().getId();
			final String recipientId = event.getRecipient().getId();
			final String passThroughParam = event.getRef();
			final Date timestamp = event.getTimestamp();

			logger.info("Received authentication for user '{}' and page '{}' with pass through param '{}' at '{}'",
					senderId, recipientId, passThroughParam, timestamp);

			sendTextMessage(senderId, "Authentication successful");
		};
	}

	private EchoMessageEventHandler newEchoMessageEventHandler() {
		return event -> {
			logger.debug("Received EchoMessageEvent: {}", event);

			final String messageId = event.getMid();
			final String recipientId = event.getRecipient().getId();
			final String senderId = event.getSender().getId();
			final Date timestamp = event.getTimestamp();

			logger.info("Received echo for message '{}' that has been sent to recipient '{}' by sender '{}' at '{}'",
					messageId, recipientId, senderId, timestamp);
		};
	}

	private MessageDeliveredEventHandler newMessageDeliveredEventHandler() {
		return event -> {
			logger.debug("Received MessageDeliveredEvent: {}", event);

			final List<String> messageIds = event.getMids();
			final Date watermark = event.getWatermark();
			final String senderId = event.getSender().getId();

			if (messageIds != null) {
				messageIds.forEach(messageId -> {
					logger.info("Received delivery confirmation for message '{}'", messageId);
				});
			}

			logger.info("All messages before '{}' were delivered to user '{}'", watermark, senderId);
		};
	}

	private MessageReadEventHandler newMessageReadEventHandler() {
		return event -> {
			logger.debug("Received MessageReadEvent: {}", event);

			final Date watermark = event.getWatermark();
			final String senderId = event.getSender().getId();

			logger.info("All messages before '{}' were read by user '{}'", watermark, senderId);
		};
	}

	/**
	 * This handler is called when either the message is unsupported or when the
	 * event handler for the actual event type is not registered. In this
	 * showcase all event handlers are registered. Hence only in case of an
	 * unsupported message the fallback event handler is called.
	 */
	private FallbackEventHandler newFallbackEventHandler() {
		return event -> {
			logger.debug("Received FallbackEvent: {}", event);

			final String senderId = event.getSender().getId();
			logger.info("Received unsupported message from user '{}'", senderId);
		};
	}

	public void sendTextMessage(String recipientId, String text) {
		try {
			final Recipient recipient = Recipient.newBuilder().recipientId(recipientId).build();
			final NotificationType notificationType = NotificationType.REGULAR;
			final String metadata = "DEVELOPER_DEFINED_METADATA";

			this.sendClient.sendTextMessage(recipient, notificationType, text, metadata);
		} catch (MessengerApiException | MessengerIOException e) {
			handleSendException(e);
		}
	}

	private void handleSendException(Exception e) {
		logger.error("Message could not be sent. An unexpected error occurred.", e);
	}

	private void handleIOException(Exception e) {
		logger.error("Could not open Spring.io page. An unexpected error occurred.", e);
	}

}