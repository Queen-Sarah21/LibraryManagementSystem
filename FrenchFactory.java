package Item.Factory;

import Item.Book.*;
import Item.Comic.*;

public class FrenchFactory extends AbstractFactory{
    public Book createBook(String title, String author, String pubDate){
        IBookBuilder frenchBookBuilder = new FrenchBookBuilder();
        BookMaker bookMaker = new BookMaker(frenchBookBuilder);
        bookMaker.makeBook(title, author, pubDate);
        return bookMaker.getBook();
    }

    public Comic createComic(String title, String author, String illustrator, String pubDate){
        IComicBuilder frenchComicBuilder = new FrenchComicBuilder();
        ComicMaker comicMaker = new ComicMaker(frenchComicBuilder);
        comicMaker.makeComic(title, author, illustrator, pubDate);
        return comicMaker.getBook();
    }
}
