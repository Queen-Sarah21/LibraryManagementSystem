package Item.Comic;

import Item.Book.Book;
import Item.Book.IBookBuilder;

public class ComicMaker {
    private IComicBuilder iComicBuilder;
    public ComicMaker(IComicBuilder iComicBuilder){this.iComicBuilder = iComicBuilder;}
    public void makeComic(String title, String author, String illustrator, String pubDate){
        iComicBuilder.setTitle(title);
        iComicBuilder.setAuthor(author);
        iComicBuilder.setIllustrator(illustrator);
        iComicBuilder.setPubDate(pubDate);
        iComicBuilder.setGenre();
        iComicBuilder.setLang();
        iComicBuilder.setState();
    }
    public Comic getBook(){
        Comic comic = this.iComicBuilder.getComic();
        this.iComicBuilder.reset();
        return comic;
    }
}
