package com.softbistro.orderbooks.components.entity;

import java.util.ArrayList;
import java.util.List;

public class OrderCart {

	public static List<Book> booksInCard;
	public static Book chooseBook;
	public static String choosePrice;
	public static List<Book> searchBooks;
	public static List<String> prices;
	public OrderCart() {
		searchBooks = new ArrayList<>();
	}
	
	
}
