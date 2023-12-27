package Item.Book;

import Enums.Genre;
import Enums.ItemState;
import Enums.Language;

public class EnglishBookBuilder implements IBookBuilder{
    Book book;
    public EnglishBookBuilder(){this.book = new Book();}
    public void setTitle(String title){book.setTitle(title);}
    public void setAuthor(String author){book.setAuthorName(author);}
    public void setPubDate(String pubDate){book.setPublicationDate(pubDate);}
    public void setGenre(){book.setGenre(Genre.EnglishLiterature);}
    public void setLang(){book.setLanguage(Language.EN);}
    public void setState(){book.setItemState(ItemState.AVAILABLE);}
    public Book getBook() {return book;}
    public void reset(){this.book = new Book();}
}
