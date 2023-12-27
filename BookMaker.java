package Item.Book;

public class BookMaker {
    private IBookBuilder iBookBuilder;
    public BookMaker(IBookBuilder iBookBuilder){this.iBookBuilder = iBookBuilder;}
    public void makeBook(String title, String author, String pubDate){
        iBookBuilder.setTitle(title);
        iBookBuilder.setAuthor(author);
        iBookBuilder.setPubDate(pubDate);
        iBookBuilder.setGenre();
        iBookBuilder.setLang();
        iBookBuilder.setState();
    }
    public Book getBook(){
        Book book = this.iBookBuilder.getBook();
        this.iBookBuilder.reset();
        return book;
    }
}
