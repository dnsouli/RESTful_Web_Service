package jayray.net.assignment2;

import java.util.List;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;

import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.ResponseBuilder;

import org.apache.log4j.Logger;

import jayray.net.hello.HelloWorldResource;

@Path("books")
public class BookDemo {
	public static String DB_URI = "jdbc:mysql://localhost/librarydemo";
	public static String DB_USER = "root";
	public static String DB_PASS = "";
	
	//LOOKUP GET METHOD///////////////////////////////////////////////////////////////////
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	@GET
    @Produces({MediaType.APPLICATION_JSON})
	public List<Book> getBooks(@QueryParam("id")int id, @QueryParam("title")String title, 
			@QueryParam("author")String author, @QueryParam("limit")int limit, @QueryParam("sortby")String sortby, 
			@QueryParam("order")String order) {
		List<Book> books = new ArrayList<Book>();
		try {
            // The newInstance() call is a work around for some
            // broken Java implementations

            Class.forName("com.mysql.jdbc.Driver").newInstance();
        } catch (Exception ex) {
            // handle the error
        		throw new IllegalStateException("Driver not found!", ex);
        }
		Connection conn = null;
		try {
			conn = DriverManager.getConnection(DB_URI, DB_USER, DB_PASS);
			Statement stmt = conn.createStatement();
			String sql;
			sql = "SELECT * FROM book WHERE";
			if(id == 0 && title == null && author ==null){
				sql = "SELECT * FROM book";
			}
			if (id != 0) {
				sql += " id=" + id;
				
				
			}
			if (id !=0 && title != null) {
				sql += " AND title=" + "'" + title + "'";
				
				
			}
			else if (title != null){
				sql += " title=" + "'" + title + "'";
			
				
			}
			if ((id !=0 && author != null) || (title != null && author != null)) {
				sql += " AND author=" + "'" + author + "'";
				
				
			}
			else if (author != null){
				sql += " author=" + "'" + author + "'";
				
			}
			
			sql += ";";
			
			ResultSet rs = stmt.executeQuery(sql);
			while (rs.next()) {
				int id1 = rs.getInt("id");
				String title1 = rs.getString("title");
				String author1 = rs.getString("author");
				String publisher1 = rs.getString("publisher");
				int year1 = rs.getInt("year");
				String available1 = rs.getString("available");
				Book b = new Book();
				b.setId(id1);
				b.setTitle(title1);
				b.setAuthor(author1);
				b.setPublisher(publisher1);
				b.setYear(year1);
				b.setAvailable(available1);
				books.add(b);
				if(books.size()==limit){
					break;
				}
			}
		} catch (SQLException ex) {
			// handle any errors
			System.out.println("SQLException: " + ex.getMessage());
			System.out.println("SQLState: " + ex.getSQLState());
			System.out.println("VendorError: " + ex.getErrorCode());
			throw new IllegalStateException("Cannot connect the database!", ex);
		}
		
		//if statements to check how to sort/order the list
		if(sortby!=null && order!=null && sortby.equals("title") && order.equals("asc")){
			books.sort(Comparator.comparing(Book::getTitle));
		}
		if(sortby!=null && order!=null && sortby.equals("title") && order.equals("desc")){
			books.sort(Comparator.comparing(Book::getTitle));
			Collections.reverse(books);
		}
		if(sortby!=null && order!=null && sortby.equals("author") && order.equals("asc")){
			books.sort(Comparator.comparing(Book::getAuthor));
		}
		if(sortby!=null && order!=null && sortby.equals("author") && order.equals("desc")){
			books.sort(Comparator.comparing(Book::getAuthor));
			Collections.reverse(books);
		}
		if(sortby!=null && order!=null && sortby.equals("id") && order.equals("asc")){
			Collections.sort(books);
			Collections.reverse(books);
		}
		if(sortby!=null && order!=null && sortby.equals("id") && order.equals("desc")){
			Collections.sort(books);
		}

		return books;
	}

	
	
	//ADD BOOKS METHOD///////////////////////////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////////////////////////
	
