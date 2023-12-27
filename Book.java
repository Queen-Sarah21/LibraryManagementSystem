package Item.Book;

import Item.Item;

public class Book extends Item {
    private static int bookCounter = 1000;
    public Book(){
        setId();
    }
    public void setId(){
        this.id = "BK" + bookCounter;
        bookCounter++;
    }
    public void display(){
        System.out.println("---------------- Book ID " + id + " ----------------");
        System.out.println("\tTitle: " + title + "\n\tAuthor: " + authorName);
        System.out.println("\tPublication date: " + publicationDate + "\n\tGenre: " + genre);
        System.out.println("\tLanguage: " + language + "\n\tCurrent state: " + itemState + "\n");
    }
}
