package Item.Comic;

import Enums.Genre;
import Enums.ItemState;
import Enums.Language;
import Item.Book.Book;

public class EnglishComicBuilder implements IComicBuilder {
    Comic comic;
    public EnglishComicBuilder(){this.comic = new Comic();}
    public void setTitle(String title){comic.setTitle(title);}
    public void setAuthor(String author){comic.setAuthorName(author);}
    public void setIllustrator(String illustrator){comic.setIllustratorName(illustrator);}
    public void setPubDate(String pubDate){comic.setPublicationDate(pubDate);}
    public void setGenre(){comic.setGenre(Genre.EnglishLiterature);}
    public void setLang(){comic.setLanguage(Language.EN);}
    public void setState(){comic.setItemState(ItemState.AVAILABLE);}
    public Comic getComic() {return comic;}
    public void reset(){this.comic = new Comic();}
}
