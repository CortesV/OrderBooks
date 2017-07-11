package com.softbistro.orderbooks.components.entity;

import java.util.List;

public class CardBooks {

	private static List<Book> booksInCard;

	public static List<Book> getBooksInCard() {
		return booksInCard;
	}

	public static void setBooksInCard(List<Book> booksInCard) {
		CardBooks.booksInCard = booksInCard;
	}

}
