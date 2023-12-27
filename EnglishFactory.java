package Item.Factory;

import Item.Book.*;
import Item.Comic.*;

public class EnglishFactory extends AbstractFactory{
    public Book createBook(String title, String author, String pubDate){
        IBookBuilder englishBookBuilder = new EnglishBookBuilder();
        BookMaker bookMaker = new BookMaker(englishBookBuilder);
        bookMaker.makeBook(title, author, pubDate);
        return bookMaker.getBook();
    }

    public Comic createComic(String title, String author, String illustrator, String pubDate){
        IComicBuilder englishComicBuilder = new EnglishComicBuilder();
        ComicMaker comicMaker = new ComicMaker(englishComicBuilder);
        comicMaker.makeComic(title, author, illustrator, pubDate);
        return comicMaker.getBook();
    }
}
