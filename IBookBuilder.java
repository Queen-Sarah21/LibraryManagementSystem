package Item.Book;

import Enums.*;

public interface IBookBuilder {
    void setTitle(String title);
    void setAuthor(String author);
    void setPubDate(String date);
    void setGenre();
    void setLang();
    void setState();
    Book getBook();
    void reset();

}
