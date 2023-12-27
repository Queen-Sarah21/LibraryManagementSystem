package Item.Factory;

import Enums.Genre;
import Item.Book.Book;
import Item.Comic.Comic;

public abstract class AbstractFactory {
    static FrenchFactory frenchFactory = new FrenchFactory();
    static EnglishFactory englishFactory = new EnglishFactory();
    public static AbstractFactory factory(Genre genre){
        AbstractFactory factory = null;
        switch(genre){
            case FrenchLiterature:
                factory = frenchFactory;
                break;
            case EnglishLiterature:
                factory = englishFactory;
                break;
        }
        return  factory;
    }
    public abstract Book createBook(String title, String author, String pubDate);
    public abstract Comic createComic(String title, String author, String illustrator, String pubDate);
}
