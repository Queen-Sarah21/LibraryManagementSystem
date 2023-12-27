package Item.Comic;

import Item.Book.Book;

public interface IComicBuilder {
    void setTitle(String title);
    void setAuthor(String author);
    void setIllustrator(String illustrator);
    void setPubDate(String date);
    void setGenre();
    void setLang();
    void setState();
    Comic getComic();
    void reset();

}
