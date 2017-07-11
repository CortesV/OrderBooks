package com.softbistro.orderbooks.components.entity;

import java.util.List;

public class CardBooks {

	private static List<Book> booksInCard;
	private static Book chooseBook;
	private static String choosePrice;

	public static List<Book> getBooksInCard() {
		return booksInCard;
	}

	public static void setBooksInCard(List<Book> booksInCard) {
		CardBooks.booksInCard = booksInCard;
	}

	public static Book getChooseBook() {
		return chooseBook;
	}

	public static void setChooseBook(Book chooseBook) {
		CardBooks.chooseBook = chooseBook;
	}

	public static String getChoosePrice() {
		return choosePrice;
	}

	public static void setChoosePrice(String choosePrice) {
		CardBooks.choosePrice = choosePrice;
	}	
}
