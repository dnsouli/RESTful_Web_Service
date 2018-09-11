package jayray.net.assignment2;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import java.util.*;
import java.lang.*;
import java.io.*;

//import jayray.net.orders.Address;
//import jayray.net.orders.Namespaces;

@XmlRootElement(namespace = "http://jayray.net/orders")
@XmlAccessorType(XmlAccessType.FIELD)
public class Book implements Comparable<Book> {
	public int id;
	public String title;
	public String author;
	public String publisher;
	public int year;
	public String available;
	
	public Book() {
        this.id = id;
        this.title = title;
        this.author = author;
        this.publisher = publisher;
        this.year = year;
        this.available = available;
    }
	
	public Book(int id, String title, String author, String publisher, int year, String available) {
        this.id = id;
        this.title = title;
        this.author = author;
        this.publisher = publisher;
        this.year = year;
        this.available = available;
    }

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}
	
	public String getAuthor() {
		return author;
	}

	public void setAuthor(String author) {
		this.author = author;
	}
	
	public String getPublisher() {
		return publisher;
	}

	public void setPublisher(String publisher) {
		this.publisher = publisher;
	}
	
	public int getYear() {
		return year;
	}

	public void setYear(int year) {
		this.year = year;
	}
	
	public String getAvailable() {
		return available;
	}

	public void setAvailable(String available) {
		this.available = available;
	}

	
	public int compareTo(Book cbook) {
		
			int compID = ((Book) cbook).getId(); 
			
			//descending order by ID
			return compID - this.id;
	}
	
	
	

}
