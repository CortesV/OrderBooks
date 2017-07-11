package com.softbistro.orderbooks.components.entity;

import java.util.List;

public class Book {
	private String id;
	private String title;
	private String isbn;
	private String ean;
	private String imageUrl;
	private String price;
	private List<String> authors;
	

	public Book(String id, String title, String isbn, String ean, String imageUrl, List<String> authors) {

		this.id = id;
		this.title = title;
		this.isbn = isbn;
		this.imageUrl = imageUrl;
		this.authors = authors;
		this.ean = ean;
	}

	public Book() {
	}

	public String getEan() {
		return ean;
	}

	public void setEan(String ean) {
		this.ean = ean;
	}

	public String getImageUrl() {
		return imageUrl;
	}

	public void setImageUrl(String imageUrl) {
		this.imageUrl = imageUrl;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getIsbn() {
		return isbn;
	}

	public void setIsbn(String isbn) {
		this.isbn = isbn;
	}

	public List<String> getAuthors() {
		return authors;
	}

	public void setAuthors(List<String> authors) {
		this.authors = authors;
	}

	public String getPrice() {
		return price;
	}

	public void setPrice(String price) {
		this.price = price;
	}

}
