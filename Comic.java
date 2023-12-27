package Item.Comic;

import Item.Item;

public class Comic extends Item {
    private String illustratorName;
    private static int comicCounter = 1000;
    public Comic(){
        setId();
    }
    public void setId(){
        this.id = "CMC" + comicCounter;
        comicCounter++;
    }
    public String getIllustratorName() {
        return illustratorName;
    }

    public void setIllustratorName(String illustratorName) {
        this.illustratorName = illustratorName;
    }

    public void display(){
        System.out.println("---------------- Comic ID " + id + " ----------------");
        System.out.println("\tTitle: " + title + "\n\tAuthor: " + authorName + "\n\tIllustrator: " + illustratorName);
        System.out.println("\tPublication date: " + publicationDate + "\n\tGenre: " + genre);
        System.out.println("\tLanguage: " + language + "\n\tCurrent state: " + itemState + "\n");
    }
}
