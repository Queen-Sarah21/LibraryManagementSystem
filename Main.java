import Enums.Genre;
import Item.Book.Book;
import Item.Comic.Comic;
import Item.Factory.AbstractFactory;
import LibraryManager.LibraryManager;

public class Main {
    public static void main(String[] args) {
        LibraryManager libraryManager = new LibraryManager();
        libraryManager.run();
    }
}