	@POST
	@Consumes(MediaType.APPLICATION_JSON)
    @Produces({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
    public Response addBook(Book book) {
		try {
            // The newInstance() call is a work around for some
            // broken Java implementations

            Class.forName("com.mysql.jdbc.Driver").newInstance();
        } catch (Exception ex) {
            // handle the error
        		throw new IllegalStateException("Driver not found!", ex);
        }
		Connection conn = null;
		try {
			conn = DriverManager.getConnection(DB_URI, DB_USER, DB_PASS);
			Statement stmt = conn.createStatement();
			String sql;
			//checks to see if all fields are filled for an official addition to the database, if not bad request response invoked
			if(book.getTitle() == null || book.getAuthor() ==null || book.getPublisher() == null || book.getYear() == 0 || book.getAvailable() == null){
				System.out.println("Error: Required fields not filled");
				return Response.status(Response.Status.BAD_REQUEST).entity("Bad request").build();
			}
			//check to see if book is already in library (duplicate), if so invokes conflict response
			else if(getBooks(0, book.getTitle(), book.getAuthor(), 0, null, null).size() != 0){
				return Response.status(Response.Status.CONFLICT).entity("Duplicate record: /books/" + getBooks(0, book.getTitle(), book.getAuthor(), 0, null, null).get(0).id).type(MediaType.TEXT_PLAIN).build();
			}
			//if all checks are passed, book is ready to be added, invoke created response as well
			else{
			sql = "INSERT INTO `book` (title,author,publisher,year,AVAILABLE) "
					+ "VALUES ("+ "'" + book.getTitle() + "', '" + book.getAuthor() + "', '" + book.getPublisher() +"', " 
					+ book.getYear() + ", '" + book.getAvailable()+ "');";
			
			int rs = stmt.executeUpdate(sql);
			sql = "SELECT id FROM book WHERE title = '" + book.getTitle() + "';";
			int idmatch = 0;
			ResultSet rs1 = stmt.executeQuery(sql);
			if (rs1.next()) {
				idmatch = rs1.getInt("id");
			}
			
			return Response.status(Response.Status.CREATED).entity("http://localhost:8080/jersey-starterkit-master/rest/books/" + idmatch).type(MediaType.TEXT_PLAIN).build();
			}
		} catch (SQLException ex) {
			// handle any errors
			System.out.println("SQLException: " + ex.getMessage());
			System.out.println("SQLState: " + ex.getSQLState());
			System.out.println("VendorError: " + ex.getErrorCode());
			throw new IllegalStateException("Cannot connect the database!", ex);
		}
    }
	
	
	//DELETE METHOD//////////////////////////////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////////////////////////
	
	private static final Logger logger = Logger.getLogger(HelloWorldResource.class); //for debugging purposes
	@DELETE
	@Path("/{id}")
    @Produces({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
	public Response deleteBook(@PathParam("id") int id){
		try {
            // The newInstance() call is a work around for some
            // broken Java implementations

            Class.forName("com.mysql.jdbc.Driver").newInstance();
        } catch (Exception ex) {
            // handle the error
        		throw new IllegalStateException("Driver not found!", ex);
        }
		Connection conn = null;
		try {
			conn = DriverManager.getConnection(DB_URI, DB_USER, DB_PASS);
			Statement stmt = conn.createStatement();
			String sql;
			//check to see if book is in library and id is valid
			if(id != 0 && (getBooks(id, null, null, 0, null, null).size() != 0)){
				sql = "DELETE FROM book WHERE id = " + id + ";";
				int rs = stmt.executeUpdate(sql);
			}
			//if book is not found in database, no record response
			else{
				return Response.status(Response.Status.NOT_FOUND).entity("No book record").type(MediaType.TEXT_PLAIN).build();
			}
		} catch (SQLException ex) {
			// handle any errors
			System.out.println("SQLException: " + ex.getMessage());
			System.out.println("SQLState: " + ex.getSQLState());
			System.out.println("VendorError: " + ex.getErrorCode());
			throw new IllegalStateException("Cannot connect the database!", ex);
		}
		return Response.ok("OK", MediaType.APPLICATION_JSON).build();
    }
	
	//LOANING AND RETURNING METHOD//////////////////////////////////////////////////////////////////////
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	@PUT
	@Path("/{id}")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
    public Response loanBook(@PathParam("id") int id, Avail a) {
		try {
            // The newInstance() call is a work around for some
            // broken Java implementations

            Class.forName("com.mysql.jdbc.Driver").newInstance();
        } catch (Exception ex) {
            // handle the error
        		throw new IllegalStateException("Driver not found!", ex);
        }
		Connection conn = null;
		try {
			conn = DriverManager.getConnection(DB_URI, DB_USER, DB_PASS);
			Statement stmt = conn.createStatement();
			String sql;
			//check if book is in database
			if((getBooks(id, null, null, 0, null, null)).size() == 0){
				return Response.status(Response.Status.NOT_FOUND).entity("No book record").type(MediaType.TEXT_PLAIN).build();
			}
			//if book is being loaned
			if(id != 0 && (getBooks(id, null, null, 0, null, null)).size() != 0 && (getBooks(id, null, null, 0, null, null)).get(0).available.equals("Y") 
				&& !(a.getAvailData())){
			sql = "UPDATE book SET available='N' WHERE id= " + id + ";";
			int rs = stmt.executeUpdate(sql);
			}
			//if book is being returned
			else if(id != 0 && (getBooks(id, null, null, 0, null, null)).size() != 0 && (getBooks(id, null, null, 0, null, null)).get(0).available.equals("N") 
					&& (a.getAvailData())){
				sql = "UPDATE book SET available='Y' WHERE id= " + id + ";";
				int rs = stmt.executeUpdate(sql);
			}
			//if none of the above conditions are met, the request is invalid
			else{ 
				return Response.status(Response.Status.BAD_REQUEST).build();
			}
			
		} catch (SQLException ex) {
			// handle any errors
			System.out.println("SQLException: " + ex.getMessage());
			System.out.println("SQLState: " + ex.getSQLState());
			System.out.println("VendorError: " + ex.getErrorCode());
			throw new IllegalStateException("Cannot connect the database!", ex);
		}
		return Response.ok("OK", MediaType.APPLICATION_JSON).build();
    }
	
}